package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmTransportEquipmentSpace;
import org.apache.ibatis.annotations.Param;

/**
 * 运输设备空间信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmTransportEquipmentSpaceMapper extends BaseMapper<TmTransportEquipmentSpace> {
    void remove(@Param("transportEquipmentTypeCode") String transportEquipmentTypeCode, @Param("orgId") String orgId);
}