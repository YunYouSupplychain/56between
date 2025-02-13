package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.extend.TmDeliverEntity;
import com.yunyou.modules.tms.order.manager.TmDeliverManager;
import com.yunyou.modules.tms.order.service.TmTransportOrderTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：网点发货处理类(非事务管理)
 */
@Service
public class TmDeliverAction extends BaseAction {
    @Autowired
    private TmDeliverManager tmDeliverManager;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;

    public Page<TmDeliverEntity> findPage(Page<TmDeliverEntity> page, TmDeliverEntity qEntity) {
        return tmDeliverManager.findPage(page, qEntity);
    }

    /**
     * 描述：发货
     */
    public ResultMessage deliver(List<TmDeliverEntity> list) {
        ResultMessage msg = new ResultMessage();

        StringBuilder errMsg = new StringBuilder();
        if (CollectionUtil.isNotEmpty(list)) {
            for (TmDeliverEntity entity : list) {
                try {
                    tmDeliverManager.deliverByLabel(entity.getDispatchNo(), entity.getTransportNo(), entity.getLabelNo(), entity.getNowOutletCode(), entity.getNextOutletCode());
                    tmTransportOrderTrackService.saveTrackNode(entity.getTransportNo(), entity.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_SHIP, UserUtils.getUser());
                } catch (GlobalException e) {
                    errMsg.append(e.getMessage()).append("<br>");
                }
            }
        }
        msg.setMessage(StringUtils.isNotBlank(errMsg) ? errMsg.toString() : "操作成功");
        return msg;
    }

    /**
     * 描述：发货按条件
     */
    public ResultMessage deliverByCondition(TmDeliverEntity qEntity) {
        return this.deliver(this.findPage(new Page<>(), qEntity).getList());
    }
}
