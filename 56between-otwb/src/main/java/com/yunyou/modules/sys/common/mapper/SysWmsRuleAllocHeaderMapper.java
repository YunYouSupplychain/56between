package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleAllocHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分配规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleAllocHeaderMapper extends BaseMapper<SysWmsRuleAllocHeader> {
    List<SysWmsRuleAllocHeader> findPage(SysWmsRuleAllocHeader entity);

    List<SysWmsRuleAllocHeader> findGrid(SysWmsRuleAllocHeader entity);

    List<SysWmsRuleAllocHeader> findSync(SysWmsRuleAllocHeader entity);

    SysWmsRuleAllocHeader getByCode(@Param("ruleCode") String ruleCode, @Param("dataSet") String dataSet);
}