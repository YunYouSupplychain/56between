package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetailWv;

/**
 * 波次规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleWvDetailWvMapper extends BaseMapper<SysWmsRuleWvDetailWv> {
    void deleteByHeaderId(String headerId);
}