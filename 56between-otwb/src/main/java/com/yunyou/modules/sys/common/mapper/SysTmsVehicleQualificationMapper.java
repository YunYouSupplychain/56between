package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsVehicleQualification;
import org.apache.ibatis.annotations.Param;

/**
 * 车辆资质信息MAPPER接口
 */
@MyBatisMapper
public interface SysTmsVehicleQualificationMapper extends BaseMapper<SysTmsVehicleQualification> {

    void deleteByCar(@Param("carNo") String carNo, @Param("dataSet") String dataSet);
}