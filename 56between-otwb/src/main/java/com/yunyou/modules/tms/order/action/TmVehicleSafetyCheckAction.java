package com.yunyou.modules.tms.order.action;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.ResultMessage;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.extend.TmVehicleSafetyCheckEntity;
import com.yunyou.modules.tms.order.manager.TmVehicleSafetyCheckManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 出车安全检查表业务处理类(非事务管理)
 */
@Service
public class TmVehicleSafetyCheckAction extends BaseAction {
    @Autowired
    private TmVehicleSafetyCheckManager tmVehicleSafetyCheckManager;

    public TmVehicleSafetyCheckEntity getEntity(String id) {
        return tmVehicleSafetyCheckManager.getEntity(id);
    }

    public List<TmVehicleSafetyCheckEntity> findList(TmVehicleSafetyCheckEntity entity) {
        return tmVehicleSafetyCheckManager.findEntityList(entity);
    }

    /**
     * 描述：分页查询
     */
    public Page<TmVehicleSafetyCheckEntity> findPage(Page<TmVehicleSafetyCheckEntity> page, TmVehicleSafetyCheckEntity qEntity) {
        return tmVehicleSafetyCheckManager.findPage(page, qEntity);
    }


    /**
     * 保存
     */
    public ResultMessage saveEntity(TmVehicleSafetyCheckEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            msg.setData(tmVehicleSafetyCheckManager.saveEntity(entity));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }
}
