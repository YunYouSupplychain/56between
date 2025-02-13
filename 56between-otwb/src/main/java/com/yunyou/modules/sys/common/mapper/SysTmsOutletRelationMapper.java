package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.TreeMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsOutletRelation;
import com.yunyou.modules.sys.common.entity.extend.SysTmsOutletRelationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网点拓扑图MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-02
 */
@MyBatisMapper
public interface SysTmsOutletRelationMapper extends TreeMapper<SysTmsOutletRelation> {

    List<SysTmsOutletRelation> findChildren(@Param("parentId") String parentId, @Param("dataSet") String dataSet);

    List<SysTmsOutletRelationEntity> findRoute(SysTmsOutletRelationEntity qEntity);

    List<SysTmsOutletRelation> findSync(@Param("dataSet") String dataSet);
}