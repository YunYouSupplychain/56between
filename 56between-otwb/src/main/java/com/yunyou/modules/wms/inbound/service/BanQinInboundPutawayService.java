package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotAttService;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcEntity;
import com.yunyou.modules.wms.qc.service.BanQinWmQcHeaderService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 上架确认
 * @author WMJ
 * @version 2019/07/09
 */
@Service
@Transactional(readOnly = true)
public class BanQinInboundPutawayService {
    @Autowired
    private BanQinWmTaskPaService wmTaskPaService;
    @Autowired
    private BanQinInventoryService inventoryService;
    @Autowired
    private BanQinWmAsnHeaderService wmAsnHeaderService;
    @Autowired
    private BanQinWmInvLotAttService wmInvLotAttService;
    @Autowired
    private BanQinWmQcHeaderService wmQcHeaderService;
    @Autowired
    private BanQinCdWhLocService cdWhLocService;
    @Autowired
    private BanQinWmsCommonService wmsCommonService;

    /**
     * 上架确认
     */
    @Transactional
    public void inboundPutaway(BanQinWmTaskPaEntity wmTaskPaEntity) {
        /*
         * 上架确认前校验
         * 1.校验上架任务状态，判断是否为新建
         * 2.校验目标数量不能超过任务数量
         * 3.上架库位不能和收货库位相同
         * 4.如果是部分上架时，一定需要上到不同的TraceID
         * 5.校验是否允许商品混放、是否允许批次混放
         */
        checkBeforePutaway(wmTaskPaEntity);
        // 实例化上架更新库存参数
        BanQinInventoryEntity fmEntity = new BanQinInventoryEntity();
        fmEntity.setAction(ActionCode.PUTAWAY);
        fmEntity.setTaskId(wmTaskPaEntity.getPaId());
        fmEntity.setOrderNo(wmTaskPaEntity.getOrderNo());
        fmEntity.setOwnerCode(wmTaskPaEntity.getOwnerCode());
        fmEntity.setSkuCode(wmTaskPaEntity.getSkuCode());
        fmEntity.setPackCode(wmTaskPaEntity.getPackCode());
        fmEntity.setUom(wmTaskPaEntity.getUom());
        fmEntity.setLotNum(wmTaskPaEntity.getLotNum());
        fmEntity.setTraceId(wmTaskPaEntity.getFmId());
        fmEntity.setLocCode(wmTaskPaEntity.getFmLoc());
        fmEntity.setQtyUom(wmTaskPaEntity.getQtyPaUom());
        fmEntity.setQtyEaOp(wmTaskPaEntity.getCurrentPaQtyEa());
        fmEntity.setOrgId(wmTaskPaEntity.getOrgId());

        BanQinInventoryEntity toEntity = new BanQinInventoryEntity();
        toEntity.setAction(ActionCode.PUTAWAY);
        toEntity.setTaskId(wmTaskPaEntity.getPaId());
        toEntity.setOrderNo(wmTaskPaEntity.getOrderNo());
        toEntity.setOwnerCode(wmTaskPaEntity.getOwnerCode());
        toEntity.setSkuCode(wmTaskPaEntity.getSkuCode());
        toEntity.setPackCode(wmTaskPaEntity.getPackCode());
        toEntity.setUom(wmTaskPaEntity.getUom());
        toEntity.setLotNum(wmTaskPaEntity.getLotNum());
        toEntity.setTraceId(wmTaskPaEntity.getToId());
        toEntity.setLocCode(wmTaskPaEntity.getToLoc());
        toEntity.setQtyUom(wmTaskPaEntity.getQtyPaUom());
        toEntity.setQtyEaOp(wmTaskPaEntity.getCurrentPaQtyEa());
        toEntity.setPlanLocCode(wmTaskPaEntity.getSuggestLoc());
        toEntity.setPlanTraceId(wmTaskPaEntity.getFmId());
        toEntity.setOrgId(wmTaskPaEntity.getOrgId());

        // 执行上架更新，更新收货库位待出数，计划库位待入数
        BanQinInventoryEntity updatedInv = inventoryService.updateInventory(fmEntity, toEntity);
        // 库位为忽略ID时，更新完库存，跟踪号置*
        BanQinWmTaskPa wmTaskPaModel = new BanQinWmTaskPa();
        BeanUtils.copyProperties(wmTaskPaEntity, wmTaskPaModel);
        wmTaskPaModel.setToId(updatedInv.getTraceId());
        Double currentPaQtyEa = wmTaskPaEntity.getCurrentPaQtyEa();
        Double qtyPaEa = wmTaskPaEntity.getQtyPaEa();
        // 更新上架任务记录
        wmTaskPaModel.setQtyPaEa(currentPaQtyEa);
        wmTaskPaModel.setStatus(WmsCodeMaster.TSK_COMPLETE.getCode());
        if (null == wmTaskPaModel.getPaTime()) {
            wmTaskPaModel.setPaTime(new Date());
        }
        this.wmTaskPaService.save(wmTaskPaModel);
        // 部分上架时，新增一个上架任务
        if (currentPaQtyEa < qtyPaEa) {
            Integer cdprQuantity = wmsCommonService.getPackageRelationAndQtyUom(wmTaskPaModel.getPackCode(), wmTaskPaModel.getUom(), wmTaskPaModel.getOrgId()).getCdprQuantity();
            Double newQtyPaEa = qtyPaEa - currentPaQtyEa;
            cdprQuantity = cdprQuantity == null || cdprQuantity == 0 ? 1 : cdprQuantity;

            BanQinWmTaskPa model = new BanQinWmTaskPa();
            BeanUtils.copyProperties(wmTaskPaModel, model);
            model.setId(IdGen.uuid());
            model.setIsNewRecord(true);
            model.setStatus(WmsCodeMaster.TSK_NEW.getCode());
            model.setLineNo(wmTaskPaService.getNewPaLineNo(wmTaskPaEntity.getPaId(), wmTaskPaEntity.getOrgId()));
            model.setToId(wmTaskPaModel.getFmId());
            model.setToLoc(wmTaskPaModel.getSuggestLoc());
            model.setQtyPaEa(newQtyPaEa);
            model.setQtyPaUom(Math.ceil(newQtyPaEa / cdprQuantity));
            model.setPaOp(null);
            model.setPaTime(null);
            model.setPrintNum(0);
            wmTaskPaService.save(model);
        }
    }

    /**
     * 上架确认前校验
     */
    private void checkBeforePutaway(BanQinWmTaskPaEntity wmTaskPaEntity) {
        BanQinWmTaskPa queryPaModel = wmTaskPaService.getByPaIdAndLineNo(wmTaskPaEntity.getPaId(), wmTaskPaEntity.getLineNo(), wmTaskPaEntity.getOrgId());
        // 校验上架任务状态，判断是否为新建
        if (null == queryPaModel) {
            throw new WarehouseException("查询结果为空");
        } else {
            if (WmsCodeMaster.TSK_COMPLETE.getCode().equals(queryPaModel.getStatus())) {
                throw new WarehouseException("已上架，不能操作");
            }
        }
        String asnNo = "";
        if (WmsCodeMaster.ORDER_QC.getCode().equals(wmTaskPaEntity.getOrderType())) {
            BanQinWmQcEntity wmQcEntity = wmQcHeaderService.findEntityByQcNo(wmTaskPaEntity.getOrderNo(), wmTaskPaEntity.getOrgId());
            asnNo = wmQcEntity.getOrderNo();
        }
        if (WmsCodeMaster.ORDER_ASN.getCode().equals(wmTaskPaEntity.getOrderType())) {
            asnNo = wmTaskPaEntity.getOrderNo();
        }
        if (StringUtils.isNotEmpty(asnNo)) {
            BanQinWmAsnHeader wmAsnHeaderModel = wmAsnHeaderService.getByAsnNo(asnNo, wmTaskPaEntity.getOrgId());
            if (null != wmAsnHeaderModel && WmsCodeMaster.ODHL_HOLD.getCode().equals(wmAsnHeaderModel.getHoldStatus())) {
                throw new WarehouseException("订单已冻结，不能操作");
            }
        }
        // 校验目标数量不能超过任务数量
        if (null == wmTaskPaEntity.getCurrentPaQtyEa() || wmTaskPaEntity.getCurrentPaQtyEa() == 0D) {
            throw new WarehouseException("上架数不能为0");
        }
        // 实际页面收集到的上架数>数据库查询出的计划上架数
        if (wmTaskPaEntity.getCurrentPaQtyEa() > queryPaModel.getQtyPaEa()) {
            throw new WarehouseException("不能超量上架");
        }
        // 上架库位不能和收货库位相同
        if (StringUtils.isEmpty(wmTaskPaEntity.getToLoc())) {
            throw new WarehouseException("上架库位不能为空");
        }
        if (wmTaskPaEntity.getToLoc().equals(wmTaskPaEntity.getFmLoc())) {
            throw new WarehouseException("上架库位不能和收货库位相同");
        }
        // 如果是部分上架并且库位不忽略跟踪号，一定需要上到不同的TraceID
        if (StringUtils.isEmpty(wmTaskPaEntity.getToId())) {
            throw new WarehouseException("上架跟踪号不能为空");
        }
        BanQinCdWhLoc locModel = cdWhLocService.findByLocCode(wmTaskPaEntity.getToLoc(), wmTaskPaEntity.getOrgId());
        if (locModel == null) {
            throw new WarehouseException(wmTaskPaEntity.getToLoc() + "上架库位不存在");
        }
        if (WmsConstants.NO.equals(locModel.getIsLoseId()) && !WmsConstants.TRACE_ID.equals(wmTaskPaEntity.getToId()) && wmTaskPaEntity.getCurrentPaQtyEa() < queryPaModel.getQtyPaEa()
                && wmTaskPaEntity.getFmId().equals(wmTaskPaEntity.getToId())) {
            throw new WarehouseException("请选择新的跟踪号");
        }
        // 如果批次是待检，不能操作。
        BanQinWmInvLotAtt wmInvLotAttModel = wmInvLotAttService.getByLotNum(wmTaskPaEntity.getLotNum(), wmTaskPaEntity.getOrgId());
        if (WmsCodeMaster.QC_ATT_TO_QC.getCode().equals(wmInvLotAttModel.getLotAtt04())) {
            throw new WarehouseException("商品需要质检，不能操作");
        }
    }
}
