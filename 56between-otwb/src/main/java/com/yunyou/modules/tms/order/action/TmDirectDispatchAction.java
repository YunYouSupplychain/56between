package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.TmDirectDispatch;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderLabelEntity;
import com.yunyou.modules.tms.order.manager.TmDirectDispatchManager;
import com.yunyou.modules.tms.order.manager.TmTransportOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：调度派车业务处理类(非事务管理)
 */
@Service
public class TmDirectDispatchAction extends BaseAction {

    @Autowired
    private TmDirectDispatchManager manager;
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;

    /**
     * 描述：校验待调度的运输订单
     */
    public ResultMessage checkOrders(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            msg.setData(manager.checkOrders(ids));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public Page<TmTransportOrderLabelEntity> findPage(Page<TmTransportOrderLabelEntity> page, TmTransportOrderLabelEntity qEntity) {
        return tmTransportOrderManager.findDirectLabelPage(page, qEntity);
    }

    /**
     * 描述：校验调度车牌信息
     */
    public ResultMessage checkVehicle(TmDirectDispatch entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            msg.setData(manager.checkVehicle(entity));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 描述：新建调度派车
     */
    public ResultMessage directDispatch(TmDirectDispatch entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            manager.directDispatchNew(entity);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }
}
