package com.yunyou.modules.tms.order.manager.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmDemandPlanHeader;
import com.yunyou.modules.tms.order.entity.extend.*;

import java.util.List;

@MyBatisMapper
public interface TmDemandPlanMapper extends BaseMapper<TmDemandPlanHeader> {

    List<TmDemandPlanEntity> findPage(TmDemandPlanEntity entity);

    List<TmDemandPlanEntity> findHeaderList(TmDemandPlanEntity entity);

    List<TmDemandPlanDetailEntity> findDetailList(TmDemandPlanDetailEntity entity);

    TmDemandPlanEntity getEntity(String id);

}
