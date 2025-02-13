package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.BanQinWmTaskKit;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmDelAllocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述：加工管理- 子件取消拣货 
 * <p>
 * create by Jianhua on 2019/8/23
 */
@Service
public class BanQinKitCancelPickingService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinWmKitSubDetailService wmKitSubDetailManager;
    @Autowired
    protected BanQinWmTaskKitService wmTaskKitManager;
    @Autowired
    protected BanQinWmDelAllocService wmDelAllocManager;
    @Autowired
    protected BanQinInventoryService inventoryServiceManager;
    @Autowired
    protected BanQinKitCreateTaskPaService kitCreateTaskPa;

    /**
     * 描述：取消拣货
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void kitCancelPicking(BanQinWmTaskKitEntity entity) throws WarehouseException {
        BanQinWmTaskKit model = wmTaskKitManager.getByKitTaskId(entity.getKitTaskId(), entity.getOrgId());
        // 1、校验状态,是否是完全拣货状态60
        if (!model.getStatus().equals(WmsCodeMaster.SUB_KIT_FULL_PICKING.getCode())) {
            throw new WarehouseException(entity.getKitTaskId() + "不是完全拣货状态");
        }
        // 2、包装单位换算数量
        BanQinCdWhPackageRelationEntity packageEntity = wmCommon.getPackageRelationAndQtyUom(entity.getPackCode(), entity.getUom(), entity.getQtyEa(), entity.getOrgId());
        // 3、写入取消分配拣货记录表，状态完全拣货60
        // 设置单号，子件明细，订单类型
        this.wmDelAllocManager.saveDelAllocByTaskKit(model);
        // 4、删除分配明细
        this.wmTaskKitManager.delete(model);

        // 6、更新库存、交易(操作数量=实际拣货数量)
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.CANCEL_PICKING);// ACTION_CODE 取消拣货
        invEntity.setOwnerCode(entity.getOwnerCode());// 货主
        invEntity.setSkuCode(entity.getSubSkuCode());// 商品
        invEntity.setLotNum(entity.getLotNum());// 批次号
        invEntity.setLocCode(entity.getToLoc());// 拣货库位
        invEntity.setTraceId(entity.getToId());// 目标拣货位跟踪号
        invEntity.setQtyEaOp(entity.getQtyEa());// 取消拣货数量 = 分配数量
        invEntity.setOrderNo(entity.getKitNo());// 加工单号
        invEntity.setLineNo(entity.getSubLineNo());// 加工单子件行号
        invEntity.setTaskId(entity.getKitTaskId());// 任务号
        invEntity.setPackCode(entity.getPackCode());// 包装代码
        invEntity.setUom(entity.getUom());// 包装单位
        invEntity.setQtyUom(packageEntity.getCdprQuantity().doubleValue());// 包装单位换算数量
        invEntity.setOrgId(entity.getOrgId());
        inventoryServiceManager.updateInventory(invEntity);

        // 7、更新出库单明细、波次单明细、出库单状态、波次单状态
        BanQinWmKitSubDetail subModel = this.wmKitSubDetailManager.getByKitNoAndSubLineNo(entity.getKitNo(), entity.getSubLineNo(), entity.getOrgId());
        if (subModel != null) {
            this.wmKitSubDetailManager.updateStatus(ActionCode.CANCEL_PICKING, entity.getQtyEa(), subModel);
        }
        // 是否生成上架任务
        if (StringUtils.isNotEmpty(entity.getIsTaskPa()) && entity.getIsTaskPa().equals(WmsConstants.YES)) {
            // 生成上架任务
            kitCreateTaskPa.kitCreateTaskPaByAlloc(entity);
        }
    }

}
