package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmVehicleType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车型信息MAPPER接口
 *
 * @author liujianhua
 * @version 2022-08-04
 */
@MyBatisMapper
public interface TmVehicleTypeMapper extends BaseMapper<TmVehicleType> {

    List<TmVehicleType> findPage(TmVehicleType entity);

    List<TmVehicleType> findGrid(TmVehicleType entity);

    TmVehicleType getByNo(@Param("code") String code, @Param("orgId") String orgId);

    void removeByNo(@Param("code") String code, @Param("orgId") String orgId);
}