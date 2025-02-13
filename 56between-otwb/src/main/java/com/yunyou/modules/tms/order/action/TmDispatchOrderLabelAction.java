package com.yunyou.modules.tms.order.action;

import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderLabelEntity;
import com.yunyou.modules.tms.order.manager.TmDispatchOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TmDispatchOrderLabelAction extends BaseAction {
    @Autowired
    private TmDispatchOrderManager tmDispatchOrderManager;

    public Page<TmDispatchOrderLabelEntity> findPage(Page<TmDispatchOrderLabelEntity> page, TmDispatchOrderLabelEntity qEntity) {
        return tmDispatchOrderManager.findPage(page, qEntity);
    }
}
