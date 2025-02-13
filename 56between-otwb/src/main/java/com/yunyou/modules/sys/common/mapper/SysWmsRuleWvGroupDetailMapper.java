package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvGroupDetail;

/**
 * 波次规则组明细MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleWvGroupDetailMapper extends BaseMapper<SysWmsRuleWvGroupDetail> {
    void deleteByHeaderId(String headerId);
}