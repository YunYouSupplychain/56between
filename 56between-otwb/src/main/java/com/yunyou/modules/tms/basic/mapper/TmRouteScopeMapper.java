package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmRouteScope;
import com.yunyou.modules.tms.basic.entity.extend.TmRouteScopeEntity;

import java.util.List;

/**
 * 业务对象路由范围MAPPER接口
 *
 * @author liujianhua
 * @version 2021-10-15
 */
@MyBatisMapper
public interface TmRouteScopeMapper extends BaseMapper<TmRouteScope> {

    TmRouteScopeEntity getEntity(String id);

    List<TmRouteScope> findPage(TmRouteScopeEntity entity);
}