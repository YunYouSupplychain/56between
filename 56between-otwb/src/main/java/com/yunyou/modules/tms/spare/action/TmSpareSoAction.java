package com.yunyou.modules.tms.spare.action;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoDetailEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoScanInfoEntity;
import com.yunyou.modules.tms.spare.manager.TmSpareSoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class TmSpareSoAction extends BaseAction {
    @Autowired
    private TmSpareSoManager tmSpareSoManager;

    public TmSpareSoEntity getEntity(String id) {
        return tmSpareSoManager.getEntity(id);
    }

    public List<TmSpareSoDetailEntity> findList(TmSpareSoDetailEntity qEntity) {
        return tmSpareSoManager.findList(qEntity);
    }

    public List<TmSpareSoScanInfoEntity> findList(TmSpareSoScanInfoEntity qEntity) {
        return tmSpareSoManager.findList(qEntity);
    }

    public Page<TmSpareSoEntity> findPage(Page<TmSpareSoEntity> page, TmSpareSoEntity qEntity) {
        return tmSpareSoManager.findPage(page, qEntity);
    }

    public Page<TmSpareSoScanInfoEntity> findPage(Page<TmSpareSoScanInfoEntity> page, TmSpareSoScanInfoEntity qEntity) {
        return tmSpareSoManager.findPage(page, qEntity);
    }

    public ResultMessage saveHeader(TmSpareSoEntity entity) {
        ResultMessage msg = new ResultMessage();

        try {
            entity = tmSpareSoManager.saveHeader(entity);
            msg.setData(entity);
        } catch (DuplicateKeyException e) {
            msg.setSuccess(false);
            msg.setMessage("订单已存在");
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        } catch (RuntimeException e) {
            if (logger.isErrorEnabled()) {
                logger.error("", e);
            }
            msg.setSuccess(false);
            msg.setMessage("保存异常");
        }
        return msg;
    }

    public ResultMessage saveDetail(List<TmSpareSoDetailEntity> entities) {
        ResultMessage msg = new ResultMessage();

        StringBuilder errMsg = new StringBuilder();
        for (TmSpareSoDetailEntity entity : entities) {
            if (entity.getId() == null) continue;
            try {
                tmSpareSoManager.saveDetail(entity);
            } catch (DuplicateKeyException e) {
                msg.setSuccess(false);
                msg.setMessage(MessageFormat.format("行号【{0}】，行号已存在", entity.getLineNo()));
            } catch (GlobalException e) {
                errMsg.append(MessageFormat.format("行号【{0}】，{1}", entity.getLineNo(), e.getMessage())).append("<br>");
            } catch (RuntimeException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
                errMsg.append(MessageFormat.format("行号【{0}】，操作异常", entity.getLineNo())).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        } else {
            msg.setMessage("操作成功");
        }
        return msg;
    }

    public ResultMessage remove(String[] ids) {
        ResultMessage msg = new ResultMessage();

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmSpareSoManager.remove(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (RuntimeException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
                errMsg.append("操作异常").append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        } else {
            msg.setMessage("操作成功");
        }
        return msg;
    }

    public ResultMessage removeDetail(String[] ids) {
        ResultMessage msg = new ResultMessage();

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmSpareSoManager.removeDetail(id);
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            } catch (RuntimeException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
                errMsg.append("操作异常").append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        } else {
            msg.setMessage("操作成功");
        }
        return msg;
    }
}
