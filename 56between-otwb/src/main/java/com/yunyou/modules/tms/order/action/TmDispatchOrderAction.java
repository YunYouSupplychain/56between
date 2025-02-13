package com.yunyou.modules.tms.order.action;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.interfaces.gps.GpsNewestLocationInfo;
import com.yunyou.modules.tms.common.map.geo.Point;
import com.yunyou.modules.tms.order.entity.TmCarrierFreight;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderLabelEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchVehicleEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderEntity;
import com.yunyou.modules.tms.order.manager.TmDispatchOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 描述：派车单业务处理类(非事务管理)
 */
@Service
public class TmDispatchOrderAction extends BaseAction {
    @Autowired
    private TmDispatchOrderManager tmDispatchOrderManager;

    /**
     * 描述：根据派车单ID查询派车单实体信息
     */
    public TmDispatchOrderEntity getEntity(String id) {
        return tmDispatchOrderManager.getEntity(id);
    }

    /**
     * 描述：分页查询派车单实体信息
     */
    public Page<TmDispatchOrderEntity> findPage(Page<TmDispatchOrderEntity> page, TmDispatchOrderEntity entity) {
        return tmDispatchOrderManager.findPage(page, entity);
    }

    /**
     * 描述：分页查询派车单标签信息
     */
    public Page<TmTransportOrderEntity> findTransportPage(Page<TmDispatchOrderLabelEntity> page, TmDispatchOrderLabelEntity entity) {
        return tmDispatchOrderManager.findTransportPage(page, entity);
    }

    /**
     * 描述：保存派车单信息
     */
    public ResultMessage saveEntity(TmDispatchOrderEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmDispatchOrderManager.saveEntity(entity);
            msg.setData(tmDispatchOrderManager.getEntity(entity.getId()));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 描述：删除派车单信息
     */
    public ResultMessage removeEntity(String id) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmDispatchOrderManager.removeEntity(tmDispatchOrderManager.getEntity(id));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 描述：批量删除派车单信息
     */
    public ResultMessage batchRemove(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (ids == null || ids.length <= 0) return msg;

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            ResultMessage message = this.removeEntity(id);
            if (!message.isSuccess()) {
                errMsg.append(message.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            msg.setSuccess(false);
            msg.setMessage(errMsg.toString());
        }
        return msg;
    }

    /**
     * 描述：批量审核派车单信息
     */
    public ResultMessage batchAudit(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (ids == null || ids.length <= 0) return msg;

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmDispatchOrderManager.audit(id);
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
     * 描述：批量取消审核派车单信息
     */
    public ResultMessage batchCancelAudit(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (ids == null || ids.length <= 0) return msg;

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmDispatchOrderManager.cancelAudit(id);
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
     * 描述：派车单发车
     */
    public ResultMessage depart(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (ids == null || ids.length <= 0) return msg;

        StringBuilder errMsg = new StringBuilder();
        for (String id : ids) {
            try {
                tmDispatchOrderManager.depart(id);
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

    public ResultMessage copy(String id) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmDispatchOrderManager.copy(id);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    /**
     * 描述：根据派车单ID查询派车单实体信息
     */
    public TmDispatchOrderEntity getByNo(String dispatchNo, String orgId) {
        TmDispatchOrderEntity con = new TmDispatchOrderEntity();
        con.setDispatchNo(dispatchNo);
        con.setOrgId(orgId);
        List<TmDispatchOrderEntity> entityList = tmDispatchOrderManager.findEntityList(con);
        return CollectionUtil.isNotEmpty(entityList) ? entityList.get(0) : null;
    }

    public Page<TmCarrierFreight> findCarrierFreight(Page<TmCarrierFreight> page, TmCarrierFreight qEntity) {
        return tmDispatchOrderManager.findCarrierFreight(page, qEntity);
    }

    /**
     * 描述：根据 运输订单 分页查询 派车单实体 信息
     */
    public Page<TmDispatchOrderEntity> transportCheckDispatchPage(Page<TmDispatchOrderEntity> page, TmDispatchOrderEntity entity) {
        return tmDispatchOrderManager.transportCheckDispatchPage(page, entity);
    }

    /**
     * 描述：查询运输订单相关派车单车辆行驶轨迹
     */
    public Map<String, Point[]> findVehicleTracks(String transportNo, String baseOrgId, String orgId) {
        try {
            return tmDispatchOrderManager.findVehicleTracks(transportNo, baseOrgId, orgId);
        } catch (Exception e) {
            logger.error("获取G7车辆行驶轨迹数据异常", e);
            return Maps.newHashMap();
        }
    }

    /**
     * 描述：查询运输订单相关派车单车辆当前位置
     */
    public List<GpsNewestLocationInfo> findVehicleLocation(String transportNo, String baseOrgId, String orgId) {
        try {
            return tmDispatchOrderManager.findVehicleLocation(transportNo, baseOrgId, orgId);
        } catch (Exception e) {
            logger.error("获取G7车辆当前位置数据异常", e);
            return Lists.newArrayList();
        }
    }

    public List<TmDispatchVehicleEntity> findRunningVehicle(String orgId) {
        return tmDispatchOrderManager.findRunningVehicle(orgId);
    }
}
