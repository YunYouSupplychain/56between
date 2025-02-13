package com.yunyou.modules.tms.order.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmTransportOrderCost;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderCostEntity;
import com.yunyou.modules.tms.order.mapper.TmTransportOrderCostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输订单费用信息Service
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Service
@Transactional(readOnly = true)
public class TmTransportOrderCostService extends CrudService<TmTransportOrderCostMapper, TmTransportOrderCost> {

    @Override
    @Transactional
    public void save(TmTransportOrderCost entity) {
        if (StringUtils.isBlank(entity.getTransportNo())) {
            throw new TmsException("运输单号不能为空");
        }
        super.save(entity);
    }

    @Transactional
    public void save(List<TmTransportOrderCostEntity> entities) {
        for (TmTransportOrderCostEntity entity : entities) {
            if (entity.getId() == null) {
                continue;
            }
            this.save(entity);
        }
    }

}