package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysOmsCarrierServiceScope;
import com.yunyou.modules.sys.common.entity.extend.SysOmsCarrierServiceScopeEntity;

import java.util.List;

/**
 * 承运商服务范围MAPPER接口
 */
@MyBatisMapper
public interface SysOmsCarrierServiceScopeMapper extends BaseMapper<SysOmsCarrierServiceScope> {

    SysOmsCarrierServiceScopeEntity getEntity(String id);

    List<SysOmsCarrierServiceScopeEntity> findPage(SysOmsCarrierServiceScopeEntity entity);
}