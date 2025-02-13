package com.yunyou.modules.tms.order.action;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.extend.TmDemandPlanDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDemandPlanEntity;
import com.yunyou.modules.tms.order.manager.TmDemandPlanManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 需求计划业务处理类(非事务管理)
 */
@Service
public class TmDemandPlanAction extends BaseAction {
    @Autowired
    private TmDemandPlanManager tmDemandPlanManager;

    public TmDemandPlanEntity getEntity(String id) {
        return tmDemandPlanManager.getEntity(id);
    }

    public Page<TmDemandPlanEntity> findPage(Page<TmDemandPlanEntity> page, TmDemandPlanEntity entity) {
        return tmDemandPlanManager.findPage(page, entity);
    }

    /**
     * 保存
     */
    public ResultMessage saveEntity(TmDemandPlanEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            msg.setData(tmDemandPlanManager.saveEntity(entity));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 保存明细
     */
    public ResultMessage saveDetailList(List<TmDemandPlanDetailEntity> detailEntityList) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmDemandPlanManager.saveDetailList(detailEntityList);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 批量删除
     */
    public ResultMessage batchRemove(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmDemandPlanManager.removeEntity(tmDemandPlanManager.getEntity(id));
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

    public List<TmDemandPlanEntity> findHeaderList(TmDemandPlanEntity entity) {
        return tmDemandPlanManager.findHeaderList(entity);
    }

    public List<TmDemandPlanDetailEntity> findDetailList(TmDemandPlanDetailEntity entity) {
        return tmDemandPlanManager.findDetailList(entity);
    }

    /**
     * 批量删除明细
     */
    public ResultMessage batchRemoveDetails(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmDemandPlanManager.removeDetail(id);
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
