package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmDispatchPlanRelation;

import java.util.List;

/**
 * 调度计划需求计划关系MAPPER接口
 * @author WMJ
 * @version 2020-07-07
 */
@MyBatisMapper
public interface TmDispatchPlanRelationMapper extends BaseMapper<TmDispatchPlanRelation> {
    List<String> findDemandByDispatch(String dispatchPlanId);
    void deleteByDispatch(String dispatchPlanId);
}