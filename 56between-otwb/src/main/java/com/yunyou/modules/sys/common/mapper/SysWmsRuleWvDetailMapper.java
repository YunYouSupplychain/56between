package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetail;

import java.util.List;

/**
 * 波次规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleWvDetailMapper extends BaseMapper<SysWmsRuleWvDetail> {
    List<SysWmsRuleWvDetail> getByHeaderId(String headerId);

    void deleteByHeaderId(String headerId);
}