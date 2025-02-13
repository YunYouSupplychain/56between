package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsBusinessRoute;
import com.yunyou.modules.sys.common.entity.extend.SysTmsBusinessRouteEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface SysTmsBusinessRouteMapper extends BaseMapper<SysTmsBusinessRoute> {

    SysTmsBusinessRouteEntity getEntity(String id);

    SysTmsBusinessRouteEntity getByCode(@Param("code") String code, @Param("dataSet") String dataSet);

    List<SysTmsBusinessRouteEntity> findPage(SysTmsBusinessRouteEntity qEntity);

    List<SysTmsBusinessRouteEntity> findGrid(SysTmsBusinessRouteEntity qEntity);
}
