package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPaOperationService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.task.entity.*;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 生成上架任务 --- 取消拣货
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundCreateTaskPaService {
    @Autowired
    protected BanQinInventoryService inventoryService;
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinWmTaskPaService wmTaskPaService;
    @Autowired
    protected BanQinInboundPaOperationService inboundPaOperationService;

    /**
     * 取消拣货生成上架任务
     * @param wmSoAllocEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundCreateTaskPaByAlloc(BanQinWmSoAllocEntity wmSoAllocEntity) throws WarehouseException {
        // 生成上架任务
        BanQinWmTaskPaEntity taskPaEntity = new BanQinWmTaskPaEntity();
        taskPaEntity.setOrderNo(wmSoAllocEntity.getSoNo());
        taskPaEntity.setOrderType(WmsCodeMaster.ORDER_SO.getCode());
        taskPaEntity.setOwnerCode(wmSoAllocEntity.getOwnerCode());
        taskPaEntity.setSkuCode(wmSoAllocEntity.getSkuCode());
        taskPaEntity.setLotNum(wmSoAllocEntity.getLotNum());
        taskPaEntity.setFromLoc(wmSoAllocEntity.getToLoc()); // 目标库位
        taskPaEntity.setFromId(wmSoAllocEntity.getToId()); // 目标跟踪号
        taskPaEntity.setQtyEa(wmSoAllocEntity.getQtyEa());
        taskPaEntity.setCubic(wmSoAllocEntity.getCubic()); // 商品单体积
        taskPaEntity.setNetWeight(wmSoAllocEntity.getNetWeight());// 商品单净重
        taskPaEntity.setGrossWeight(wmSoAllocEntity.getGrossWeight());// 商品单毛重
        taskPaEntity.setPackCode(wmSoAllocEntity.getPackCode());
        taskPaEntity.setUom(wmSoAllocEntity.getUom());
        taskPaEntity.setNewPaRule(wmSoAllocEntity.getPaRule()); // 指定上架规则
        taskPaEntity.setNewReserveCode(null);// 上架库位指定规则
        taskPaEntity.setOrgId(wmSoAllocEntity.getOrgId());
        if (StringUtils.isNotEmpty(wmSoAllocEntity.getIsAllocLoc()) && wmSoAllocEntity.getIsAllocLoc().equals(WmsConstants.YES)) {
            taskPaEntity.setPlanPaLoc(wmSoAllocEntity.getLocCode());// 计划上架库位 =
        } else {
            taskPaEntity.setPlanPaLoc(null);// 计划上架库位 = 分配库位
        }
        taskPaEntity.setPaIdRcv(null);// 上架任务ID
        return this.outboundCreateTaskPa(taskPaEntity);
    }

    /**
     * 出库 生成上架任务
     * @param taskPaEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundCreateTaskPa(BanQinWmTaskPaEntity taskPaEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        String orderType = taskPaEntity.getOrderType();
        String orderNo = taskPaEntity.getOrderNo();
        String ownerCode = taskPaEntity.getOwnerCode();
        String skuCode = taskPaEntity.getSkuCode();
        String lotNum = taskPaEntity.getLotNum();
        String fromLoc = taskPaEntity.getFromLoc();
        String fromId = taskPaEntity.getFromId();
        String uom = taskPaEntity.getUom();
        String packCode = taskPaEntity.getPackCode();
        Double qtyPa = taskPaEntity.getQtyEa();
        Double qtyPaUom = 0d;
        String planToLoc = "";
        Double grossWeight = taskPaEntity.getGrossWeight() == null ? 0D : taskPaEntity.getGrossWeight();
        Double cubic = taskPaEntity.getCubic() == null ? 0D : taskPaEntity.getCubic();
        String orgId = taskPaEntity.getOrgId();

        // 实例化查询库存参数
        BanQinInventoryEntity checkEntity = new BanQinInventoryEntity();
        checkEntity.setOwnerCode(ownerCode);
        checkEntity.setSkuCode(skuCode);
        checkEntity.setLotNum(lotNum);
        checkEntity.setTraceId(fromId);
        checkEntity.setLocCode(fromLoc);
        checkEntity.setOrgId(orgId);
        // 根据owner,sku,loc,lotnum,traceId查询wm_inv_lot_loc中可用库存，如果不存在可用库存，则不能生成上架任务。
        // 库位：理货站，目标跟踪号
        Double qtyAvailable = this.inventoryService.getAvailableQty(checkEntity);
        if (qtyAvailable == 0D) {
            // 有效库存不足,批次{0} 库位{1} 跟踪号{2}，不能操作
            throw new WarehouseException("有效库存不足", lotNum, fromLoc, fromId);
        }
        // 单位数
        BanQinCdWhPackageRelationEntity packReEntity = wmCommon.getPackageRelationAndQtyUom(packCode, uom, qtyPa, orgId);
        qtyPaUom = packReEntity.getQtyUom();
        // 生成上架任务，推荐库位=计划上架库位=分配库位
        if (StringUtils.isNotEmpty(taskPaEntity.getPlanPaLoc())) {
            planToLoc = taskPaEntity.getPlanPaLoc();
        } else {
            // 计算上架库位
            BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
            entity.setOwnerCode(ownerCode);
            entity.setSkuCode(skuCode);
            entity.setFromLoc(fromLoc);
            entity.setFromId(fromId);
            entity.setLotNum(lotNum);
            entity.setOrderType(orderType);
            entity.setPackCode(packCode);
            entity.setPaRule(taskPaEntity.getNewPaRule());
            entity.setUom(uom);
            entity.setQtyPa(qtyPa);
            entity.setQtyPaUom(qtyPaUom);
            entity.setGrossWeight(grossWeight * qtyPa);
            entity.setCubic(cubic * qtyPa);
            entity.setOrgId(orgId);
            planToLoc = inboundPaOperationService.putawayCalculation(entity);
        }
        // 实例化上架更新库存参数
        BanQinInventoryEntity fmEntity = new BanQinInventoryEntity();
        fmEntity.setAction(ActionCode.CREATE_PA_TASK);
        fmEntity.setOrderNo(orderNo);
        fmEntity.setOwnerCode(ownerCode);
        fmEntity.setSkuCode(skuCode);
        fmEntity.setPackCode(packCode);
        fmEntity.setUom(uom);
        fmEntity.setLotNum(lotNum);
        fmEntity.setTraceId(fromId);
        fmEntity.setLocCode(fromLoc);
        fmEntity.setQtyUom(qtyPaUom);
        fmEntity.setQtyEaOp(qtyPa);
        fmEntity.setOrgId(orgId);

        BanQinInventoryEntity toEntity = new BanQinInventoryEntity();
        toEntity.setAction(ActionCode.CREATE_PA_TASK);
        toEntity.setOrderNo(orderNo);
        toEntity.setOwnerCode(ownerCode);
        toEntity.setSkuCode(skuCode);
        toEntity.setPackCode(packCode);
        toEntity.setUom(uom);
        toEntity.setLotNum(lotNum);
        toEntity.setTraceId(fromId);
        toEntity.setLocCode(planToLoc);
        toEntity.setPlanLocCode(planToLoc);
        toEntity.setQtyUom(qtyPaUom);
        toEntity.setQtyEaOp(qtyPa);
        toEntity.setOrgId(orgId);

        // 执行上架更新，更新收货库位待出数，计划库位待入数
        inventoryService.updateInventory(fmEntity, toEntity);
        // 生成上架任务
        BanQinWmTaskPa wmTaskPaModel = wmTaskPaService.saveTaskPa(taskPaEntity, planToLoc, qtyPa, uom, qtyPaUom);
        // 构造返回的参数
        BanQinTaskPaReturnParam returnParam = new BanQinTaskPaReturnParam();
        returnParam.setWmTaskPaModel(wmTaskPaModel);
        msg.setData(returnParam);
        msg.setSuccess(true);
        return msg;
    }

}
