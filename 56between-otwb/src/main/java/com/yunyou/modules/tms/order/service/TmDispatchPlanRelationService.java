package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDispatchPlanRelation;
import com.yunyou.modules.tms.order.mapper.TmDispatchPlanRelationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 调度计划需求计划Service
 * @author WMJ
 * @version 2020-07-07
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchPlanRelationService extends CrudService<TmDispatchPlanRelationMapper, TmDispatchPlanRelation> {

    public List<String> findDemandByDispatch(String dispatchPlanId) {
        return mapper.findDemandByDispatch(dispatchPlanId);
    }

    @Transactional
    public void deleteByDispatch(String dispatchPlanId) {
        mapper.deleteByDispatch(dispatchPlanId);
    }

}