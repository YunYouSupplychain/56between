package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerial;
import com.yunyou.modules.wms.inventory.service.BanQinInventorySerialService;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvSerialService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerialEntity;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 发运确认-单行
 *
 * @author WMj
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundShipmentService {
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
    @Lazy
    protected BanQinOutboundLdService outboundLdService;
    // 发货
    @Autowired
    protected BanQinOutboundBatchShipmentAction outboundBatchShipmentAction;
    // 库存序列号
    @Autowired
    protected BanQinInventorySerialService inventorySerialService;
    // 出库序列号
    @Autowired
    protected BanQinWmSoSerialService wmSoSerialService;

    /**
     * 发运确认
     * 
     * @param wmSoAllocEntity 分配明细 按波次单号/出库单号/分配编号
     * @return
     */
    @Transactional
    public ResultMessage outboundShipment(BanQinWmSoAllocEntity wmSoAllocEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 1、获取分配明细
        BanQinWmSoAlloc wmSoAllocModel = wmSoAllocService.getByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId());
        if (!wmSoAllocModel.getStatus().equals(WmsCodeMaster.ALLOC_FULL_PICKING.getCode())) {
            // 校验是否是拣货状态,分配拣货明细不是完全拣货状态
            throw new WarehouseException("分配拣货明细不是完全拣货状态", wmSoAllocEntity.getAllocId());
        }
        // 复核状态发运修改(控制参数在分配、拣货时取值控制)
        /* 2、校验是否需要复核（90）/是否已经复核(99)， 如果未复核(00)，那么提示未复核不能发运(不复核(90),可直接发运) */
        if (wmSoAllocModel.getCheckStatus().equals(WmsCodeMaster.CHECK_NEW.getCode())) {
            // 分配明细未复核不能发运
            throw new WarehouseException("分配明细未复核不能发运", wmSoAllocEntity.getAllocId());
        }
        // 校验需要序列号管理的商品，是否已扫描序列号
        if (StringUtils.isNotEmpty(wmSoAllocEntity.getIsSerial()) && wmSoAllocEntity.getIsSerial().equals(WmsConstants.YES)) {
            List<BanQinWmSoSerialEntity> wmSoSerialItems = wmSoSerialService.findByAllocIds(Lists.newArrayList(wmSoAllocEntity.getAllocId()), wmSoAllocEntity.getOrgId());
            if (wmSoSerialItems.size() == 0 || wmSoSerialItems.size() != wmSoAllocEntity.getQtyEa()) {
                // 如果序列号记录数!=分配明细数量，那么不能发货
                throw new WarehouseException(wmSoAllocEntity.getAllocId() + "序列号记录数!=分配明细数量，不能发货");
            }
        }
        // 3、更新分配明细信息 (发货人、发货时间、数量不变、状态发运80)
        wmSoAllocModel.setShipOp(UserUtils.getUser().getName());// 发货人
        wmSoAllocModel.setShipTime(new Date());// 发货时间
        wmSoAllocModel.setStatus(WmsCodeMaster.ALLOC_FULL_SHIPPING.getCode());// 状态：完全发货
        this.wmSoAllocService.save(wmSoAllocModel);
        // 4、更新库存、交易(操作数量=分配数量)
        // 库存更新
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.SHIPMENT);// ACTION_CODE 发货
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

        // 删除库存序列号
        // 查询库存序列号
        List<BanQinWmInvSerial> invModels = wmInvSerialService.getByAllocIds(new String[]{wmSoAllocEntity.getAllocId()}, wmSoAllocEntity.getOrgId());
        if (invModels.size() > 0) {
            for (BanQinWmInvSerial invModel : invModels) {
                BanQinInventorySerialEntity serialEntity = new BanQinInventorySerialEntity();
                BeanUtils.copyProperties(invModel, serialEntity);
                serialEntity.setOrderNo(wmSoAllocEntity.getSoNo());
                serialEntity.setLineNo(wmSoAllocEntity.getLineNo());
                serialEntity.setOrderType(WmsCodeMaster.ORDER_SO.getCode());
                serialEntity.setSerialTranType(WmsCodeMaster.SP.getCode());// 交易类型-发货
                // 库存序列号更新、交易
                inventorySerialService.updateInventorySerial(serialEntity);
            }
        }
        // 6、更新出库单明细、波次单明细、出库单状态、波次单状态
        outboundCommon.updateOrder(ActionCode.SHIPMENT, wmSoAllocEntity.getQtyEa(), wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getLineNo(), wmSoAllocEntity.getWaveNo(), wmSoAllocEntity.getOrgId());
        return msg;
    }
}