package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLocEntity;
import com.yunyou.modules.wms.outbound.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 销售单生成发运单
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundCreateSoService {
    @Autowired
    private BanQinWmSaleHeaderService wmSaleHeaderService;
    @Autowired
    private BanQinWmSaleDetailService wmSaleDetailService;
    @Autowired
    private BanQinWmSoHeaderService wmSoHeaderService;
    @Autowired
    private BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;
    @Autowired
    private BanQinOutboundSoService outboundSoService;

    /**
     * 可选多个同货主，同收货人的Sale单，生成一个SO单
     *
     * @param ownerCode
     * @param consigneeCode
     * @param wmSaleDetailEntitys
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundCreateSo(String ownerCode, String consigneeCode, List<BanQinWmSaleDetailEntity> wmSaleDetailEntitys) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 校验是否可以生成SO单
        List<String> saleNos = new ArrayList<>();
        List<BanQinWmSaleDetailEntity> saleDetailList = new ArrayList<>();
        for (BanQinWmSaleDetailEntity entity : wmSaleDetailEntitys) {
            if (entity.getCurrentQtySoEa() > 0D) {
                saleDetailList.add(entity);
                if (!saleNos.contains(entity.getSaleNo())) {
                    saleNos.add(entity.getSaleNo());
                }
            }
        }
        if (saleDetailList.size() == 0) {
            throw new WarehouseException("不是同一个SO");
        }
        String soNo = "";
        // SO单头
        BanQinWmSoEntity soEntity = new BanQinWmSoEntity();
        // 如果是来自一个销售单，则带销售单的值到出库单。
        if (saleNos.size() == 1) {
            BanQinWmSaleHeader wmSaleHeaderModel = wmSaleHeaderService.getBySaleNo(saleNos.get(0), wmSaleDetailEntitys.get(0).getOrgId());
            BeanUtils.copyProperties(wmSaleHeaderModel, soEntity);
            soEntity.setId(IdGen.uuid());
        } else {
            soEntity.setOwnerCode(ownerCode);
            soEntity.setConsigneeCode(consigneeCode);
        }
        soEntity.setAuditOp(null);
        soEntity.setAuditTime(null);
        soEntity.setEdiSendTime(null);
        soEntity.setIsEdiSend(WmsConstants.EDI_FLAG_00);
        soEntity.setSoType(WmsCodeMaster.SO_PO.getCode());
        soEntity.setOrderTime(new Date());
        wmSoHeaderService.saveSoHeader(soEntity);
        soNo = soEntity.getSoNo();
        for (BanQinWmSaleDetailEntity wmSaleDetailEntity : saleDetailList) {
            BanQinCdWhSku skuModel = cdWhSkuService.getByOwnerAndSkuCode(wmSaleDetailEntity.getOwnerCode(), wmSaleDetailEntity.getSkuCode(), wmSaleDetailEntity.getOrgId());
            if (null == skuModel) {
                throw new WarehouseException("缺少参数", wmSaleDetailEntity.getOwnerCode(), wmSaleDetailEntity.getSkuCode());
            }
            // SO SKU明细
            BanQinWmSoDetailEntity wmSoDetailModel = new BanQinWmSoDetailEntity();
            BeanUtils.copyProperties(wmSaleDetailEntity, wmSoDetailModel);
            wmSoDetailModel.setId(IdGen.uuid());
            wmSoDetailModel.setSoNo(soNo);
            wmSoDetailModel.setSaleLineNo(wmSaleDetailEntity.getLineNo());
            wmSoDetailModel.setQtySoEa(wmSaleDetailEntity.getCurrentQtySoEa());
            wmSoDetailModel.setLogisticLineNo(null);
            wmSoDetailModel.setLogisticNo(null);
            wmSoDetailModel.setAllocRule(skuModel.getAllocRule());
            wmSoDetailModel.setPreallocRule(skuModel.getPreallocRule());
            wmSoDetailModel.setRotationRule(skuModel.getRotationRule());
            this.wmSoDetailService.saveSoDetail(wmSoDetailModel);
            // 更新Sale明细SO数
            wmSaleDetailEntity.setQtySoEa(wmSaleDetailEntity.getQtySoEa() + wmSaleDetailEntity.getCurrentQtySoEa());
            BanQinWmSaleDetail wmSaleDetailModel = new BanQinWmSaleDetail();
            BeanUtils.copyProperties(wmSaleDetailEntity, wmSaleDetailModel);
            this.wmSaleDetailService.save(wmSaleDetailModel);
        }

        msg.setSuccess(true);
        msg.addMessage("操作成功，生成的SO单" + soNo);
        return msg;
    }

    /**
     * 按照库存数据生成SO
     *
     * @param list
     */
    public ResultMessage createSoByInv(List<BanQinWmInvLotLocEntity> list) {
        ResultMessage msg = new ResultMessage();
        if (CollectionUtil.isNotEmpty(list)) {
            // 生成SO单
            ResultMessage message = createSo(list);
            BanQinWmSoHeader wmSoHeader = (BanQinWmSoHeader) message.getData();
            // 审核
            wmSoHeaderService.audit(wmSoHeader.getSoNo(), wmSoHeader.getOrgId());
            msg.addMessage("生成SO单成功，单号:[" + wmSoHeader.getSoNo() + "] ");
            // 手工分配
            this.alloc(msg, wmSoHeader.getSoNo(), list);
        }
        return msg;
    }

    @Transactional
    public ResultMessage createSo(List<BanQinWmInvLotLocEntity> list) {
        BanQinWmInvLotLocEntity entity = list.get(0);
        BanQinWmSoEntity soEntity = new BanQinWmSoEntity();
        soEntity.setOwnerCode(entity.getOwnerCode());
        soEntity.setSoType(WmsCodeMaster.SO_PO.getCode());
        soEntity.setOrderTime(entity.getSoOrderTime() == null ? new Date() : entity.getSoOrderTime());
        soEntity.setOrgId(entity.getOrgId());
        soEntity.setCarrierCode(entity.getCarrierCode());
        soEntity.setCarrierName(entity.getCarrierName());
        soEntity.setVehicleNo(entity.getVehicleNo());
        soEntity.setDriver(entity.getDriver());
        soEntity.setDriverTel(entity.getDriverTel());
        soEntity.setConsigneeAddr(entity.getConsigneeAddr());
        soEntity.setDef5(entity.getCustomerNo());
        soEntity.setRemarks(entity.getRemarks());
        ResultMessage message = outboundSoService.saveSoEntity(soEntity);
        if (message.isSuccess()) {
            BanQinWmSoHeader wmSoHeader = (BanQinWmSoHeader) message.getData();
            for (int i = 0; i < list.size(); i++) {
                BanQinWmInvLotLocEntity invEntity = list.get(i);
                BanQinCdWhSku sku = cdWhSkuService.getByOwnerAndSkuCode(invEntity.getOwnerCode(), invEntity.getSkuCode(), invEntity.getOrgId());
                BanQinWmSoDetailEntity soDetailEntity = new BanQinWmSoDetailEntity();
                soDetailEntity.setHeadId(wmSoHeader.getId());
                soDetailEntity.setSoNo(wmSoHeader.getSoNo());
                soDetailEntity.setLineNo(String.format("%04d", i + 1));
                soDetailEntity.setStatus(WmsCodeMaster.SO_NEW.getCode());
                soDetailEntity.setOwnerCode(invEntity.getOwnerCode());
                soDetailEntity.setSkuCode(invEntity.getSkuCode());
                soDetailEntity.setPackCode(sku.getPackCode());
                soDetailEntity.setUom(WmsConstants.UOM_EA);
                soDetailEntity.setQtySoEa(invEntity.getQty());
                soDetailEntity.setOrgId(invEntity.getOrgId());
                soDetailEntity.setRotationRule(sku.getRotationRule());
                soDetailEntity.setAllocRule(sku.getAllocRule());
                soDetailEntity.setLotAtt01(invEntity.getLotAtt01());
                soDetailEntity.setLotAtt02(invEntity.getLotAtt02());
                soDetailEntity.setLotAtt03(invEntity.getLotAtt03());
                soDetailEntity.setLotAtt04(invEntity.getLotAtt04());
                soDetailEntity.setLotAtt05(invEntity.getLotAtt05());
                soDetailEntity.setLotAtt06(invEntity.getLotAtt06());
                soDetailEntity.setLotAtt07(invEntity.getLotAtt07());
                soDetailEntity.setLotAtt08(invEntity.getLotAtt08());
                soDetailEntity.setLotAtt09(invEntity.getLotAtt09());
                soDetailEntity.setLotAtt10(invEntity.getLotAtt10());
                soDetailEntity.setLotAtt11(invEntity.getLotAtt11());
                soDetailEntity.setLotAtt12(invEntity.getLotAtt12());
                soDetailEntity.setOutboundTime(invEntity.getOutboundTime() == null ? new Date() : invEntity.getOutboundTime());
                outboundSoService.saveSoDetailEntity(soDetailEntity);
            }
        }

        return message;
    }

    public void alloc(ResultMessage msg, String soNo, List<BanQinWmInvLotLocEntity> list) {
        int i = 0;
        for (BanQinWmInvLotLocEntity entity : list) {
            BanQinWmSoAllocEntity allocEntity = new BanQinWmSoAllocEntity();
            allocEntity.setSoNo(soNo);
            allocEntity.setLineNo(String.format("%04d", i + 1));
            allocEntity.setStatus(WmsCodeMaster.ALLOC_FULL_ALLOC.getCode());
            allocEntity.setOwnerCode(entity.getOwnerCode());
            allocEntity.setSkuCode(entity.getSkuCode());
            allocEntity.setLotNum(entity.getLotNum());
            allocEntity.setLocCode(entity.getLocCode());
            allocEntity.setTraceId(entity.getTraceId());
            allocEntity.setPackCode(entity.getPackCode());
            allocEntity.setUom(WmsConstants.UOM_EA);
            allocEntity.setQtyEa(entity.getQty());
            allocEntity.setQtyUom(entity.getQty());
            allocEntity.setToLoc(WmsConstants.SORTATION);
            allocEntity.setToId("*");
            allocEntity.setOrgId(entity.getOrgId());
            i++;
            ResultMessage message = outboundSoService.manualAlloc(allocEntity);
            if (!message.isSuccess()) {
                msg.addMessage(message.getMessage());
            }
        }
    }
}
