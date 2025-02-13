package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmOrderDetailAppendix;
import com.yunyou.modules.oms.order.entity.OmTaskDetail;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.service.OmOrderDetailAppendixService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.service.BanQinEbCustomerService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnHeader;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnHeaderService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoHeader;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundSoService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PushWmsService extends BaseService {
    @Autowired
    private BanQinEbCustomerService ebCustomerService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;
    @Autowired
    private BanQinWmAsnHeaderService wmAsnHeaderService;
    @Autowired
    private BanQinWmAsnDetailService wmAsnDetailService;
    @Autowired
    private BanQinWmSoHeaderService wmSoHeaderService;
    @Autowired
    private BanQinOutboundSoService outboundSoService;
    @Autowired
    private OmOrderDetailAppendixService omOrderDetailAppendixService;

    private BanQinEbCustomer getCustomer(String customerNo, String type, String orgId) {
        if (StringUtils.isBlank(customerNo) || StringUtils.isBlank(orgId)) {
            return new BanQinEbCustomer();
        }
        BanQinEbCustomer ebCustomer = ebCustomerService.getByCode(customerNo, orgId);
        if (ebCustomer == null || !ebCustomer.getEbcuType().contains(type)) {
            throw new GlobalException(SystemAliases.WMS.getDesc() + CustomerType.value(type).getDesc() + "[" + customerNo + "]不存在");
        }
        return ebCustomer;
    }

    private BanQinCdWhSku getSku(String ownerCode, String skuCode, String orgId) {
        if (StringUtils.isBlank(ownerCode) || StringUtils.isBlank(skuCode) || StringUtils.isBlank(orgId)) {
            return new BanQinCdWhSku();
        }
        BanQinCdWhSku cdWhSku = cdWhSkuService.getByOwnerAndSkuCode(ownerCode, skuCode, orgId);
        if (cdWhSku == null) {
            throw new OmsException(SystemAliases.WMS.getDesc() + "货主[" + ownerCode + "]商品[" + skuCode + "]不存在");
        }
        return cdWhSku;
    }

    @Transactional
    public void deleteInputTask(String sourceNo, String dataSource, String orgId) {
        String getSql = "select concat_ws(',', id, status) from wm_asn_header where def3 = '" + sourceNo + "' and data_source = '" + dataSource + "' and org_id = '" + orgId + "'";
        List<Object> objects = wmAsnHeaderService.executeSelectSql(getSql);
        if (CollectionUtil.isEmpty(objects) || objects.size() <= 0) {
            return;
        }
        for (Object object : objects) {
            String item = (String) object;
            String[] itemList = item.split(",");
            String id = itemList[0];
            String status = itemList[1];
            if (!WmsCodeMaster.ASN_NEW.getCode().equals(status)) {
                throw new OmsException(SystemAliases.WMS.getDesc() + "已进行操作，无法删除");
            }
            String deleteSql = "delete from wm_asn_header where id = '" + id + "'";
            String deleteDetailSql = "delete from wm_asn_detail where head_id = '" + id + "'";
            String deleteDetailReceiveSql = "delete from wm_asn_detail_receive where head_id = '" + id + "'";
            wmAsnHeaderService.executeDeleteSql(deleteSql);
            wmAsnDetailService.executeDeleteSql(deleteDetailSql);
            wmAsnDetailService.executeDeleteSql(deleteDetailReceiveSql);
        }
    }

    @Transactional
    public void deleteOutputTask(String sourceNo, String dataSource, String orgId) {
        String getSql = "select concat_ws(',', id, status) from wm_so_header where def3 = '" + sourceNo + "' and data_source = '" + dataSource + "' and org_id = '" + orgId + "'";
        List<Object> objects = wmSoHeaderService.executeSelectSql(getSql);
        if (CollectionUtil.isEmpty(objects) || objects.size() <= 0) {
            return;
        }
        for (Object object : objects) {
            String item = (String) object;
            String[] itemList = item.split(",");
            String id = itemList[0];
            String status = itemList[1];
            if (!WmsCodeMaster.SO_NEW.getCode().equals(status)) {
                throw new OmsException(SystemAliases.WMS.getDesc() + "已进行操作，无法删除");
            }
            String deleteSql = "delete from wm_so_header where id = '" + id + "'";
            String deleteDetailSql = "delete from wm_so_detail where head_id = '" + id + "'";
            wmSoHeaderService.executeDeleteSql(deleteSql);
            wmSoHeaderService.executeDeleteSql(deleteDetailSql);
        }
    }

    @Transactional
    public void pushTaskToInput(OmTaskHeader omTaskHeader) {
        if (!(OmsConstants.OMS_TASK_STATUS_20.equals(omTaskHeader.getStatus()) || OmsConstants.OMS_TASK_STATUS_40.equals(omTaskHeader.getStatus()))) {
            throw new OmsException("[" + omTaskHeader.getTaskNo() + "]不是确认或已下发状态，无法下发");
        }
        this.deleteInputTask(omTaskHeader.getTaskNo(), SystemAliases.OMS.getCode(), omTaskHeader.getWarehouse());

        String orgId = omTaskHeader.getWarehouse();
        this.getCustomer(omTaskHeader.getOwner(), CustomerType.OWNER.getCode(), orgId);
        this.getCustomer(omTaskHeader.getSupplierCode(), CustomerType.SUPPLIER.getCode(), orgId);
        this.getCustomer(omTaskHeader.getCarrier(), CustomerType.CARRIER.getCode(), orgId);
        this.getCustomer(omTaskHeader.getSettlement(), CustomerType.SETTLEMENT.getCode(), orgId);

        // 入库单头
        BanQinWmAsnEntity entity = new BanQinWmAsnEntity();
        entity.setAsnType(omTaskHeader.getPushOrderType());
        entity.setOrderTime(omTaskHeader.getOrderDate());
        entity.setOwnerCode(omTaskHeader.getOwner());
        entity.setSupplierCode(omTaskHeader.getSupplierCode());
        entity.setCarrierCode(omTaskHeader.getCarrier());
        entity.setSettleCode(omTaskHeader.getSettlement());
        entity.setDataSource(SystemAliases.OMS.getCode());
        entity.setOrgId(orgId);

        entity.setFmEta(omTaskHeader.getArrivalTime());
        entity.setToEta(omTaskHeader.getArrivalTime());
        entity.setLogisticNo(omTaskHeader.getLogisticsNo());
        entity.setFreightAmount(omTaskHeader.getFreightCharge() == null ? null : omTaskHeader.getFreightCharge().doubleValue());

        entity.setDef1(omTaskHeader.getBusinessNo());// 商流订单号
        entity.setDef2(omTaskHeader.getChainNo());// 供应链订单号
        entity.setDef3(omTaskHeader.getTaskNo());// 作业任务号
        entity.setDef4(omTaskHeader.getCustomerNo());// 客户订单号
        entity.setDef5(omTaskHeader.getOrderSource());// 订单来源
        entity.setDef10(omTaskHeader.getBusinessOrderType());// 业务订单类型

        entity.setDef14(omTaskHeader.getDef10());   // 客户机构
        entity.setRemarks(omTaskHeader.getRemarks());
        ResultMessage message = wmAsnHeaderService.saveAsnEntity(entity);
        if (!message.isSuccess()) {
            throw new OmsException(message.getMessage());
        }
        BanQinWmAsnHeader wmAsnHeader = (BanQinWmAsnHeader) message.getData();
        // 入库单明细
        List<OmTaskDetail> omTaskDetailList = omTaskHeader.getOmTaskDetailList();
        for (int i = 0; i < omTaskDetailList.size(); i++) {
            OmTaskDetail omTaskDetail = omTaskDetailList.get(i);
            BanQinCdWhSku cdWhSku = this.getSku(omTaskHeader.getOwner(), omTaskDetail.getSkuCode(), orgId);
            OmOrderDetailAppendix orderDetailAppendix = omOrderDetailAppendixService.getByChainNoAndLineNo(omTaskDetail.getChainNo(), omTaskDetail.getLineNo(), omTaskDetail.getOrgId());

            BanQinWmAsnDetailEntity asnDetailEntity = new BanQinWmAsnDetailEntity();
            asnDetailEntity.setHeadId(wmAsnHeader.getId());
            asnDetailEntity.setAsnNo(wmAsnHeader.getAsnNo());
            asnDetailEntity.setLineNo(String.format("%04d", i + 1));
            asnDetailEntity.setStatus(WmsCodeMaster.ASN_NEW.getCode());
            asnDetailEntity.setOwnerCode(omTaskHeader.getOwner());
            asnDetailEntity.setSkuCode(omTaskDetail.getSkuCode());
            asnDetailEntity.setPackCode(cdWhSku.getPackCode());
            asnDetailEntity.setUom(OmsConstants.OMS_PACKAGE_EA);
            asnDetailEntity.setQtyAsnEa(omTaskDetail.getQty().doubleValue());
            asnDetailEntity.setQtyRcvEa(0D);
            asnDetailEntity.setOrgId(orgId);

            asnDetailEntity.setLogisticNo(omTaskHeader.getLogisticsNo());
            asnDetailEntity.setPrice(omTaskDetail.getPrice() == null ? 0D : omTaskDetail.getPrice().doubleValue());
            asnDetailEntity.setReserveCode(cdWhSku.getReserveCode());
            asnDetailEntity.setPaRule(cdWhSku.getPaRule());
            asnDetailEntity.setIsQc(cdWhSku.getIsQc());
            asnDetailEntity.setQcPhase(cdWhSku.getQcPhase());
            asnDetailEntity.setQcRule(cdWhSku.getQcRule());
            asnDetailEntity.setItemGroupCode(cdWhSku.getItemGroupCode());
            // 自定义
            asnDetailEntity.setDef1(omTaskDetail.getChainNo());// 供应链订单号
            asnDetailEntity.setDef2(omTaskDetail.getLineNo());// 供应链订单行号
            asnDetailEntity.setDef3(omTaskHeader.getTaskNo());// 作业任务号
            asnDetailEntity.setDef4(omTaskDetail.getCustomerNo());// 客户订单号
            asnDetailEntity.setDef5(omTaskDetail.getDataSource());// 数据来源
            asnDetailEntity.setDef6(omTaskDetail.getDef1());// 温层
            asnDetailEntity.setDef7(omTaskDetail.getDef2());// 课别
            asnDetailEntity.setDef8(omTaskDetail.getDef3());// 商品品类
            asnDetailEntity.setRemarks(omTaskDetail.getRemarks());
            if (null != orderDetailAppendix) {
                asnDetailEntity.setLotAtt01(orderDetailAppendix.getDefTime1());
                asnDetailEntity.setLotAtt02(orderDetailAppendix.getDefTime2());
                asnDetailEntity.setLotAtt03(orderDetailAppendix.getDefTime3());
                asnDetailEntity.setLotAtt04(orderDetailAppendix.getDef4());
                asnDetailEntity.setLotAtt05(orderDetailAppendix.getDef5());
                asnDetailEntity.setLotAtt06(orderDetailAppendix.getDef6());
                asnDetailEntity.setLotAtt07(orderDetailAppendix.getDef7());
                asnDetailEntity.setLotAtt08(orderDetailAppendix.getDef8());
                asnDetailEntity.setLotAtt09(orderDetailAppendix.getDef9());
                asnDetailEntity.setLotAtt10(orderDetailAppendix.getDef10());
                asnDetailEntity.setLotAtt11(orderDetailAppendix.getDef11());
                asnDetailEntity.setLotAtt12(orderDetailAppendix.getDef12());
                asnDetailEntity.setPlanToLoc(orderDetailAppendix.getDef1());
            }

            ResultMessage rsMessage = wmAsnDetailService.saveAsnDetailEntity(asnDetailEntity);
            if (!rsMessage.isSuccess()) {
                throw new OmsException(rsMessage.getMessage());
            }
        }
    }

    @Transactional
    public void pushTaskToOutput(OmTaskHeader omTaskHeader) {
        if (!(OmsConstants.OMS_TASK_STATUS_30.equals(omTaskHeader.getStatus()) || OmsConstants.OMS_TASK_STATUS_40.equals(omTaskHeader.getStatus()))) {
            throw new OmsException("[" + omTaskHeader.getTaskNo() + "]不是已分配或已下发状态，无法下发");
        }
        this.deleteOutputTask(omTaskHeader.getTaskNo(), SystemAliases.OMS.getCode(), omTaskHeader.getWarehouse());

        String orgId = omTaskHeader.getWarehouse();
        this.getCustomer(omTaskHeader.getOwner(), CustomerType.OWNER.getCode(), orgId);
        BanQinEbCustomer consignee = this.getCustomer(StringUtils.isBlank(omTaskHeader.getConsigneeCode()) ? omTaskHeader.getCustomer() : omTaskHeader.getConsigneeCode(), CustomerType.CONSIGNEE.getCode(), orgId);
        this.getCustomer(omTaskHeader.getSupplierCode(), CustomerType.SUPPLIER.getCode(), orgId);
        this.getCustomer(omTaskHeader.getCarrier(), CustomerType.CARRIER.getCode(), orgId);
        this.getCustomer(omTaskHeader.getSettlement(), CustomerType.SETTLEMENT.getCode(), orgId);

        BanQinWmSoEntity entity = new BanQinWmSoEntity();
        entity.setSoType(omTaskHeader.getPushOrderType());
        entity.setOrderTime(omTaskHeader.getOrderDate());
        entity.setOwnerCode(omTaskHeader.getOwner());
        entity.setCarrierCode(omTaskHeader.getCarrier());
        entity.setSettleCode(omTaskHeader.getSettlement());
        entity.setConsigneeCode(consignee.getEbcuCustomerNo());
        entity.setConsigneeName(consignee.getEbcuNameCn());
        entity.setConsigneeTel(consignee.getEbcuTel());
        entity.setConsigneeAddr(consignee.getEbcuAddress());

        entity.setLogisticNo(omTaskHeader.getLogisticsNo());
        entity.setVehicleNo(omTaskHeader.getVehicleNo());
        entity.setDriver(omTaskHeader.getDriver());
        entity.setDriverTel(omTaskHeader.getContactTel());
        entity.setContactName(omTaskHeader.getConsignee());
        entity.setContactTel(omTaskHeader.getConsigneeTel());
        entity.setContactAddr(omTaskHeader.getConsigneeAddress());
        entity.setDataSource(SystemAliases.OMS.getCode());
        entity.setOrgId(orgId);

        entity.setDef1(omTaskHeader.getBusinessNo());// 商流订单号
        entity.setDef2(omTaskHeader.getChainNo());// 供应链订单号
        entity.setDef3(omTaskHeader.getTaskNo());// 作业任务号
        entity.setDef4(omTaskHeader.getOrderSource());// 订单来源
        entity.setDef5(omTaskHeader.getCustomerNo());// 客户订单号
        entity.setDef6(omTaskHeader.getChannel());  // 渠道
        entity.setDef7(omTaskHeader.getServiceMode());  // 服务模式
        entity.setDef8(omTaskHeader.getDef3());         // 客户大类
        entity.setDef10(omTaskHeader.getDef4());        // 城市
        entity.setDef11(omTaskHeader.getDef5());        // 客户行业类型
        entity.setDef12(omTaskHeader.getDef6());        // 客户范围
        entity.setDef13(omTaskHeader.getBusinessOrderType());   // 业务订单类型
        entity.setDef14(omTaskHeader.getDef10());        // 客户机构
        entity.setDef17(omTaskHeader.getConsigneeAddressArea());// 收货人区域地址
        entity.setRemarks(omTaskHeader.getRemarks());
        ResultMessage message = outboundSoService.saveSoEntity(entity);
        if (!message.isSuccess()) {
            throw new OmsException(message.getMessage());
        }
        BanQinWmSoHeader wmSoHeader = (BanQinWmSoHeader) message.getData();

        List<OmTaskDetail> omTaskDetailList = omTaskHeader.getOmTaskDetailList();
        for (int i = 0; i < omTaskDetailList.size(); i++) {
            OmTaskDetail omTaskDetail = omTaskDetailList.get(i);
            BanQinCdWhSku banQinCdWhSku = this.getSku(omTaskHeader.getOwner(), omTaskDetail.getSkuCode(), orgId);
            OmOrderDetailAppendix orderDetailAppendix = omOrderDetailAppendixService.getByChainNoAndLineNo(omTaskDetail.getChainNo(), omTaskDetail.getLineNo(), omTaskDetail.getOrgId());

            BanQinWmSoDetailEntity soDetailEntity = new BanQinWmSoDetailEntity();
            soDetailEntity.setHeadId(wmSoHeader.getId());
            soDetailEntity.setSoNo(wmSoHeader.getSoNo());
            soDetailEntity.setLineNo(String.format("%04d", i + 1));
            soDetailEntity.setStatus(WmsCodeMaster.SO_NEW.getCode());
            soDetailEntity.setOwnerCode(omTaskHeader.getOwner());
            soDetailEntity.setSkuCode(omTaskDetail.getSkuCode());
            soDetailEntity.setPackCode(banQinCdWhSku.getPackCode());
            soDetailEntity.setOrgId(orgId);

            soDetailEntity.setUom(OmsConstants.OMS_PACKAGE_EA);
            soDetailEntity.setQtySoEa(omTaskDetail.getQty().doubleValue());

            soDetailEntity.setLogisticNo(omTaskHeader.getLogisticsNo());
            soDetailEntity.setRotationRule(banQinCdWhSku.getRotationRule());
            soDetailEntity.setAllocRule(banQinCdWhSku.getAllocRule());
            soDetailEntity.setPrice(omTaskDetail.getPrice() == null ? 0D : omTaskDetail.getPrice().doubleValue());

            soDetailEntity.setDef1(omTaskDetail.getChainNo());// 供应链订单号
            soDetailEntity.setDef2(omTaskDetail.getLineNo());// 供应链订单行号
            soDetailEntity.setDef3(omTaskHeader.getTaskNo());// 作业任务号
            soDetailEntity.setDef4(omTaskDetail.getCustomerNo());// 客户订单号
            soDetailEntity.setDef5(omTaskDetail.getDataSource());// 数据来源
            soDetailEntity.setDef6(omTaskDetail.getDef1());// 温层
            soDetailEntity.setDef7(omTaskDetail.getDef2());// 课别
            soDetailEntity.setDef8(omTaskDetail.getDef3());// 商品品类
            soDetailEntity.setRemarks(omTaskDetail.getRemarks());
            if (null != orderDetailAppendix) {
                soDetailEntity.setLotAtt01(orderDetailAppendix.getDefTime1());
                soDetailEntity.setLotAtt02(orderDetailAppendix.getDefTime2());
                soDetailEntity.setLotAtt03(orderDetailAppendix.getDefTime3());
                soDetailEntity.setLotAtt04(orderDetailAppendix.getDef4());
                soDetailEntity.setLotAtt05(orderDetailAppendix.getDef5());
                soDetailEntity.setLotAtt06(orderDetailAppendix.getDef6());
                soDetailEntity.setLotAtt07(orderDetailAppendix.getDef7());
                soDetailEntity.setLotAtt08(orderDetailAppendix.getDef8());
                soDetailEntity.setLotAtt09(orderDetailAppendix.getDef9());
                soDetailEntity.setLotAtt10(orderDetailAppendix.getDef10());
                soDetailEntity.setLotAtt11(orderDetailAppendix.getDef11());
                soDetailEntity.setLotAtt12(orderDetailAppendix.getDef12());
            }
            ResultMessage rsMessage = outboundSoService.saveSoDetailEntity(soDetailEntity);
            if (!rsMessage.isSuccess()) {
                throw new OmsException(rsMessage.getMessage());
            }
        }
    }
}
