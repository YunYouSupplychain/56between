package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质检规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleQcHeaderMapper extends BaseMapper<SysWmsRuleQcHeader> {
    List<SysWmsRuleQcHeader> findPage(SysWmsRuleQcHeader entity);

    List<SysWmsRuleQcHeader> findGrid(SysWmsRuleQcHeader entity);

    List<SysWmsRuleQcHeader> findSync(SysWmsRuleQcHeader entity);

    SysWmsRuleQcHeader getByCode(@Param("ruleCode") String ruleCode, @Param("dataSet") String dataSet);
}