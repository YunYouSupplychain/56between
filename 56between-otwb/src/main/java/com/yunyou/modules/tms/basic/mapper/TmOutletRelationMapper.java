package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.TreeMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmOutletRelation;
import com.yunyou.modules.tms.basic.entity.extend.TmOutletRelationEntity;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网点拓扑图MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-02
 */
@MyBatisMapper
public interface TmOutletRelationMapper extends TreeMapper<TmOutletRelation> {

    List<TmOutletRelation> findChildren(@Param("parentId") String parentId, @Param("orgId") String orgId);

    List<TmOutletRelationEntity> findRoute(TmOutletRelationEntity qEntity);

    void removeAll(@Param("orgId") String orgId);

    void batchInsert(List<TmOutletRelation> list);
}