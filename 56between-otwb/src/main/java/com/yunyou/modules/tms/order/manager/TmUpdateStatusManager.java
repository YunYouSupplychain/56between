package com.yunyou.modules.tms.order.manager;

import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmUpdateStatusManager extends BaseService {
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;

    @Transactional
    public void updateTransport(String transportNo) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.getByNo(transportNo);
        if (TmsConstants.TRANSPORT_ORDER_STATUS_90.equals(orderHeader.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]已取消，无法操作", orderHeader.getTransportNo()));
        }
        if (TmsConstants.TRANSPORT_ORDER_STATUS_99.equals(orderHeader.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]已关闭，无法操作", orderHeader.getTransportNo()));
        }
        // 是否含有新建/是否含有收货/是否含有配载/是否含有签收/是否含有回单
        boolean isHasAdd = false, isHasReceive = false, isHasDispatch = false, isHasSign = false, isHasReceipt = false;
        // 查找运输订单下所有标签
        List<TmTransportOrderLabel> orderLabels = tmTransportOrderLabelService.findList(new TmTransportOrderLabel(orderHeader.getTransportNo(), orderHeader.getOrgId()));
        for (TmTransportOrderLabel orderLabel : orderLabels) {
            // 是否含有新建状态标签
            if (!isHasAdd && TmsConstants.ORDER_LABEL_STATUS_00.equals(orderLabel.getStatus())) {
                isHasAdd = true;
                continue;
            }
            // 是否含有收货状态标签
            if (!isHasReceive && TmsConstants.ORDER_LABEL_STATUS_10.equals(orderLabel.getStatus())) {
                isHasReceive = true;
                continue;
            }
            // 是否含有配载状态标签
            if (!isHasDispatch && TmsConstants.ORDER_LABEL_STATUS_15.equals(orderLabel.getStatus())) {
                isHasDispatch = true;
                continue;
            }
            // 是否含有签收状态标签
            if (!isHasSign && TmsConstants.ORDER_LABEL_STATUS_20.equals(orderLabel.getStatus())) {
                isHasSign = true;
                continue;
            }
            // 是否含有回单状态标签
            if (!isHasReceipt && TmsConstants.ORDER_LABEL_STATUS_30.equals(orderLabel.getStatus())) {
                isHasReceipt = true;
            }
        }

        String newOrderStatus;
        if (isHasReceipt && !isHasSign && !isHasDispatch && !isHasReceive && !isHasAdd) {
            // 有回单，无签收、无配载、无收货、无新建，则全部回单
            newOrderStatus = TmsConstants.TRANSPORT_ORDER_STATUS_50;
        } else if (isHasReceipt) {
            // 有回单，有签收或配载或收货或新建，则部分回单
            newOrderStatus = TmsConstants.TRANSPORT_ORDER_STATUS_45;
        } else if (isHasSign && !isHasDispatch && !isHasReceive && !isHasAdd) {
            // 无回单，有签收、无配载、无收货、无新建，则全部签收
            newOrderStatus = TmsConstants.TRANSPORT_ORDER_STATUS_40;
        } else if (isHasSign) {
            // 无回单，有签收，有配载或收货或新建，则部分签收
            newOrderStatus = TmsConstants.TRANSPORT_ORDER_STATUS_35;
        } else if (isHasDispatch && !isHasReceive && !isHasAdd) {
            // 无回单，无签收、有配载、无收货、无新建，则全部配载
            newOrderStatus = TmsConstants.TRANSPORT_ORDER_STATUS_32;
        } else if (isHasDispatch) {
            // 无回单，无签收、有配载、有收货或新建，则部分签收
            newOrderStatus = TmsConstants.TRANSPORT_ORDER_STATUS_31;
        } else if (isHasReceive && !isHasAdd) {
            // 无回单，无签收，无配载，有收货，无新建，则全部收货
            newOrderStatus = TmsConstants.TRANSPORT_ORDER_STATUS_30;
        } else if (isHasReceive) {
            // 无回单，无签收，无配载。有收货，有新建，则部分收货
            newOrderStatus = TmsConstants.TRANSPORT_ORDER_STATUS_25;
        } else {
            newOrderStatus = TmsConstants.TRANSPORT_ORDER_STATUS_10;
        }
        orderHeader.setOrderStatus(newOrderStatus);
        tmTransportOrderHeaderService.save(orderHeader);
    }
}
