package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsTransportObjScope;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportObjScopeEntity;

import java.util.List;

/**
 * 业务对象服务范围MAPPER接口
 */
@MyBatisMapper
public interface SysTmsTransportObjScopeMapper extends BaseMapper<SysTmsTransportObjScope> {

    SysTmsTransportObjScopeEntity getEntity(String id);

    List<SysTmsTransportObjScopeEntity> findPage(SysTmsTransportObjScopeEntity entity);

    List<SysTmsTransportObjScope> findCarrierScope(SysTmsTransportObjScope entity);

}