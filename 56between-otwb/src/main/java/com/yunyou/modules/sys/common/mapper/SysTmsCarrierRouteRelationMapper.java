package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsCarrierRouteRelation;
import com.yunyou.modules.sys.common.entity.extend.SysTmsCarrierRouteRelationEntity;

import java.util.List;

/**
 * 承运商路由信息MAPPER接口
 */
@MyBatisMapper
public interface SysTmsCarrierRouteRelationMapper extends BaseMapper<SysTmsCarrierRouteRelation> {

    SysTmsCarrierRouteRelationEntity getEntity(String id);

    List<SysTmsCarrierRouteRelationEntity> findPage(SysTmsCarrierRouteRelationEntity entity);

    List<SysTmsCarrierRouteRelationEntity> findGrid(SysTmsCarrierRouteRelationEntity entity);
}