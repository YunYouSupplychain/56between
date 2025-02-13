package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsVehicle;
import com.yunyou.modules.sys.common.entity.extend.SysTmsVehicleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆信息MAPPER接口
 */
@MyBatisMapper
public interface SysTmsVehicleMapper extends BaseMapper<SysTmsVehicle> {

    SysTmsVehicleEntity getEntity(String id);

    List<SysTmsVehicleEntity> findPage(SysTmsVehicleEntity entity);

    List<SysTmsVehicleEntity> findGrid(SysTmsVehicleEntity entity);

    List<SysTmsVehicle> findSync(SysTmsVehicle entity);

    SysTmsVehicle getByNo(@Param("carNo") String carNo, @Param("dataSet") String dataSet);

    SysTmsVehicleEntity getEntityByNo(@Param("carNo") String carNo, @Param("dataSet") String dataSet);
}