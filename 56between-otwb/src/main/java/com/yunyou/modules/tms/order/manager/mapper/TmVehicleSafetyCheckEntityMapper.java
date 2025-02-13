package com.yunyou.modules.tms.order.manager.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmVehicleSafetyCheck;
import com.yunyou.modules.tms.order.entity.extend.TmVehicleSafetyCheckEntity;

import java.util.List;

@MyBatisMapper
public interface TmVehicleSafetyCheckEntityMapper extends BaseMapper<TmVehicleSafetyCheck> {

    List<TmVehicleSafetyCheckEntity> findPage(TmVehicleSafetyCheckEntity entity);

    List<TmVehicleSafetyCheckEntity> findEntityList(TmVehicleSafetyCheckEntity entity);

    TmVehicleSafetyCheckEntity getEntity(String id);

}
