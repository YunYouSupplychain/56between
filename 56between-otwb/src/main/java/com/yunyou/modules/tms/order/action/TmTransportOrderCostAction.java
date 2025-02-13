package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.TmTransportOrderCost;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderCostEntity;
import com.yunyou.modules.tms.order.manager.TmTransportOrderManager;
import com.yunyou.modules.tms.order.service.TmTransportOrderCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运输订单费用明细Action
 * @author liujianhua
 * @version 2022.8.3
 */
@Service
public class TmTransportOrderCostAction extends BaseAction {
    @Autowired
    private TmTransportOrderCostService tmTransportOrderCostService;
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;

    public Page<TmTransportOrderCostEntity> findPage(Page<TmTransportOrderCostEntity> page, TmTransportOrderCostEntity qEntity) {
        return tmTransportOrderManager.findPage(page, qEntity);
    }

    public List<TmTransportOrderCostEntity> findList(TmTransportOrderCostEntity qEntity) {
        return tmTransportOrderManager.findCostList(qEntity);
    }

    public ResultMessage saveAll(List<TmTransportOrderCostEntity> entities) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmTransportOrderCostService.save(entities);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public ResultMessage deleteAll(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (ids == null || ids.length <= 0) {
            return msg;
        }

        try {
            for (String id : ids) {
                tmTransportOrderCostService.delete(new TmTransportOrderCost(id));
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }
}
