package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvGroupHeader;

import java.util.List;

/**
 * 波次规则组MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleWvGroupHeaderMapper extends BaseMapper<SysWmsRuleWvGroupHeader> {
    List<SysWmsRuleWvGroupHeader> findPage(SysWmsRuleWvGroupHeader entity);

    List<SysWmsRuleWvGroupHeader> findGrid(SysWmsRuleWvGroupHeader entity);

    List<SysWmsRuleWvGroupHeader> findSync(SysWmsRuleWvGroupHeader entity);
}