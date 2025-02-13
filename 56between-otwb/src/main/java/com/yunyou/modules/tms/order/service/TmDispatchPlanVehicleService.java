package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDispatchPlanVehicle;
import com.yunyou.modules.tms.order.mapper.TmDispatchPlanVehicleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 调度计划车辆Service
 * @author WMJ
 * @version 2020-07-07
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchPlanVehicleService extends CrudService<TmDispatchPlanVehicleMapper, TmDispatchPlanVehicle> {

}