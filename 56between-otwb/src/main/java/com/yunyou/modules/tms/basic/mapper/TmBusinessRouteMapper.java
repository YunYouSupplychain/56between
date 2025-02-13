package com.yunyou.modules.tms.basic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmBusinessRoute;
import com.yunyou.modules.tms.basic.entity.extend.TmBusinessRouteEntity;

@MyBatisMapper
public interface TmBusinessRouteMapper extends BaseMapper<TmBusinessRoute> {

    TmBusinessRouteEntity getEntity(String id);

    TmBusinessRouteEntity getByCode(@Param("code") String code, @Param("orgId") String orgId);

    List<TmBusinessRouteEntity> findPage(TmBusinessRouteEntity qEntity);

    List<TmBusinessRouteEntity> findGrid(TmBusinessRouteEntity qEntity);

    void remove(@Param("code") String code, @Param("orgId") String orgId);
}
