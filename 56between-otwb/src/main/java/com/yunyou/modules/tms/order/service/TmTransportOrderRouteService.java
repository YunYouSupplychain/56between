package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.mapper.TmTransportOrderRouteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 运输订单路由信息Service
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Service
@Transactional(readOnly = true)
public class TmTransportOrderRouteService extends CrudService<TmTransportOrderRouteMapper, TmTransportOrderRoute> {

    public TmTransportOrderRoute getByTransportNoAndLabelNo(String transportNo, String labelNo, String baseOrgId) {
        return mapper.getByTransportNoAndLabel(transportNo, labelNo, baseOrgId);
    }
}