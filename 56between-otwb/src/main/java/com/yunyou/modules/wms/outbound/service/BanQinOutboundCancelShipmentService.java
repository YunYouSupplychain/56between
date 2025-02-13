package com.yunyou.modules.wms.outbound.service;

import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.service.BanQinInventorySerialService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvSerialService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoHeader;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerialEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 取消发运
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundCancelShipmentService {
    // 公共方法
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 出库公共
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    // 出库单
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    // 分配明细
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    // 库存更新
    @Autowired
    protected BanQinInventoryService inventoryService;
    // 关闭
    @Autowired
    protected BanQinOutboundCloseOrCancelService outboundCloseOrCancelService;
    // 库存序列号
    @Autowired
    protected BanQinWmInvSerialService wmInvSerialService;
    // 装车单明细
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;
    // 装车单
    @Autowired
    protected BanQinWmLdHeaderService wmLdHeaderService;
    // 装车单操作方法类
    @Autowired
    protected BanQinOutboundLdService outboundLdService;
    // 发货
    @Autowired
    protected BanQinOutboundBatchShipmentAction outboundBatchShipmentAction;
    // 出库序列号
    @Autowired
    protected BanQinWmSoSerialService wmSoSerialService;
    // 库存序列号
    @Autowired
    protected BanQinInventorySerialService inventorySerialService;

    /**
     * 取消发运
     */
    @Transactional
    public void outboundCancelShipment(BanQinWmSoAllocEntity wmSoAllocEntity) {
        // 校验：如果订单已下发TMS，则不允许取消发运
        BanQinWmSoHeader wmSoHeader = wmSoHeaderService.findBySoNo(wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getOrgId());
        if (WmsConstants.YES.equals(wmSoHeader.getIsPushed())) {
            throw new WarehouseException(wmSoHeader.getSoNo() + "已下发运输，不能操作");
        }
        // 1、获取分配明细
        BanQinWmSoAlloc wmSoAllocModel = wmSoAllocService.getByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId());
        if (!wmSoAllocModel.getStatus().equals(WmsCodeMaster.ALLOC_FULL_SHIPPING.getCode())) {
            throw new WarehouseException("分配拣货明细不是完全发运状态", wmSoAllocEntity.getAllocId());
        }
        BanQinWmSoHeader headerModel = this.wmSoHeaderService.findBySoNo(wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getOrgId());
        // 如果出库单未生成装车单，并且装车状态==完全交接，那么不能取消发货
        if (headerModel.getLdStatus() != null && WmsCodeMaster.LD_FULL_DELIVERY.getCode().equals(headerModel.getLdStatus())) {
            throw new WarehouseException(wmSoAllocEntity.getAllocId() + "出库单完全交接，不能操作", wmSoAllocEntity.getSoNo());
        }
        // 3、更新分配明细信息 (发货人、发货时间、数量不变、状态拣货60)
        wmSoAllocModel.setShipOp(null);// 发货人
        wmSoAllocModel.setShipTime(null);// 发货时间
        wmSoAllocModel.setStatus(WmsCodeMaster.ALLOC_FULL_PICKING.getCode());// 状态：完全拣货
        this.wmSoAllocService.save(wmSoAllocModel);
        // 4、更新库存、交易(操作数量=分配数量)
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.CANCEL_SHIPMENT);// ACTION_CODE 取消发货
        invEntity.setOwnerCode(wmSoAllocEntity.getOwnerCode());// 货主
        invEntity.setSkuCode(wmSoAllocEntity.getSkuCode());// 商品
        invEntity.setLotNum(wmSoAllocEntity.getLotNum());// 批次号
        invEntity.setLocCode(wmSoAllocEntity.getToLoc());// 拣货库位
        invEntity.setTraceId(wmSoAllocEntity.getToId());// 拣货库位跟踪号
        invEntity.setQtyEaOp(wmSoAllocEntity.getQtyEa());// 发货数量 = 分配数量
        invEntity.setOrderNo(wmSoAllocEntity.getSoNo());// 出库单号
        invEntity.setLineNo(wmSoAllocEntity.getLineNo());// 出库单行号
        invEntity.setTaskId(wmSoAllocEntity.getAllocId());// 任务号 = 分配号
        invEntity.setPackCode(wmSoAllocEntity.getPackCode());// 包装代码
        invEntity.setUom(wmSoAllocEntity.getUom());// 包装单位
        invEntity.setQtyUom(wmSoAllocEntity.getQtyUom());// 包装数量=分配数uom
        invEntity.setOrgId(wmSoAllocEntity.getOrgId());
        inventoryService.updateInventory(invEntity);
        // 回写库存序列号
        List<BanQinWmSoSerialEntity> soSerialItems = wmSoSerialService.findEntityByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId());
        if (soSerialItems.size() > 0) {
            for (BanQinWmSoSerialEntity soSerialItem : soSerialItems) {
                BanQinInventorySerialEntity serialEntity = new BanQinInventorySerialEntity();
                serialEntity.setOwnerCode(soSerialItem.getOwnerCode());
                serialEntity.setSkuCode(soSerialItem.getSkuCode());
                serialEntity.setSerialNo(soSerialItem.getSerialNo());
                serialEntity.setLotNum(soSerialItem.getLotNum());
                serialEntity.setOrderNo(wmSoAllocEntity.getSoNo());
                serialEntity.setLineNo(wmSoAllocEntity.getLineNo());
                serialEntity.setOrderType(WmsCodeMaster.ORDER_SO.getCode());// 订单类型-出库单
                serialEntity.setSerialTranType(WmsCodeMaster.CSP.getCode());// 交易类型-取消发货
                serialEntity.setOrgId(wmSoAllocEntity.getOrgId());
                // 库存序列号更新、交易
                inventorySerialService.updateInventorySerial(serialEntity);
            }
        }
        // 6、更新出库单明细、波次单明细、出库单状态、波次单状态
        outboundCommon.updateOrder(ActionCode.CANCEL_SHIPMENT, wmSoAllocEntity.getQtyEa(), wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getLineNo(), wmSoAllocEntity.getWaveNo(), wmSoAllocEntity.getOrgId());
    }

}