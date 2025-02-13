package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmObjRoute;
import com.yunyou.modules.tms.basic.entity.extend.TmObjRouteEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务对象路由Mapper
 */
@MyBatisMapper
public interface TmObjRouteMapper extends BaseMapper<TmObjRoute> {

    TmObjRoute getByCode(@Param("carrierCode") String carrierCode, @Param("startObjCode") String startObjCode, @Param("endObjCode") String endObjCode, @Param("orgId") String orgId);

    TmObjRouteEntity getEntity(String id);

    List<TmObjRoute> findPage(TmObjRouteEntity entity);
}
