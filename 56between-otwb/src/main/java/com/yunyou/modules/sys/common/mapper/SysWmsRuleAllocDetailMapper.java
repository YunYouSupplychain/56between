package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleAllocDetail;

/**
 * 分配规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleAllocDetailMapper extends BaseMapper<SysWmsRuleAllocDetail> {
    void deleteByHeaderId(String headerId);
}