package com.yunyou.modules.tms.order.manager;

import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderHeader;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderLabel;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 派车单标签记录Manager
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchOrderLabelManager extends BaseService {
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmDispatchOrderLabelService tmDispatchOrderLabelService;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;

    @Transactional
    public void save(TmDispatchOrderLabel orderLabel) {
        TmTransportOrderHeader transportOrderHeader = tmTransportOrderHeaderService.getByNo(orderLabel.getTransportNo());
        // 保存前，设置门店派车单号
        orderLabel.setStoreDispatchNo(orderLabel.getDispatchNo() + "-" + orderLabel.getDispatchSiteOutletCode() + "-" + transportOrderHeader.getOrderSource() + "-" + transportOrderHeader.getDataSource());
        tmDispatchOrderLabelService.save(orderLabel);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(orderLabel);
    }

    @Transactional
    public void remove(TmDispatchOrderLabel orderLabel) {
        TmDispatchOrderHeader tmDispatchOrderHeader = tmDispatchOrderHeaderService.getByNo(orderLabel.getDispatchNo());
        // 如果是提货的派车单标签记录 且 配送点不是派车单的派车网点（注：提货的派车标签记录其配送的是派车单的派车网点时，其本身没有删除功能，在同标签号的送货的派车标签记录删除时一同删除）
        if (TmsConstants.RECEIVE.equals(orderLabel.getReceiveShip()) && !tmDispatchOrderHeader.getDispatchOutletCode().equals(orderLabel.getDispatchSiteOutletCode())) {
            // 删除同标签号的送货的派车单标签记录将送货的派车单标签记录
            TmDispatchOrderLabel shipLabel = tmDispatchOrderLabelService.getByNoAndLabelAndRS(orderLabel.getDispatchNo(), orderLabel.getLabelNo(), TmsConstants.SHIP, orderLabel.getOrgId());
            if (shipLabel != null) {
                this.remove(shipLabel);
            }
        } else if (TmsConstants.SHIP.equals(orderLabel.getReceiveShip())) {
            // 如果是送货的派车单标签记录，检查标签的提货的网点是否是派车单的派车网点，如果是则将其一同删除
            TmDispatchOrderLabel receiveLabel = tmDispatchOrderLabelService.getByNoAndLabelAndRS(orderLabel.getDispatchNo(), orderLabel.getLabelNo(), TmsConstants.RECEIVE, orderLabel.getOrgId());
            if (receiveLabel != null && receiveLabel.getDispatchSiteOutletCode().equals(tmDispatchOrderHeader.getDispatchOutletCode())) {
                this.remove(receiveLabel);
            }
        }
        if (TmsConstants.RECEIVE.equals(orderLabel.getReceiveShip())) {
            // 解绑标签路由中绑定的预派车单号
            TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(orderLabel.getTransportNo(), orderLabel.getLabelNo(), orderLabel.getBaseOrgId());
            if (!TmsConstants.NULL.equals(orderRoute.getDispatchNo())) {
                throw new TmsException("派车单[" + orderLabel.getDispatchNo() + "]已发车，无法操作");
            }
            // 运输网点路由标签tm_transport_order_route取消绑定派车单号
            orderRoute.setPreAllocDispatchNo(TmsConstants.NULL);
            tmTransportOrderRouteService.save(orderRoute);
        }
        // 删除标签
        tmDispatchOrderLabelService.delete(orderLabel);
        // 删除授权数据
        tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_LABEL.getValue(), orderLabel.getId());
        // 标签网点路由信息移除配送网点授权信息
        TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(orderLabel.getTransportNo(), orderLabel.getLabelNo(), orderLabel.getBaseOrgId());
        tmAuthorityManager.removeOutletAuthorityData(orderLabel.getDispatchSiteOutletCode(), orderLabel.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
    }

    @Transactional
    public void labelDelivery(TmDispatchOrderLabel dLabel, User user) {
        dLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_10);
        tmDispatchOrderLabelService.saveByUser(dLabel, user);
    }
}
