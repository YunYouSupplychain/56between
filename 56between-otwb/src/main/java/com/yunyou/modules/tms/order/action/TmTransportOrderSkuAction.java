package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderSku;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderSkuEntity;
import com.yunyou.modules.tms.order.manager.TmTransportOrderManager;
import com.yunyou.modules.tms.order.manager.TmTransportOrderSkuManager;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderSkuService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TmTransportOrderSkuAction extends BaseAction {
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderSkuService tmTransportOrderSkuService;
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;
    @Autowired
    private TmTransportOrderSkuManager tmTransportOrderSkuManager;

    public Page<TmTransportOrderSkuEntity> findPage(Page page, TmTransportOrderSkuEntity qEntity) {
        return tmTransportOrderManager.findPage(page, qEntity);
    }

    public List<TmTransportOrderSkuEntity> findList(TmTransportOrderSkuEntity qEntity) {
        return tmTransportOrderManager.findList(qEntity);
    }

    public ResultMessage saveAll(List<TmTransportOrderSkuEntity> entities) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmTransportOrderSkuManager.save(entities);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public ResultMessage removeAll(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (ids == null || ids.length <= 0) {
            return msg;
        }
        List<TmTransportOrderSku> orderSkus = Lists.newArrayList();
        try {
            for (String id : ids) {
                orderSkus.add(tmTransportOrderSkuService.get(id));
            }
            List<String> transportNos = orderSkus.stream().map(TmTransportOrderSku::getTransportNo).distinct().collect(Collectors.toList());
            for (String transportNo : transportNos) {
                TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.getByNo(transportNo);
                if (orderHeader == null) {
                    throw new TmsException(MessageFormat.format("{0}无效的运输订单", transportNo));
                }
                if (!TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(orderHeader.getOrderStatus())) {
                    throw new TmsException(MessageFormat.format("{0}运输订单非新建状态，无法操作", transportNo));
                }
            }
            tmTransportOrderSkuManager.removeAll(orderSkus);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }
}
