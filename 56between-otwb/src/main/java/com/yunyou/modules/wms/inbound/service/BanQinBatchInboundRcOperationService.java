package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.edi.service.EdiSendOrderInfoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.crossDock.service.BanQinCrossDockCancelConfirmService;
import com.yunyou.modules.wms.crossDock.service.BanQinCrossDockConfirmService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：批量入库收货操作Service
 */
@Service
public class BanQinBatchInboundRcOperationService {
    @Autowired
    private BanQinInboundRcOperationService inboundRcOperationService;
    @Autowired
    @Lazy
    private BanQinCrossDockConfirmService crossDockConfirmService;
    @Autowired
    @Lazy
    private BanQinInboundOperationService inboundOperationService;
    @Autowired
    private EdiSendOrderInfoService ediSendOrderInfoService;
    @Autowired
    @Lazy
    private BanQinCrossDockCancelConfirmService crossDockCancelConfirmService;

    /**
     * 描述： 批量收货确认
     *
     * @param entities 收货明细信息
     */
    public ResultMessage inboundBatchReceiving(List<BanQinWmAsnDetailReceiveEntity> entities) {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmAsnDetailReceiveEntity> successList = Lists.newArrayList();
        for (BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity : entities) {
            try {
                if (StringUtils.isNotEmpty(wmAsnDetailReceiveEntity.getCdType())) {
                    // 越库收货
                    if (StringUtils.isNotEmpty(wmAsnDetailReceiveEntity.getQcStatus())
                            && WmsCodeMaster.QC_FULL_QC.getCode().equals(wmAsnDetailReceiveEntity.getQcStatus())
                            && (StringUtils.isEmpty(wmAsnDetailReceiveEntity.getLotAtt04()) || !WmsConstants.YES.equals(wmAsnDetailReceiveEntity.getLotAtt04()))) {
                        msg.setSuccess(false);
                        msg.addMessage("[" + wmAsnDetailReceiveEntity.getAsnNo() + "][" + wmAsnDetailReceiveEntity.getLineNo() + "]执行越库的质检商品，批次属性品质必须是良品");
                    }
                    crossDockConfirmService.crossDockConfirmByAsn(wmAsnDetailReceiveEntity);
                } else {
                    wmAsnDetailReceiveEntity.setActionCode(ActionCode.RECEIVING);
                    inboundRcOperationService.inboundReceiving(wmAsnDetailReceiveEntity);
                }
                successList.add(wmAsnDetailReceiveEntity);
            } catch (WarehouseException e) {
                msg.addMessage("行号" + wmAsnDetailReceiveEntity.getLineNo() + e.getMessage());
            }
            // 完全收货状态，并且不存在未完成的上架任务，是否自动关闭ASN
            inboundOperationService.checkRcvAutoCloseAsn(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getOrgId());
        }
        if (CollectionUtil.isNotEmpty(successList)) {
            // WMS参数:是否调用外部接口（Y：调用；N：不调用）
            final String isCallEdi = SysControlParamsUtils.getValue(ControlParamCode.WM_CALL_EDI.getCode());
            if (WmsConstants.YES.equals(isCallEdi)) {
                ediSendOrderInfoService.saveByWmsAsn(successList);
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述： 批量取消收货
     *
     * @param entities 收货明细信息
     */
    public ResultMessage inboundBatchCancelReceiving(List<BanQinWmAsnDetailReceiveEntity> entities) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity : entities) {
            try {
                // 越库匹配取消收货
                if (StringUtils.isNotEmpty(wmAsnDetailReceiveEntity.getCdType())) {
                    crossDockCancelConfirmService.crossDockCancelConfirm(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getLineNo(), wmAsnDetailReceiveEntity.getOrgId());
                } else {
                    wmAsnDetailReceiveEntity.setActionCode(ActionCode.CANCEL_RECEIVING);
                    inboundRcOperationService.inboundCancelReceiving(wmAsnDetailReceiveEntity);
                }
            } catch (WarehouseException e) {
                msg.addMessage("行号" + wmAsnDetailReceiveEntity.getLineNo() + e.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }
}
