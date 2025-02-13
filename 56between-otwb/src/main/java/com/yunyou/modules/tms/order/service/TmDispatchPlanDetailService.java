package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDispatchPlanDetail;
import com.yunyou.modules.tms.order.mapper.TmDispatchPlanDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 调度计划明细Service
 * @author WMJ
 * @version 2020-07-07
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchPlanDetailService extends CrudService<TmDispatchPlanDetailMapper, TmDispatchPlanDetail> {

}