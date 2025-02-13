package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmVehicle;
import com.yunyou.modules.tms.basic.entity.extend.TmVehicleEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 车辆信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmVehicleMapper extends BaseMapper<TmVehicle> {

    TmVehicleEntity getEntity(String id);

    List<TmVehicleEntity> findPage(TmVehicle tmVehicle);

    List<TmVehicleEntity> findGrid(TmVehicleEntity entity);

    TmVehicle getByNo(@Param("carNo") String carNo, @Param("orgId") String orgId);

    TmVehicleEntity getEntityByNo(@Param("carNo") String carNo, @Param("orgId") String orgId);

    void remove(@Param("carNo") String carNo, @Param("orgId") String orgId);
}