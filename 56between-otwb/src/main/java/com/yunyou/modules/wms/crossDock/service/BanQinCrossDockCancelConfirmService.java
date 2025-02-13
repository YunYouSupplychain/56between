package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.crossDock.entity.BanQinGetCdAllocStatusAndOutStepQueryEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.service.BanQinInboundRcOperationService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 取消执行越库
 *
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinCrossDockCancelConfirmService {
    @Autowired
    protected BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    protected BanQinOutboundCancelShipmentService outboundCancelShipmentService;
    @Autowired
    protected BanQinOutboundCancelPickingService outboundCancelPickingService;
    @Autowired
    protected BanQinOutboundCancelAllocService outboundCancelAllocService;
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;
    @Autowired
    private BanQinCrossDockService crossDockService;
    @Autowired
    private BanQinInboundRcOperationService inboundCancelReceiving;

    /**
     * 取消执行越库，ASN收货明细和SO商品明细 全部回到创建状态
     */
    @Transactional
    public ResultMessage crossDockCancelConfirm(String asnNo, String rcvLineNo, String orgId) throws WarehouseException {
        // 获取收货明细行
        BanQinWmAsnDetailReceiveEntity asnRcvEntity = wmAsnDetailReceiveService.getEntityByAsnNoAndLineNo(asnNo, rcvLineNo, orgId);
        if (!asnRcvEntity.getStatus().equals(WmsCodeMaster.ASN_FULL_RECEIVING.getCode())) {
            throw new WarehouseException("[" + asnNo + "][" + rcvLineNo + "]不是完全收货，不能操作");
        }
        // 校验是否存在生成装车单的记录
        if (this.wmLdDetailService.checkIsGeneratorLdByCd(asnNo, rcvLineNo, orgId)) {

        }
        // 校验是否允许发运取消，状态是否回归到越库原始状态
        BanQinGetCdAllocStatusAndOutStepQueryEntity checkCondition = new BanQinGetCdAllocStatusAndOutStepQueryEntity();
        checkCondition.setAsnNo(asnNo);
        checkCondition.setRcvLineNo(rcvLineNo);
        checkCondition.setOrgId(orgId);
        List<BanQinGetCdAllocStatusAndOutStepQueryEntity> checkItems = crossDockService.getCdAllocStatusAndOutStepQueryList(checkCondition);
        for (BanQinGetCdAllocStatusAndOutStepQueryEntity checkItem : checkItems) {
            if (checkItem.getCdOutStep().equals(WmsCodeMaster.RCV_ALLOC_PK.getCode()) && checkItem.getStatus().compareTo(WmsCodeMaster.ALLOC_FULL_PICKING.getCode()) > 0) {

            } else if (checkItem.getCdOutStep().equals(WmsCodeMaster.RCV_ALLOC.getCode()) && checkItem.getStatus().compareTo(WmsCodeMaster.ALLOC_FULL_ALLOC.getCode()) > 0) {
                throw new WarehouseException("[" + asnNo + "][" + rcvLineNo + "]越库操作到分配，存在分配拣货明细已拣货或己发货");
            }
        }
        // 无论是直接越库还是分拨直库，获取出库单分配明细行
        // 获取完全发货状态的分配明细记录
        List<BanQinWmSoAllocEntity> wmSoAllocEntities = wmSoAllocService.getEntityByCd(asnNo, rcvLineNo,
            new String[]{WmsCodeMaster.CD_TYPE_DIRECT.getCode(), WmsCodeMaster.CD_TYPE_INDIRECT.getCode()},
            new String[]{WmsCodeMaster.ALLOC_FULL_SHIPPING.getCode()},
            orgId);
        // for循环分配明细行，执行取消发货
        if (wmSoAllocEntities.size() > 0) {
            for (BanQinWmSoAllocEntity wmSoAllocEntity : wmSoAllocEntities) {
                if (!WmsCodeMaster.ALLOC_FULL_SHIPPING.getCode().equals(wmSoAllocEntity.getStatus())) {
                    continue;
                }
                // 完全发货，执行取消发货
                outboundCancelShipmentService.outboundCancelShipment(wmSoAllocEntity);
            }
        }
        // for循环分配明细行，执行取消拣货/取消分配操作
        // 获取完全拣货、完全分配状态的分配明细记录
        wmSoAllocEntities = wmSoAllocService.getEntityByCd(asnNo, rcvLineNo,
            new String[]{WmsCodeMaster.CD_TYPE_DIRECT.getCode(), WmsCodeMaster.CD_TYPE_INDIRECT.getCode()},
            new String[]{WmsCodeMaster.ALLOC_FULL_PICKING.getCode(), WmsCodeMaster.ALLOC_FULL_ALLOC.getCode()},
            orgId);
        if (wmSoAllocEntities.size() > 0) {
            for (BanQinWmSoAllocEntity wmSoAllocEntity : wmSoAllocEntities) {
                // 完全拣货，执行取消拣货
                if (wmSoAllocEntity.getStatus().equals(WmsCodeMaster.ALLOC_FULL_PICKING.getCode())) {
                    // 取消拣货需要回到Crossdock中
                    outboundCancelPickingService.outboundCancelPicking(wmSoAllocEntity);
                }
                // 完全分配，执行取消分配
                else if (wmSoAllocEntity.getStatus().equals(WmsCodeMaster.ALLOC_FULL_ALLOC.getCode())) {
                    outboundCancelAllocService.OutboundCancelAlloc(wmSoAllocEntity);
                }
            }
        }
        // 取消收货
        asnRcvEntity.setActionCode(ActionCode.CANCEL_RECEIVING);
        return inboundCancelReceiving.inboundCancelReceiving(asnRcvEntity);
    }

}
