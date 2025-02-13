package com.yunyou.modules.tms.order.action;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.order.entity.TmAttachementDetail;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillEntity;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillFeeEntity;
import com.yunyou.modules.tms.order.manager.TmExceptionHandleBillManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 异常处理单处理类(非事务管理)
 */
@Service
public class TmExceptionHandleBillAction extends BaseAction {
    @Autowired
    private TmExceptionHandleBillManager tmExceptionHandleBillManager;

    public TmExceptionHandleBillEntity getEntity(String id) {
        return tmExceptionHandleBillManager.getEntity(id);
    }

    public Page<TmExceptionHandleBillEntity> findPage(Page<TmExceptionHandleBillEntity> page, TmExceptionHandleBillEntity entity) {
        return tmExceptionHandleBillManager.findPage(page, entity);
    }

    /**
     * 保存
     */
    public ResultMessage saveEntity(TmExceptionHandleBillEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            msg.setData(tmExceptionHandleBillManager.saveEntity(entity));
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
                tmExceptionHandleBillManager.removeEntity(tmExceptionHandleBillManager.getEntity(id));
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

    public List<TmExceptionHandleBillDetailEntity> findDetailList(TmExceptionHandleBillDetailEntity entity) {
        return tmExceptionHandleBillManager.findDetailList(entity);
    }

    /**
     * 保存明细
     */
    public ResultMessage saveDetailList(List<TmExceptionHandleBillDetailEntity> detailEntityList) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmExceptionHandleBillManager.saveDetailList(detailEntityList);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 批量删除明细
     */
    public ResultMessage batchRemoveDetails(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmExceptionHandleBillManager.removeDetail(id);
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

    public List<TmExceptionHandleBillFeeEntity> findFeeDetailList(TmExceptionHandleBillFeeEntity entity) {
        return tmExceptionHandleBillManager.findFeeDetailList(entity);
    }

    /**
     * 保存费用明细
     */
    public ResultMessage saveFeeDetailList(List<TmExceptionHandleBillFeeEntity> feeEntityList) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmExceptionHandleBillManager.saveFeeDetailList(feeEntityList);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 批量删除费用明细
     */
    public ResultMessage batchRemoveFeeDetails(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmExceptionHandleBillManager.removeFeeDetail(id);
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

    public ResultMessage saveImgDetail(String billNo, String orgId, String fileName, String imgFilePath, String imgUrl) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmExceptionHandleBillManager.saveImgDetail(billNo, orgId, fileName, imgFilePath, imgUrl, UserUtils.getUser());
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public Page<TmAttachementDetail> findPicPage(Page<TmAttachementDetail> page, TmAttachementDetail entity) {
        return tmExceptionHandleBillManager.findPicPage(page, entity);
    }
}
