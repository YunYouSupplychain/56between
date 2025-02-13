package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.config.Global;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.entity.extend.TmTransportSignInfo;
import com.yunyou.modules.tms.order.manager.mapper.TmTransportOrderMapper;
import com.yunyou.modules.tms.order.service.TmTransportOrderDeliveryService;
import com.yunyou.modules.tms.order.service.TmTransportOrderLabelService;
import com.yunyou.modules.tms.order.service.TmTransportOrderRouteService;
import com.yunyou.modules.tms.order.service.TmTransportOrderTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

/**
 * 签收服务
 *
 * @author liujianhua
 * @version 2022.8.3
 */
@Service
@Transactional(readOnly = true)
public class TmSignManager extends BaseService {
    @Autowired
    private TmTransportOrderMapper mapper;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmTransportOrderDeliveryService tmTransportOrderDeliveryService;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;
    @Autowired
    private TmTransportOrderRouteManager tmTransportOrderRouteManager;
    @Autowired
    private TmReceiveManager tmReceiveManager;
    @Autowired
    private TmUpdateStatusManager tmUpdateStatusManager;
    @Autowired
    private TmLabelLogManager tmLabelLogManager;

    /**
     * 签收 - 按运输订单
     */
    @Transactional
    public void signTransport(TmTransportSignInfo tmTransportSignInfo) {
        // 查找可签收标签
        List<TmTransportOrderLabel> orderLabelList = mapper.findCanSignLabel(tmTransportSignInfo.getTransportNo(), tmTransportSignInfo.getOrgId());
        for (TmTransportOrderLabel orderLabel : orderLabelList) {
            // 标签在途且 TMS参数：交接时网点自动收、发(Y:是 N:否)为“Y”
            final String HANDOVER_OUTLET_AUTO_R_S = SysControlParamsUtils.getValue(SysParamConstants.HANDOVER_OUTLET_AUTO_R_S, orderLabel.getOrgId());
            if (Global.Y.equals(HANDOVER_OUTLET_AUTO_R_S)) {
                TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(orderLabel.getTransportNo(), orderLabel.getLabelNo(), orderLabel.getBaseOrgId());
                if (!TmsConstants.NULL.equals(orderRoute.getDispatchNo())) {
                    tmReceiveManager.receiveByLabel(orderLabel.getTransportNo(), orderLabel.getLabelNo(), orderRoute.getNextOutletCode());
                    tmTransportOrderTrackService.saveTrackNode(orderLabel.getTransportNo(), orderLabel.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_ARRIVE, UserUtils.getUser());
                }
            }
            tmTransportSignInfo.setLabelNo(orderLabel.getLabelNo());
            this.signLabel(tmTransportSignInfo);
        }
    }

    /**
     * 签收 - 按标签
     */
    @Transactional
    public void signLabel(TmTransportSignInfo tmTransportSignInfo) {
        String transportNo = tmTransportSignInfo.getTransportNo();
        String labelNo = tmTransportSignInfo.getLabelNo();
        String orgId = tmTransportSignInfo.getOrgId();

        TmTransportOrderLabel orderLabel = tmTransportOrderLabelService.getByTransportNoAndLabelNo(transportNo, labelNo, orgId);
        if (TmsConstants.ORDER_LABEL_STATUS_00.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签号[{1}]未收货，无法操作", transportNo, labelNo));
        }
        if (TmsConstants.ORDER_LABEL_STATUS_20.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签号[{1}]已签收，无法操作", transportNo, labelNo));
        }
        if (TmsConstants.ORDER_LABEL_STATUS_30.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签号[{1}]已回单，无法操作", transportNo, labelNo));
        }
        // 更新运输订单标签签收信息
        orderLabel.setStatus(TmsConstants.ORDER_LABEL_STATUS_20);
        orderLabel.setSignBy(tmTransportSignInfo.getSignBy());
        orderLabel.setSignTime(tmTransportSignInfo.getSignTime());
        orderLabel.setSignRemarks(tmTransportSignInfo.getRemarks());
        tmTransportOrderLabelService.save(orderLabel);
        // 更新签收信息
        tmTransportOrderDeliveryService.updateSignInfo(tmTransportSignInfo);
        // 保存路由跟踪信息
        tmTransportOrderTrackService.saveTrackNode(orderLabel.getTransportNo(), orderLabel.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_SIGN, UserUtils.getUser());
        // 插入已签收标签履历记录
        tmLabelLogManager.saveSignLog(transportNo, labelNo, orgId);
        // 删除运输网点路由
        tmTransportOrderRouteManager.remove(tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, orderLabel.getBaseOrgId()));
        // 更新运输订单状态
        tmUpdateStatusManager.updateTransport(transportNo);
    }
}
