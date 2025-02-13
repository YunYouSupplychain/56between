package com.yunyou.modules.tms.order.manager.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmDispatchPlanHeader;
import com.yunyou.modules.tms.order.entity.extend.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmDispatchPlanMapper extends BaseMapper<TmDispatchPlanHeader> {

    TmDispatchPlanEntity getEntity(String id);

    TmDispatchPlanConfigEntity getConfigEntity(String id);

    List<TmDispatchPlanEntity> findPage(TmDispatchPlanEntity entity);

    List<TmDispatchPlanEntity> findHeaderList(TmDispatchPlanEntity entity);

    List<TmDispatchPlanDetailEntity> findDetailList(TmDispatchPlanDetailEntity entity);

    List<TmDispatchPlanDetailEntity> findDetailGrid(TmDispatchPlanDetailEntity qEntity);

    List<TmDispatchPlanConfigEntity> findConfigList(TmDispatchPlanConfigEntity entity);

    List<String> findVehicleByPlanNo(@Param("planNo") String planNo, @Param("orgId") String orgId);

    List<TmDispatchPlanVehicleEntity> getVehicle(TmDispatchPlanVehicleEntity entity);

    List<TmDispatchPlanVehicleEntity> getVehicleGrid(TmDispatchPlanVehicleEntity qEntity);

    void deleteDetailByPlanNo(@Param("planNo") String planNo, @Param("orgId") String orgId);

    void deleteConfigByPlanNo(@Param("planNo") String planNo, @Param("orgId") String orgId);

    void deleteVehicleByPlanNo(@Param("planNo") String planNo, @Param("orgId") String orgId);
}
