package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.BanQinInventoryEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPrealloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPreallocEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 取消预配
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundCancelPreallocService {
    // 出库公共
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    // 出库单行
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    // 预配明细
    @Autowired
    protected BanQinWmSoPreallocService wmSoPreallocService;
    // 取消预配记录
    @Autowired
    protected BanQinWmDelPreallocService wmDelPreallocService;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryService;
    @Autowired
    private BanQinWmSoAllocService wmSoAllocService;

    /**
     * 取消预配
     * @param wmSoPreallocEntity
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage outboundCancelPrealloc(BanQinWmSoPreallocEntity wmSoPreallocEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 1、获取出库单行信息(出库单号、行号),校验取消状态
        BanQinWmSoDetail wmSoDetailModel = this.wmSoDetailService.checkStatus(wmSoPreallocEntity.getSoNo(), wmSoPreallocEntity.getLineNo(), wmSoPreallocEntity.getOrgId());
        // 不能取消预配
        if (wmSoPreallocEntity.getQtyPreallocEa() > wmSoDetailModel.getQtyPreallocEa()) {
            // 数据过期
            throw new WarehouseException("数据过期");
        }
        // 2、写入取消预配记录
        this.wmDelPreallocService.saveDelPrealloc(wmSoPreallocEntity);
        // 3、删除预配明细或者修改预配明细
        // 查询预配明细
        BanQinWmSoPrealloc wmSoPreallocModel = wmSoPreallocService.getByPreallocId(wmSoPreallocEntity.getPreallocId(), wmSoPreallocEntity.getOrgId());
        if (wmSoPreallocModel.getQtyPreallocEa() < wmSoPreallocEntity.getQtyPreallocEa()) {
            // 数据过期
            throw new WarehouseException("数据过期");
        }
        // 查询预配明细是否存在被分配(即分配明细与预配明细关联)
        BanQinWmSoAlloc condition = new BanQinWmSoAlloc();
        condition.setPreallocId(wmSoPreallocEntity.getPreallocId());
        condition.setOrgId(wmSoPreallocEntity.getOrgId());
        Integer count = wmSoAllocService.findList(condition).size();
        // 如果大于0，那么修改预配明细，如果=0，那么删除预配明细
        if (count > 0) {
            // 修改预配明细的预配数量=0，与分配明细关联,不能删除预配明细
            wmSoPreallocModel.setQtyPreallocEa(wmSoPreallocModel.getQtyPreallocEa() - wmSoPreallocEntity.getQtyPreallocEa());
            wmSoPreallocModel.setQtyPreallocUom(wmSoPreallocModel.getQtyPreallocUom() - wmSoPreallocEntity.getQtyPreallocUom());
            this.wmSoPreallocService.save(wmSoPreallocModel);
        } else {
            // 删除预配明细
            this.wmSoPreallocService.delete(wmSoPreallocModel);
        }
        // 当预配明细的预配数量=0 时，不进行库存更新
        if (wmSoPreallocEntity.getQtyPreallocEa() == 0D) {
            msg.setSuccess(true);
            return msg;
        }
        // 4、更新库存
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.CANCEL_PREALLOCATION); // 取消预配
        invEntity.setOwnerCode(wmSoPreallocEntity.getOwnerCode());// 货主
        invEntity.setSkuCode(wmSoPreallocEntity.getSkuCode());// 商品
        invEntity.setLotNum(wmSoPreallocEntity.getLotNum());// 批次号
        invEntity.setQtyEaOp(wmSoPreallocEntity.getQtyPreallocEa());// 预配数量
        invEntity.setOrgId(wmSoPreallocEntity.getOrgId());
        inventoryService.updateInventory(invEntity);
        // 5、更新出库单明细、波次单明细、出库单状态、波次单状态
        outboundCommon.updateOrder(ActionCode.CANCEL_PREALLOCATION, wmSoPreallocEntity.getQtyPreallocEa(), wmSoPreallocEntity.getSoNo(), wmSoPreallocEntity.getLineNo(), wmSoPreallocEntity.getWaveNo(), wmSoPreallocEntity.getOrgId());
        return msg;

    }
}