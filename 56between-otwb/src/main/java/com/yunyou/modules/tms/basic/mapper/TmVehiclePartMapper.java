package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmVehiclePart;
import org.apache.ibatis.annotations.Param;

/**
 * 车辆配件MAPPER接口
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmVehiclePartMapper extends BaseMapper<TmVehiclePart> {

    void deleteByCar(@Param("carNo") String carNo, @Param("orgId") String orgId);
}