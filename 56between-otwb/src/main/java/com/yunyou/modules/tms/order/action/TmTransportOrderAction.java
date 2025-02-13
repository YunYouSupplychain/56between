package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.TmDirectDispatch;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.entity.TmTransportOrderTrack;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportReceiptInfo;
import com.yunyou.modules.tms.order.entity.extend.TmTransportSignInfo;
import com.yunyou.modules.tms.order.manager.TmDirectDispatchManager;
import com.yunyou.modules.tms.order.manager.TmSignManager;
import com.yunyou.modules.tms.order.manager.TmTransportOrderManager;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderLabelService;
import com.yunyou.modules.tms.order.service.TmTransportOrderTrackService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * 描述：运输订单业务处理类(非事务管理)
 */
@Service
public class TmTransportOrderAction extends BaseAction {
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;
    @Autowired
    private TmDirectDispatchManager tmDirectDispatchManager;
    @Autowired
    private TmSignManager tmSignManager;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;

    /**
     * 描述：根据派车单ID查询运输订单实体信息
     */
    public TmTransportOrderEntity getEntity(String id) {
        return tmTransportOrderManager.getEntity(id);
    }

    /**
     * 描述：分页查询运输订单实体信息
     */
    public Page<TmTransportOrderEntity> findPage(Page<TmTransportOrderEntity> page, TmTransportOrderEntity qEntity) {
        return tmTransportOrderManager.findPage(page, qEntity);
    }

    /**
     * 描述：获取各状态的运输订单数量
     */
    public TmTransportOrderEntity getEachStatusOrderQty(TmTransportOrderEntity qEntity) {
        TmTransportOrderEntity rs = new TmTransportOrderEntity();
        long toAuditOrderQty = 0L; // 待审核订单数 = 订单状态为新建的订单数量
        long toReceiveOrderQty = 0L; // 待收货订单数 = 订单状态为已审核的订单数量 + 订单状态为已部分收货的订单数量 + 订单状态为部分配载/签收且标签状态为新建的订单数量
        long toDispatchOrderQty = 0L; // 待配载订单数 = 待收货订单数量 + 订单状态为全部收货的订单数量 + 订单状态为部分配载的订单数量 + 订单状态为部分签收且标签状态为新建、已收货的订单数量
        long toSignOrderQty = 0L; // 待签收订单数 = 订单状态为全部配载的订单数量 + 订单状态为部分签收且标签状态为已配载的订单数量
        long signedOrderQty = 0L; // 已签收订单数 = 订单状态为全部签收的订单数量

        List<TmTransportOrderEntity> orderHeaderList = tmTransportOrderManager.findPage(new Page<TmTransportOrderEntity>(), qEntity).getList();
        for (TmTransportOrderHeader orderHeader : orderHeaderList) {
            // 新建
            if (TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(orderHeader.getOrderStatus())) {
                toAuditOrderQty += 1;
                continue;
            }
            // 已审核、部分收货
            if (TmsConstants.TRANSPORT_ORDER_STATUS_10.equals(orderHeader.getOrderStatus()) || TmsConstants.TRANSPORT_ORDER_STATUS_25.equals(orderHeader.getOrderStatus())) {
                toReceiveOrderQty += 1;
                toDispatchOrderQty += 1;
                continue;
            }
            // 全部收货
            if (TmsConstants.TRANSPORT_ORDER_STATUS_30.equals(orderHeader.getOrderStatus())) {
                toDispatchOrderQty += 1;
                continue;
            }
            // 部分配载
            if (TmsConstants.TRANSPORT_ORDER_STATUS_31.equals(orderHeader.getOrderStatus())) {
                toDispatchOrderQty += 1;
                List<TmTransportOrderLabel> orderLabelList = tmTransportOrderLabelService.findList(new TmTransportOrderLabel(orderHeader.getTransportNo(), orderHeader.getOrgId()));
                // 判断是否存在新建标签
                if (orderLabelList.stream().anyMatch(o -> TmsConstants.ORDER_LABEL_STATUS_00.equals(o.getStatus()))) {
                    toReceiveOrderQty += 1;
                }
                continue;
            }
            // 全部配载
            if (TmsConstants.TRANSPORT_ORDER_STATUS_32.equals(orderHeader.getOrderStatus())) {
                toSignOrderQty += 1;
                continue;
            }
            // 部分签收
            if (TmsConstants.TRANSPORT_ORDER_STATUS_35.equals(orderHeader.getOrderStatus())) {
                List<TmTransportOrderLabel> orderLabelList = tmTransportOrderLabelService.findList(new TmTransportOrderLabel(orderHeader.getTransportNo(), orderHeader.getOrgId()));
                // 判断是否存在新建标签
                if (orderLabelList.stream().anyMatch(o -> TmsConstants.ORDER_LABEL_STATUS_00.equals(o.getStatus()))) {
                    toReceiveOrderQty += 1;
                    toDispatchOrderQty += 1;
                }
                // 判断是否存在已收货标签
                if (orderLabelList.stream().anyMatch(o -> TmsConstants.ORDER_LABEL_STATUS_10.equals(o.getStatus()))) {
                    toDispatchOrderQty += 1;
                }
                // 判断是否存在已配载标签
                if (orderLabelList.stream().anyMatch(o -> TmsConstants.ORDER_LABEL_STATUS_15.equals(o.getStatus()))) {
                    toSignOrderQty += 1;
                }
                continue;
            }
            // 全部签收
            if (TmsConstants.TRANSPORT_ORDER_STATUS_40.equals(orderHeader.getOrderStatus())) {
                signedOrderQty += 1;
            }
        }
        rs.setToAuditOrderQty(toAuditOrderQty);
        rs.setToReceiveOrderQty(toReceiveOrderQty);
        rs.setToDispatchOrderQty(toDispatchOrderQty);
        rs.setToSignOrderQty(toSignOrderQty);
        rs.setSignedOrderQty(signedOrderQty);
        return rs;
    }

    /**
     * 描述：保存
     */
    public ResultMessage saveEntity(TmTransportOrderEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmTransportOrderManager.saveEntity(entity);
            msg.setData(tmTransportOrderManager.getEntity(entity.getId()));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 描述：批量删除
     */
    public ResultMessage batchRemove(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmTransportOrderManager.removeEntity(tmTransportOrderManager.getEntity(id));
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    /**
     * 描述：批量审核
     */
    public ResultMessage batchAudit(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmTransportOrderManager.audit(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    /**
     * 描述：批量取消审核
     */
    public ResultMessage batchCancelAudit(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmTransportOrderManager.cancelAudit(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    /**
     * 描述：批量按单揽收
     */
    public ResultMessage batchReceive(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            ResultMessage message = this.receive(id);
            if (!message.isSuccess()) {
                errMsg.append(message.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    /**
     * 描述：按单揽收
     */
    public ResultMessage receive(String id) {
        ResultMessage msg = new ResultMessage("操作成功");

        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.get(id);
        if (orderHeader == null) {
            msg.setSuccess(false);
            msg.setMessage("数据已过期");
            return msg;
        }
        List<TmTransportOrderLabel> orderLabels = tmTransportOrderManager.findCanReceiveLabel(orderHeader.getTransportNo(), orderHeader.getOrgId());
        for (TmTransportOrderLabel orderLabel : orderLabels) {
            try {
                tmTransportOrderManager.receive(orderHeader.getTransportNo(), orderLabel.getLabelNo(), orderHeader.getReceiveOutletCode());
            } catch (GlobalException e) {
                msg.setSuccess(false);
                msg.setMessage(MessageFormat.format("订单[{0}]部分收货成功", orderHeader.getTransportNo()));
            }
        }
        return msg;
    }

    /**
     * 描述：签收
     */
    public ResultMessage sign(String[] ids, TmTransportSignInfo tmTransportSignInfo) {
        ResultMessage msg = new ResultMessage("操作成功");

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.get(id);
            try {
                TmTransportSignInfo signInfo = new TmTransportSignInfo();
                BeanUtils.copyProperties(tmTransportSignInfo, signInfo);
                signInfo.setTransportNo(orderHeader.getTransportNo());
                signInfo.setBaseOrgId(orderHeader.getBaseOrgId());
                signInfo.setOrgId(orderHeader.getOrgId());
                tmSignManager.signTransport(signInfo);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    /**
     * 描述：回单
     */
    public ResultMessage receipt(String[] ids, TmTransportReceiptInfo tmTransportReceiptInfo) {
        ResultMessage msg = new ResultMessage("操作成功");
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.get(id);
                TmTransportReceiptInfo receiptInfo = new TmTransportReceiptInfo();
                BeanUtils.copyProperties(tmTransportReceiptInfo, receiptInfo);
                receiptInfo.setTransportNo(orderHeader.getTransportNo());
                receiptInfo.setBaseOrgId(orderHeader.getBaseOrgId());
                receiptInfo.setOrgId(orderHeader.getOrgId());

                tmTransportOrderManager.receiptByTransport(receiptInfo);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    /**
     * 描述：复制
     */
    public ResultMessage copy(String id) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmTransportOrderManager.copy(id);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public ResultMessage cancelReceive(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.get(id);
                tmTransportOrderManager.cancelReceive(orderHeader.getTransportNo());
            } catch (GlobalException e) {
                errMsg.append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    public ResultMessage directDispatch(TmDirectDispatch tmDirectDispatch) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            String dispatchNo = tmDirectDispatchManager.directDispatch(tmDirectDispatch);
            msg.setMessage(MessageFormat.format("调度成功，生成派车单号【{0}】", dispatchNo));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 描述：查询跟踪信息
     */
    public List<TmTransportOrderTrack> findTrackList(TmTransportOrderTrack qEntity) {
        return tmTransportOrderTrackService.findList(qEntity);
    }
}
