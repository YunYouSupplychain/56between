package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderLabelEntity;
import com.yunyou.modules.tms.order.manager.TmTransportOrderLabelManager;
import com.yunyou.modules.tms.order.manager.TmTransportOrderManager;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderLabelService;
import com.yunyou.common.utils.number.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TmTransportOrderLabelAction extends BaseAction {
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;
    @Autowired
    private TmTransportOrderLabelManager tmTransportOrderLabelManager;

    public Page<TmTransportOrderLabelEntity> findPage(Page<TmTransportOrderLabelEntity> page, TmTransportOrderLabelEntity qEntity) {
        return tmTransportOrderManager.findPage(page, qEntity);
    }

    public List<TmTransportOrderLabel> findList(TmTransportOrderLabel qEntity) {
        return tmTransportOrderLabelService.findList(qEntity);
    }

    public ResultMessage addLabel(String[] ids, String lineNo, String skuCode, long labelQty, double totalQty, double totalWeight, double totalCubic) {
        ResultMessage msg = new ResultMessage();
        if (labelQty <= 0) {
            msg.setSuccess(false);
            msg.setMessage("标签数量必须大于0");
            return msg;
        }
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.get(id);
                // 平摊计算每个标签的重量
                double qty = BigDecimalUtil.div(totalQty, labelQty, 4, BigDecimal.ROUND_HALF_UP);
                double weight = BigDecimalUtil.div(totalWeight, labelQty, 4, BigDecimal.ROUND_HALF_UP);
                double cubic = BigDecimalUtil.div(totalCubic, labelQty, 4, BigDecimal.ROUND_HALF_UP);
                while ((labelQty--) > 0) {
                    tmTransportOrderManager.addLabel(orderHeader.getTransportNo(), lineNo, null, qty, weight, cubic);
                }
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            }
        }
        msg.setMessage(StringUtils.isNotBlank(errMsg) ? errMsg.toString() : "操作成功");
        return msg;
    }

    public ResultMessage removeLabel(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmTransportOrderLabelManager.remove(tmTransportOrderLabelService.get(id));
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
}
