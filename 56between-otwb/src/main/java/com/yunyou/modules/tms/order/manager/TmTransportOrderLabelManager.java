package com.yunyou.modules.tms.order.manager;

import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.service.TmTransportOrderLabelService;
import com.yunyou.modules.tms.order.service.TmTransportOrderRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmTransportOrderLabelManager extends BaseService {
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmTransportOrderRouteManager tmTransportOrderRouteManager;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;

    @Transactional
    public void remove(TmTransportOrderLabel orderLabel) {
        if (!TmsConstants.ORDER_LABEL_STATUS_00.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签[{1}]不是新建状态，不能删除", orderLabel.getTransportNo(), orderLabel.getLabelNo()));
        }
        // 删除标签路由
        List<TmTransportOrderRoute> orderRoutes = tmTransportOrderRouteService.findList(new TmTransportOrderRoute(orderLabel.getTransportNo(), orderLabel.getLabelNo(), orderLabel.getBaseOrgId()));
        for (TmTransportOrderRoute orderRoute : orderRoutes) {
            tmTransportOrderRouteManager.remove(orderRoute);
        }
        // 删除标签信息
        tmTransportOrderLabelService.delete(orderLabel);
        // 删除授权数据信息
        tmAuthorityManager.remove(TmAuthorityTable.TM_TRANSPORT_ORDER_LABEL.getValue(), orderLabel.getId());
    }
}
