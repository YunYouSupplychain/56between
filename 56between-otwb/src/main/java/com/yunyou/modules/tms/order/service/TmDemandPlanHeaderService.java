package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDemandPlanHeader;
import com.yunyou.modules.tms.order.mapper.TmDemandPlanHeaderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 需求计划单头Service
 * @author WMJ
 * @version 2020-07-07
 */
@Service
@Transactional(readOnly = true)
public class TmDemandPlanHeaderService extends CrudService<TmDemandPlanHeaderMapper, TmDemandPlanHeader> {

}