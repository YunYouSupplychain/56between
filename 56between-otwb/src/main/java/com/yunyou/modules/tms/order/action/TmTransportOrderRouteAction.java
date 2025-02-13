package com.yunyou.modules.tms.order.action;

import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderRouteEntity;
import com.yunyou.modules.tms.order.manager.TmTransportOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TmTransportOrderRouteAction extends BaseAction {
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;

    public Page<TmTransportOrderRouteEntity> findPage(Page<TmTransportOrderRouteEntity> page, TmTransportOrderRouteEntity qEntity) {
        return tmTransportOrderManager.findPage(page, qEntity);
    }
}
