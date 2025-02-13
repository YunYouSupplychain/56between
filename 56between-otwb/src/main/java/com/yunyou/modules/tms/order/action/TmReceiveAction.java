package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.extend.TmReceiveEntity;
import com.yunyou.modules.tms.order.manager.TmReceiveManager;
import com.yunyou.modules.tms.order.service.TmTransportOrderTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：网点收货业务处理类(非事务管理)
 */
@Service
public class TmReceiveAction extends BaseAction {
    @Autowired
    private TmReceiveManager tmReceiveManager;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;
    @Autowired
    TmTransportObjService tmTransportObjService;

    /**
     * 描述：分页查询派车单实体信息
     */
    public Page<TmReceiveEntity> findPage(Page<TmReceiveEntity> page, TmReceiveEntity entity) {
        return tmReceiveManager.findPage(page, entity);
    }

    /**
     * 描述：批量按条件单收货
     */
    public ResultMessage receiveCondition(TmReceiveEntity entity) {
        return this.receive(this.findPage(new Page<>(), entity).getList());
    }

    /**
     * 描述：批量按标签收货
     */
    public ResultMessage receive(List<TmReceiveEntity> list) {
        ResultMessage msg = new ResultMessage("操作成功");
        StringBuilder errMsg = new StringBuilder();
        for (TmReceiveEntity entity : list) {
            try {
                tmReceiveManager.receiveByLabel(entity.getTransportNo(), entity.getLabelNo(), entity.getNextOutletCode());
                tmTransportOrderTrackService.saveTrackNode(entity.getTransportNo(), entity.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_ARRIVE, UserUtils.getUser());
            } catch (GlobalException e) {
                errMsg.append("<br>").append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }


}
