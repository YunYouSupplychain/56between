package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcDetail;

/**
 * 质检规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleQcDetailMapper extends BaseMapper<SysWmsRuleQcDetail> {
    void deleteByHeaderId(String headerId);
}