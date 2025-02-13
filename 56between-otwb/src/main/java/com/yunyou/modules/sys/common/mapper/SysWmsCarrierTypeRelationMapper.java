package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsCarrierTypeRelation;
import com.yunyou.modules.sys.common.entity.extend.SysWmsCarrierTypeRelationEntity;

import java.util.List;

/**
 * 承运商类型关系MAPPER接口
 */
@MyBatisMapper
public interface SysWmsCarrierTypeRelationMapper extends BaseMapper<SysWmsCarrierTypeRelation> {

    SysWmsCarrierTypeRelationEntity getEntity(String id);

    List<SysWmsCarrierTypeRelationEntity> findPage(SysWmsCarrierTypeRelationEntity entity);
}