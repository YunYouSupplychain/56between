package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsTransportEquipmentSpace;
import org.apache.ibatis.annotations.Param;

/**
 * 运输设备空间信息MAPPER接口
 */
@MyBatisMapper
public interface SysTmsTransportEquipmentSpaceMapper extends BaseMapper<SysTmsTransportEquipmentSpace> {
    void deleteByEquipmentType(@Param("transportEquipmentTypeCode") String transportEquipmentTypeCode, @Param("dataSet") String dataSet);
}