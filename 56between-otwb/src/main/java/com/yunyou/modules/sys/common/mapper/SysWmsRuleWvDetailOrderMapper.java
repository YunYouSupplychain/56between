package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetailOrder;

/**
 * 波次规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleWvDetailOrderMapper extends BaseMapper<SysWmsRuleWvDetailOrder> {
    void deleteByHeaderId(String headerId);
}