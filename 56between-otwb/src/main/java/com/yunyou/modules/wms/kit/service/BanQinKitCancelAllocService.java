package com.yunyou.modules.wms.kit.service;

import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.BanQinInventoryEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.BanQinWmTaskKit;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmDelAllocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述：加工管理-子件取消分配
 * <p>
 * create by Jianhua on 2019/8/23
 */
@Service
public class BanQinKitCancelAllocService {
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinWmTaskKitService banQinWmTaskKitService;
    @Autowired
    protected BanQinWmDelAllocService banQinWmDelAllocService;
    @Autowired
    protected BanQinInventoryService banQinInventoryService;

    /**
     * Description :加工取消分配
     *
     * @param entity
     * @Author: Morice
     * @Create Date: 2014-12-25
     */
    @Transactional
    public void kitCancelAlloc(BanQinWmTaskKitEntity entity) throws WarehouseException {
        // 获取分配明细
        BanQinWmTaskKit model = banQinWmTaskKitService.getByKitTaskId(entity.getKitTaskId(), entity.getOrgId());
        // 1、校验状态，是否是完全分配
        if (!model.getStatus().equals(WmsCodeMaster.SUB_KIT_FULL_ALLOC.getCode())) {
            // 不是完全分配状态
            throw new WarehouseException(entity.getKitTaskId() + "不是完全分配状态，不能操作");
        }

        // 2、写入取消分配拣货记录表，状态完全分配(40)
        // 设置单号，子件明细，订单类型
        this.banQinWmDelAllocService.saveDelAllocByTaskKit(model);
        // 3、删除加工任务
        this.banQinWmTaskKitService.delete(model);

        // 5、更新库存、交易(操作数量=实际拣货数量)
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.CANCEL_ALLOCATION);// ACTION_CODE 取消分配
        invEntity.setOwnerCode(entity.getOwnerCode());// 货主
        invEntity.setSkuCode(entity.getSubSkuCode());// 商品
        invEntity.setLotNum(entity.getLotNum());// 批次号
        invEntity.setLocCode(entity.getLocCode());// 分配库位
        invEntity.setTraceId(entity.getTraceId());// 分配库位跟踪号
        invEntity.setQtyEaOp(entity.getQtyEa());// 分配数量
        invEntity.setOrgId(entity.getOrgId());

        banQinInventoryService.updateInventory(invEntity);

        // 6、更新子件状态
        BanQinWmKitSubDetail subModel = this.banQinWmKitSubDetailService.getByKitNoAndSubLineNo(entity.getKitNo(), entity.getSubLineNo(), entity.getOrgId());
        if (subModel != null) {
            this.banQinWmKitSubDetailService.updateStatus(ActionCode.CANCEL_ALLOCATION, entity.getQtyEa(), subModel);
        }
    }

}
