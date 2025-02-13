package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysBmsRoute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 路由MAPPER接口
 */
@MyBatisMapper
public interface SysBmsRouteMapper extends BaseMapper<SysBmsRoute> {

    List<SysBmsRoute> findPage(SysBmsRoute entity);

    SysBmsRoute getByCode(@Param("routeCode") String routeCode, @Param("dataSet") String dataSet);

    SysBmsRoute getByStartAndEndAreaId(@Param("startAreaId") String startAreaId, @Param("endAreaId") String endAreaId, @Param("dataSet") String dataSet);

    SysBmsRoute getByStartAndEndAreaCode(@Param("startAreaCode") String startAreaCode, @Param("endAreaCode") String endAreaCode, @Param("dataSet") String dataSet);
}