package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderEntity;
import com.yunyou.modules.tms.order.manager.TmPreTransportOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：运输订单业务处理类(非事务管理)
 */
@Service
public class TmPreTransportOrderAction extends BaseAction {
    @Autowired
    private TmPreTransportOrderManager tmTransportOrderManager;

    /**
     * 描述：根据派车单ID查询运输订单实体信息
     */
    public TmPreTransportOrderEntity getEntity(String id) {
        return tmTransportOrderManager.getEntity(id);
    }

    /**
     * 描述：分页查询运输订单实体信息
     */
    public Page<TmPreTransportOrderEntity> findPage(Page<TmPreTransportOrderEntity> page, TmPreTransportOrderEntity qEntity) {
        return tmTransportOrderManager.findPage(page, qEntity);
    }

    /**
     * 描述：保存
     */
    public ResultMessage saveEntity(TmPreTransportOrderEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            msg.setData(tmTransportOrderManager.saveEntity(entity));
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
}
