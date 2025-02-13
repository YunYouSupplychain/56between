package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcClass;

/**
 * 质检规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleQcClassMapper extends BaseMapper<SysWmsRuleQcClass> {
    void deleteByHeaderId(String headerId);
}