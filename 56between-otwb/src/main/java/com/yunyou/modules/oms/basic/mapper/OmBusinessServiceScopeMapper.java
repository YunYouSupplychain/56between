package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmBusinessServiceScope;
import com.yunyou.modules.oms.basic.entity.extend.OmBusinessServiceScopeEntity;
import com.yunyou.modules.sys.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务服务范围MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-10-23
 */
@MyBatisMapper
public interface OmBusinessServiceScopeMapper extends BaseMapper<OmBusinessServiceScope> {

    OmBusinessServiceScopeEntity getEntity(String id);

    List<OmBusinessServiceScopeEntity> findPage(OmBusinessServiceScopeEntity entity);

    List<Area> getArea(String id);

    void deleteArea(OmBusinessServiceScopeEntity entity);

    void insertArea(OmBusinessServiceScopeEntity entity);

    List<OmBusinessServiceScope> findByAreaId(@Param("areaId") String areaId, @Param("orgId") String orgId);

    OmBusinessServiceScopeEntity getByCode(@Param("groupCode") String groupCode, @Param("orgId") String orgId);

    void removeArea(@Param("groupCode") String groupCode, @Param("orgId") String orgId);

    void remove(@Param("groupCode") String groupCode, @Param("orgId") String orgId);
}