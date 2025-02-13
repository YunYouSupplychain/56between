package com.yunyou.modules.tms.order.action;

import java.util.List;

import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.TmAttachementDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderEntity;
import com.yunyou.modules.tms.order.manager.TmRepairOrderManager;

@Service
public class TmRepairOrderAction extends BaseAction {
    @Autowired
    private TmRepairOrderManager tmRepairOrderManager;

    public TmRepairOrderEntity getEntity(String id) {
        return tmRepairOrderManager.getEntity(id);
    }

    public Page<TmRepairOrderEntity> findPage(Page<TmRepairOrderEntity> page, TmRepairOrderEntity qEntity) {
        return tmRepairOrderManager.findPage(page, qEntity);
    }

    public List<TmRepairOrderDetailEntity> findDetailList(TmRepairOrderDetailEntity qEntity) {
        return tmRepairOrderManager.findDetailList(qEntity);
    }

    public ResultMessage batchRemove(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (ids == null || ids.length <= 0) {
            return msg;
        }
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmRepairOrderManager.removeEntity(tmRepairOrderManager.getEntity(id));
            } catch (TmsException e) {
                errMsg.append(e.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    public ResultMessage saveForUnRepair(TmRepairOrderEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            msg.setData(tmRepairOrderManager.saveForUnRepair(entity));
        } catch (TmsException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public ResultMessage saveForRepair(TmRepairOrderEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            msg.setData(tmRepairOrderManager.saveForRepair(entity));
        } catch (TmsException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public ResultMessage saveDetail(List<TmRepairOrderDetailEntity> tmRepairOrderDetailList) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (CollectionUtil.isEmpty(tmRepairOrderDetailList)) {
            return msg;
        }
        StringBuilder errMsg = new StringBuilder();
        for (TmRepairOrderDetailEntity entity : tmRepairOrderDetailList) {
            if (entity.getId() == null) continue;
            try {
                tmRepairOrderManager.saveDetail(entity);
            } catch (TmsException e) {
                errMsg.append(e.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    public ResultMessage removeDetail(String repairNo, String[] detailIds) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (detailIds == null || detailIds.length <= 0) {
            return msg;
        }
        StringBuilder errMsg = new StringBuilder();
        for (String detailId : detailIds) {
            try {
                tmRepairOrderManager.removeDetail(repairNo, detailId);
            } catch (TmsException e) {
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
     * 描述：分页查询图片信息
     */
    public Page<TmAttachementDetail> findImgPage(Page<TmAttachementDetail> page, TmAttachementDetail entity) {
        entity.setOrderType(TmsConstants.IMP_UPLOAD_TYPE_REPAIR);
        return tmRepairOrderManager.findImgPage(page, entity);
    }

}
