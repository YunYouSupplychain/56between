package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.entity.TmTransportOrderSku;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderSkuEntity;
import com.yunyou.modules.tms.order.service.TmTransportOrderLabelService;
import com.yunyou.modules.tms.order.service.TmTransportOrderSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmTransportOrderSkuManager extends BaseService {
    @Autowired
    private TmTransportOrderSkuService tmTransportOrderSkuService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;

    @Transactional
    public void save(List<TmTransportOrderSkuEntity> entities) {
        for (TmTransportOrderSkuEntity entity : entities) {
            if (entity.getId() == null) {
                continue;
            }
            tmTransportOrderSkuService.save(entity);
        }
    }

    @Transactional
    public void saveAll(List<TmTransportOrderSku> entities) {
        tmTransportOrderSkuService.saveAll(entities);
    }

    @Transactional
    public void remove(TmTransportOrderSku entity) {
        if (StringUtils.isNotBlank(entity.getOwnerCode()) && StringUtils.isNotBlank(entity.getSkuCode())) {
            List<TmTransportOrderLabel> orderLabels = tmTransportOrderLabelService.findList(new TmTransportOrderLabel(entity.getTransportNo(), entity.getOwnerCode(), entity.getLineNo(), entity.getSkuCode(), entity.getOrgId()));
            for (TmTransportOrderLabel orderLabel : orderLabels) {
                tmTransportOrderLabelService.delete(orderLabel);
            }
        }
        tmTransportOrderSkuService.delete(entity);
    }

    @Transactional
    public void removeAll(List<TmTransportOrderSku> entities) {
        for (TmTransportOrderSku entity : entities) {
            if (entity.getId() == null) {
                continue;
            }
            this.remove(entity);
        }
    }
}
