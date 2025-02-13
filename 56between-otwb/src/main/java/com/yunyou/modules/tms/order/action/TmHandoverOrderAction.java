package com.yunyou.modules.tms.order.action;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.TmAttachementDetail;
import com.yunyou.modules.tms.order.entity.TmHandoverOrderHeader;
import com.yunyou.modules.tms.order.entity.TmHandoverOrderSku;
import com.yunyou.modules.tms.order.entity.extend.TmHandoverOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmHandoverOrderLabelEntity;
import com.yunyou.modules.tms.order.entity.extend.TmHandoverOrderSkuEntity;
import com.yunyou.modules.tms.order.manager.TmHandoverOrderManager;
import com.yunyou.modules.tms.order.service.TmHandoverOrderHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


/**
 * 描述：交接单业务处理类(非事务管理)
 */
@Service
public class TmHandoverOrderAction extends BaseAction {
    @Autowired
    private TmHandoverOrderManager tmHandoverOrderManager;
    @Autowired
    private TmHandoverOrderHeaderService tmHandoverOrderHeaderService;

    /**
     * 描述：分页查询交接单实体信息
     */
    public Page<TmHandoverOrderEntity> findPage(Page<TmHandoverOrderEntity> page, TmHandoverOrderEntity entity) {
        return tmHandoverOrderManager.findPage(page, entity);
    }

    /**
     * 描述：分页查询交接单标签信息
     */
    public Page<TmHandoverOrderLabelEntity> findLabelPage(Page<TmHandoverOrderLabelEntity> page, TmHandoverOrderLabelEntity entity) {
        return tmHandoverOrderManager.findLabelPage(page, entity);
    }

    /**
     * 描述：分页查询交接单商品信息
     */
    public Page<TmHandoverOrderSkuEntity> findSkuPage(Page<TmHandoverOrderSkuEntity> page, TmHandoverOrderSkuEntity entity) {
        return tmHandoverOrderManager.findSkuPage(page, entity);
    }

    /**
     * 描述：分页查询交接单图片信息
     */
    public Page<TmAttachementDetail> findImgPage(Page<TmAttachementDetail> page, TmAttachementDetail entity) {
        entity.setOrderType(TmsConstants.IMP_UPLOAD_TYPE_HANDOVER);
        return tmHandoverOrderManager.findImgPage(page, entity);
    }

    /**
     * 描述：根据交接单ID查询交接单实体信息
     */
    public TmHandoverOrderEntity getEntity(String id) {
        return tmHandoverOrderManager.getEntity(id);
    }

    /**
     * 描述：保存交接单信息
     */
    public ResultMessage saveEntity(TmHandoverOrderEntity entity) {
        ResultMessage msg = new ResultMessage();
        try {
            TmHandoverOrderEntity tmHandoverOrderEntity = tmHandoverOrderManager.saveOrder(entity);
            msg.setData(tmHandoverOrderEntity);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setMessage(StringUtils.isNotBlank(msg.getMessage()) ? msg.getMessage() : "操作成功");
        return msg;
    }

    /**
     * 描述：删除派车单信息
     */
    public ResultMessage removeEntity(String id) {
        ResultMessage msg = new ResultMessage();
        try {
            TmHandoverOrderEntity entity = tmHandoverOrderManager.getEntity(id);
            tmHandoverOrderManager.removeOrder(entity);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        msg.setMessage(StringUtils.isNotBlank(msg.getMessage()) ? msg.getMessage() : "操作成功");
        return msg;
    }

    /**
     * 描述：批量删除派车单信息
     */
    public ResultMessage batchRemove(String[] ids, boolean isUnifiedTransaction) {
        ResultMessage msg = new ResultMessage();
        if (ids == null || ids.length <= 0) {return msg;}

        StringBuilder errMsg = new StringBuilder();
        if (isUnifiedTransaction) {// 统一事务控制(全部成功或者全部失败)
            try {
                tmHandoverOrderManager.batchRemoveOrder(Arrays.asList(ids));
            } catch (GlobalException e) {
                errMsg.append(e.getMessage()).append("<br>");
            }
        } else {
            for (String id : ids) {
                ResultMessage message = this.removeEntity(id);
                if (!message.isSuccess()) {
                    errMsg.append(message.getMessage()).append("<br>");
                }
            }
        }
        msg.setMessage(StringUtils.isNotBlank(errMsg) ? errMsg.toString() : "操作成功");
        return msg;
    }

    /**
     * 批量保存商品明细
     */
    public ResultMessage saveSkuList(List<TmHandoverOrderSku> detailEntityList, User user) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmHandoverOrderManager.batchSaveSku(detailEntityList, user);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 修改商品明细
     */
    public ResultMessage skuEdit(TmHandoverOrderSku tmHandoverOrderSku, User user) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmHandoverOrderManager.saveSku(tmHandoverOrderSku, user);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 交接单交接
     *
     * @param ids 交接单ID
     */
    public ResultMessage handover(String[] ids) {
        ResultMessage msg = new ResultMessage();
        if (ids == null || ids.length <= 0) {
            return msg;
        }

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            TmHandoverOrderHeader tmHandoverOrderHeader = tmHandoverOrderHeaderService.get(id);
            try {
                tmHandoverOrderManager.handover(tmHandoverOrderHeader);
            } catch (GlobalException e) {
                errMsg.append(tmHandoverOrderHeader.getHandoverNo()).append(e.getMessage()).append("<br>");
            } catch (Exception e) {
                logger.error("交接单交接：id=" + id, e);
                errMsg.append(tmHandoverOrderHeader.getHandoverNo()).append("交接异常<br>");
            }
        }
        msg.setMessage(StringUtils.isNotBlank(errMsg) ? errMsg.toString() : "操作成功");
        return msg;
    }
}
