package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysDataSetOrgRelation;
import com.yunyou.modules.sys.common.entity.extend.SysDataSetOrgRelationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据套机构关系MAPPER接口
 */
@MyBatisMapper
public interface SysDataSetOrgRelationMapper extends BaseMapper<SysDataSetOrgRelation> {

    List<SysDataSetOrgRelationEntity> findGrid(SysDataSetOrgRelationEntity entity);

    List<SysDataSetOrgRelationEntity> findEntity(SysDataSetOrgRelationEntity entity);

    SysDataSetOrgRelationEntity getEntityByOrgId(@Param("orgId") String orgId);

    List<String> findDataSetByOrgCodes(@Param("orgCodes") List<String> orgCodes);
}