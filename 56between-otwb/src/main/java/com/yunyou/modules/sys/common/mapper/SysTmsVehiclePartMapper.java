package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsVehiclePart;
import org.apache.ibatis.annotations.Param;

/**
 * 车辆配件MAPPER接口
 */
@MyBatisMapper
public interface SysTmsVehiclePartMapper extends BaseMapper<SysTmsVehiclePart> {

    void deleteByCar(@Param("carNo") String carNo, @Param("dataSet") String dataSet);
}