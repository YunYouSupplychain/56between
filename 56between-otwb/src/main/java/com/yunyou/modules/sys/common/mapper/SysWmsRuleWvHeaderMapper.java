package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvHeader;

import java.util.List;

/**
 * 质检规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleWvHeaderMapper extends BaseMapper<SysWmsRuleWvHeader> {
    List<SysWmsRuleWvHeader> findPage(SysWmsRuleWvHeader entity);

    List<SysWmsRuleWvHeader> findGrid(SysWmsRuleWvHeader entity);

    List<SysWmsRuleWvHeader> findSync(SysWmsRuleWvHeader entity);
}