package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmVehicleSafetyCheck;
import com.yunyou.modules.tms.order.mapper.TmVehicleSafetyCheckMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出车安全检查表Service
 * @author ZYF
 * @version 2020-07-15
 */
@Service
@Transactional(readOnly = true)
public class TmVehicleSafetyCheckService extends CrudService<TmVehicleSafetyCheckMapper, TmVehicleSafetyCheck> {

}