package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsDriverQualification;
import org.apache.ibatis.annotations.Param;

/**
 * 运输人员资质信息MAPPER接口
 */
@MyBatisMapper
public interface SysTmsDriverQualificationMapper extends BaseMapper<SysTmsDriverQualification> {

    void deleteByDriver(@Param("driverCode") String driverCode, @Param("dataSet") String dataSet);
}