package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.tms.basic.entity.TmTransportScope;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportScopeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务服务范围MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmTransportScopeMapper extends BaseMapper<TmTransportScope> {

    TmTransportScopeEntity getEntity(String id);

    List<TmTransportScope> findPage(TmTransportScope tmTransportScope);

    List<TmTransportScope> findGrid(TmTransportScopeEntity entity);

    List<Area> findArea(String id);

    void insertArea(TmTransportScopeEntity entity);

    void deleteAreaByScope(String id);

    List<Area> findAreaByScopeCode(@Param("code") String code, @Param("orgId") String orgId);

    TmTransportScope getByCode(@Param("code") String code, @Param("orgId") String orgId);

    void removeArea(@Param("code") String code, @Param("orgId") String orgId);

    void remove(@Param("code") String code, @Param("orgId") String orgId);
}