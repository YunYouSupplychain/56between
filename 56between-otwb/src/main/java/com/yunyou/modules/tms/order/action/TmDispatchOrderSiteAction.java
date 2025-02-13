package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderSite;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderSiteEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchSiteSelectLabelEntity;
import com.yunyou.modules.tms.order.manager.TmDispatchOrderSiteManager;
import com.yunyou.modules.tms.order.service.TmDispatchOrderSiteService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TmDispatchOrderSiteAction extends BaseAction {
    @Autowired
    private TmDispatchOrderSiteService tmDispatchOrderSiteService;
    @Autowired
    private TmDispatchOrderSiteManager tmDispatchOrderSiteManager;

    public TmDispatchOrderSiteEntity getEntity(String id) {
        return tmDispatchOrderSiteService.getEntity(id);
    }

    public List<TmDispatchOrderSiteEntity> findList(TmDispatchOrderSiteEntity qEntity) {
        return tmDispatchOrderSiteManager.findList(qEntity);
    }

    public ResultMessage saveAll(List<TmDispatchOrderSiteEntity> entities) {
        ResultMessage msg = new ResultMessage("操作成功");

        try {
            tmDispatchOrderSiteManager.save(entities);
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
        List<TmDispatchOrderSite> orderSites = Lists.newArrayList();
        try {
            for (String id : ids) {
                orderSites.add(tmDispatchOrderSiteService.get(id));
            }
            tmDispatchOrderSiteManager.remove(orderSites);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public List<TmDispatchSiteSelectLabelEntity> selectLabelForLeft(TmDispatchSiteSelectLabelEntity qEntity) {
        return tmDispatchOrderSiteManager.selectLabelForLeft(qEntity);
    }

    public List<TmDispatchSiteSelectLabelEntity> selectLabelForRight(TmDispatchSiteSelectLabelEntity qEntity) {
        return tmDispatchOrderSiteManager.selectLabelForRight(qEntity);
    }

    public ResultMessage selectLabelConfirm(List<TmDispatchSiteSelectLabelEntity> list) {
        ResultMessage msg = new ResultMessage("操作成功");

        StringBuilder errMsg = new StringBuilder();
        for (TmDispatchSiteSelectLabelEntity entity : list) {
            try {
                tmDispatchOrderSiteManager.bindSiteLabel(entity);
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

    public ResultMessage cancelSelectLabelConfirm(List<TmDispatchSiteSelectLabelEntity> list) {
        ResultMessage msg = new ResultMessage("操作成功");

        StringBuilder errMsg = new StringBuilder();
        for (TmDispatchSiteSelectLabelEntity entity : list) {
            try {
                tmDispatchOrderSiteManager.unbindSiteLabel(entity.getDispatchNo(), entity.getDispatchSiteOutletCode(), entity.getTransportNo(), entity.getLabelNo());
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
