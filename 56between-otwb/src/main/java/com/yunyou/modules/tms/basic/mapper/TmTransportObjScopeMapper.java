package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmTransportObjScope;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjScopeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务对象服务范围MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmTransportObjScopeMapper extends BaseMapper<TmTransportObjScope> {

    TmTransportObjScopeEntity getEntity(String id);

    List<TmTransportObjScopeEntity> findPage(TmTransportObjScope tmTransportObjScope);

    List<TmTransportObjScope> findCarrierScope(TmTransportObjScope tmTransportObjScope);

    void remove(@Param("transportObjCode") String transportObjCode, @Param("transportScopeCode") String transportScopeCode, @Param("transportScopeType") String transportScopeType, @Param("orgId") String orgId);
}