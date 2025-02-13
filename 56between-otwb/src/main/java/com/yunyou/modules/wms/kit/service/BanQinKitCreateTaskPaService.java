package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPaOperationService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述：加工管理-生成上架任务
 * <p>
 * create by Jianhua on 2019/8/23
 */
@Service
public class BanQinKitCreateTaskPaService {
    @Autowired
    protected BanQinInventoryService inventoryServiceManager;
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinWmTaskPaService banQinWmTaskPaService;
    @Autowired
    protected BanQinInboundPaOperationService banQinInboundPaOperationService;

    /**
     * 描述：加工任务 取消拣货生成上架任务
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public ResultMessage kitCreateTaskPaByAlloc(BanQinWmTaskKitEntity taskKitEntity) throws WarehouseException {
        // 生成上架任务
        BanQinWmTaskPaEntity taskPaEntity = new BanQinWmTaskPaEntity();
        taskPaEntity.setOrderNo(taskKitEntity.getKitNo());
        taskPaEntity.setOrderType(WmsCodeMaster.ORDER_KIT.getCode());
        taskPaEntity.setOwnerCode(taskKitEntity.getOwnerCode());
        taskPaEntity.setSkuCode(taskKitEntity.getSubSkuCode());
        taskPaEntity.setLotNum(taskKitEntity.getLotNum());
        taskPaEntity.setFromLoc(taskKitEntity.getToLoc()); // 目标库位
        taskPaEntity.setFromId(taskKitEntity.getToId()); // 目标跟踪号
        taskPaEntity.setQtyEa(taskKitEntity.getQtyEa());
        taskPaEntity.setCubic(taskKitEntity.getCubic()); // 商品单体积
        taskPaEntity.setNetWeight(taskKitEntity.getNetWeight());// 商品单净重
        taskPaEntity.setGrossWeight(taskKitEntity.getGrossWeight());// 商品单毛重
        taskPaEntity.setPackCode(taskKitEntity.getPackCode());
        taskPaEntity.setUom(taskKitEntity.getUom());
        taskPaEntity.setNewPaRule(taskKitEntity.getPaRule()); // 指定上架规则
        taskPaEntity.setNewReserveCode(null);// 上架库位指定规则
        if (null != taskKitEntity.getIsAllocLoc() && taskKitEntity.getIsAllocLoc().equals(WmsConstants.YES)) {
            taskPaEntity.setPlanPaLoc(taskKitEntity.getLocCode());// 计划上架库位 =
            // 分配库位
        } else {
            taskPaEntity.setPlanPaLoc(null);// 计划上架库位 = 分配库位
        }
        taskPaEntity.setPaIdRcv(null);// 上架任务ID
        taskPaEntity.setOrgId(taskKitEntity.getOrgId());
        return this.kitCreateTaskPa(taskPaEntity);
    }

    /**
     * 描述：加工 生成上架任务
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public ResultMessage kitCreateTaskPa(BanQinWmTaskPaEntity taskPaEntity) throws WarehouseException {
        // 初始化参数
        String orderType = taskPaEntity.getOrderType();
        // 单据号
        String orderNo = taskPaEntity.getOrderNo();
        // 货主
        String ownerCode = taskPaEntity.getOwnerCode();
        // 商品
        String skuCode = taskPaEntity.getSkuCode();
        // 批次号
        String lotnum = taskPaEntity.getLotNum();
        // 收货库位
        String fromLoc = taskPaEntity.getFromLoc();
        // 收货traceID
        String fromId = taskPaEntity.getFromId();
        // 单位(默认EA)
        String uom = taskPaEntity.getUom();
        // 包装
        String packCode = taskPaEntity.getPackCode();
        // 实际生成上架任务数
        Double qtyPa = taskPaEntity.getQtyEa();
        Double qtyPaUom = 0D;
        // 推荐库位
        String planToLoc = "";
        // 毛重
        Double grossWeight = taskPaEntity.getGrossWeight() == null ? 0D : taskPaEntity.getGrossWeight();
        // 体积
        Double cubic = taskPaEntity.getCubic() == null ? 0D : taskPaEntity.getCubic();
        // 机构
        String orgId = taskPaEntity.getOrgId();

        // 实例化查询库存参数
        BanQinInventoryEntity checkEntity = new BanQinInventoryEntity();
        checkEntity.setOwnerCode(ownerCode);
        checkEntity.setSkuCode(skuCode);
        checkEntity.setLotNum(lotnum);
        checkEntity.setTraceId(fromId);
        checkEntity.setLocCode(fromLoc);
        checkEntity.setOrgId(orgId);
        // 根据owner,sku,loc,lotnum,traceId查询wm_inv_lot_loc中可用库存，如果不存在可用库存，则不能生成上架任务。
        // 库位：理货站，目标跟踪号
        Double qtyAvailable = this.inventoryServiceManager.getAvailableQty(checkEntity);
        if (qtyAvailable == 0D) {
            throw new WarehouseException("有效库存不足,批次[" + lotnum + "]库位[" + fromLoc + "]跟踪号[" + fromId + "],不能操作");
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
            entity.setLotNum(lotnum);
            entity.setOrderType(orderType);
            entity.setPackCode(packCode);
            entity.setPaRule(taskPaEntity.getNewPaRule());
            entity.setUom(uom);
            entity.setQtyPa(qtyPa);
            entity.setQtyPaUom(qtyPaUom);
            entity.setGrossWeight(grossWeight * qtyPa);
            entity.setCubic(cubic * qtyPa);
            entity.setOrgId(orgId);
            planToLoc = banQinInboundPaOperationService.putawayCalculation(entity);
        }
        // 实例化上架更新库存参数
        BanQinInventoryEntity fmEntity = new BanQinInventoryEntity();
        fmEntity.setAction(ActionCode.CREATE_PA_TASK);
        fmEntity.setOrderNo(orderNo);
        fmEntity.setOwnerCode(ownerCode);
        fmEntity.setSkuCode(skuCode);
        fmEntity.setPackCode(packCode);
        fmEntity.setUom(uom);
        fmEntity.setLotNum(lotnum);
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
        toEntity.setLotNum(lotnum);
        toEntity.setTraceId(fromId);
        toEntity.setLocCode(planToLoc);
        toEntity.setPlanLocCode(planToLoc);
        toEntity.setQtyUom(qtyPaUom);
        toEntity.setQtyEaOp(qtyPa);
        toEntity.setOrgId(orgId);

        // 执行上架更新，更新收货库位待出数，计划库位待入数
        inventoryServiceManager.updateInventory(fmEntity, toEntity);

        // 生成上架任务
        BanQinWmTaskPa wmTaskPaModel = banQinWmTaskPaService.saveTaskPa(taskPaEntity, planToLoc, qtyPa, uom, qtyPaUom);
        ResultMessage msg = new ResultMessage();
        msg.setData(wmTaskPaModel);
        msg.setSuccess(true);
        return msg;
    }

}
