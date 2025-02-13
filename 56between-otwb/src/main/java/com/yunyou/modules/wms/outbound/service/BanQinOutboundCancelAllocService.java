package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.BanQinInventoryEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPrealloc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 取消分配
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundCancelAllocService {
    // 出库公共
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    // 取消预配记录
    @Autowired
    protected BanQinWmDelPreallocService wmDelPreallocManager;
    // 预配明细
    @Autowired
    protected BanQinWmSoPreallocService wmSoPreallocManager;
    // 分配明细
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocManager;
    // 取消分配拣货记录表
    @Autowired
    protected BanQinWmDelAllocService wmDelAllocManager;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryServiceManager;

    /**
     * 取消分配
     *
     * @param wmSoAllocEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage OutboundCancelAlloc(BanQinWmSoAllocEntity wmSoAllocEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 获取分配明细
        BanQinWmSoAlloc wmSoAllocModel = wmSoAllocManager.getByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId());
        // 1、校验状态，是否是完全分配
        if (!wmSoAllocModel.getStatus().equals(WmsCodeMaster.ALLOC_FULL_ALLOC.getCode())) {
            // 分配明细不是完全分配状态
            throw new WarehouseException(wmSoAllocModel.getAllocId() + "分配明细不是完全分配状态");
        }

        // 2、写入取消分配拣货记录表，状态完全分配(40)
        this.wmDelAllocManager.saveDelAlloc(wmSoAllocEntity);
        // 3、删除分配明细
        this.wmSoAllocManager.delete(wmSoAllocModel);
        // 4、如果分配明细有预配明细，那么校验存在的预配明细是否为0，是，则删除
        if (StringUtils.isNotEmpty(wmSoAllocEntity.getPreallocId())) {
            // 查询预配明细是否存在被分配(即分配明细与预配明细关联)
            BanQinWmSoAlloc condition = new BanQinWmSoAlloc();
            condition.setPreallocId(wmSoAllocEntity.getPreallocId());
            condition.setOrgId(wmSoAllocEntity.getOrgId());
            Integer count = wmSoAllocManager.findList(condition).size();
            // 如果大于0，那么修改预配明细，如果=0，那么删除预配明细
            if (count == 0) {
                BanQinWmSoPrealloc wmSoPreallocModel = wmSoPreallocManager.getByPreallocId(wmSoAllocEntity.getPreallocId(), wmSoAllocEntity.getOrgId());
                if (wmSoPreallocModel.getQtyPreallocEa() == 0D) {
                    // 删除预配明细表
                    this.wmSoPreallocManager.delete(wmSoPreallocModel);
                }
            }
        }

        // 5、更新库存、交易(操作数量=实际拣货数量)
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.CANCEL_ALLOCATION);// ACTION_CODE 取消分配
        invEntity.setOwnerCode(wmSoAllocEntity.getOwnerCode());// 货主
        invEntity.setSkuCode(wmSoAllocEntity.getSkuCode());// 商品
        invEntity.setLotNum(wmSoAllocEntity.getLotNum());// 批次号
        invEntity.setLocCode(wmSoAllocEntity.getLocCode());// 分配库位
        invEntity.setTraceId(wmSoAllocEntity.getTraceId());// 分配库位跟踪号
        invEntity.setQtyEaOp(wmSoAllocEntity.getQtyEa());// 分配数量
        invEntity.setOrgId(wmSoAllocEntity.getOrgId());

        inventoryServiceManager.updateInventory(invEntity);

        // 6、更新出库单明细、波次单明细、出库单状态、波次单状态
        outboundCommon.updateOrder(ActionCode.CANCEL_ALLOCATION, wmSoAllocEntity.getQtyEa(), wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getLineNo(), wmSoAllocEntity.getWaveNo(), wmSoAllocEntity.getOrgId());

        return msg;
    }

}