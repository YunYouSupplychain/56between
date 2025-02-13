package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderCost;
import com.yunyou.modules.tms.order.mapper.TmPreTransportOrderCostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 运输订单费用信息Service
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Service
@Transactional(readOnly = true)
public class TmPreTransportOrderCostService extends CrudService<TmPreTransportOrderCostMapper, TmPreTransportOrderCost> {

}