package com.yunyou.modules.tms.order.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderSku;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderSkuEntity;
import com.yunyou.modules.tms.order.manager.TmPreTransportOrderManager;
import com.yunyou.modules.tms.order.service.TmPreTransportOrderSkuService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TmPreTransportOrderSkuAction extends BaseAction {
    @Autowired
    private TmPreTransportOrderSkuService tmPreTransportOrderSkuService;
    @Autowired
    private TmPreTransportOrderManager tmPreTransportOrderManager;

    public Page<TmPreTransportOrderSkuEntity> findPage(Page page, TmPreTransportOrderSkuEntity qEntity) {
        return tmPreTransportOrderManager.findPage(page, qEntity);
    }

    public List<TmPreTransportOrderSkuEntity> findList(TmPreTransportOrderSkuEntity qEntity) {
        return tmPreTransportOrderManager.findList(qEntity);
    }

    public ResultMessage saveAll(List<TmPreTransportOrderSkuEntity> entities) {
        ResultMessage msg = new ResultMessage("操作成功");
        try {
            tmPreTransportOrderSkuService.save(entities);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }

    public ResultMessage removeAll(String[] ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (ids == null || ids.length <= 0) {
            return msg;
        }
        List<TmPreTransportOrderSku> orderSkus = Lists.newArrayList();
        try {
            for (String id : ids) {
                orderSkus.add(tmPreTransportOrderSkuService.get(id));
            }
            tmPreTransportOrderSkuService.remove(orderSkus);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }
        return msg;
    }
}
