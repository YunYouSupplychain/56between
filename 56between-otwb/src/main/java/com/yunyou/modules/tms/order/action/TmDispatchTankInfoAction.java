package com.yunyou.modules.tms.order.action;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.ResultMessage;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchTankInfoEntity;
import com.yunyou.modules.tms.order.manager.TmDispatchTankInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 派车装罐信息处理类(非事务管理)
 */
@Service
public class TmDispatchTankInfoAction extends BaseAction {
    @Autowired
    private TmDispatchTankInfoManager tmDispatchTankInfoManager;

    public TmDispatchTankInfoEntity getEntity(String id) {
        return tmDispatchTankInfoManager.getEntity(id);
    }

    public List<TmDispatchTankInfoEntity> findList(TmDispatchTankInfoEntity entity) {
        return tmDispatchTankInfoManager.findEntityList(entity);
    }

    /**
     * 保存
     */
    public ResultMessage saveEntity(TmDispatchTankInfoEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            msg.setData(tmDispatchTankInfoManager.saveEntity(entity));
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }
}
