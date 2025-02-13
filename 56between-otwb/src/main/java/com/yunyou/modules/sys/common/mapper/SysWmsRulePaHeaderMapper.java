package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRulePaHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质检规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRulePaHeaderMapper extends BaseMapper<SysWmsRulePaHeader> {
    List<SysWmsRulePaHeader> findPage(SysWmsRulePaHeader entity);

    List<SysWmsRulePaHeader> findGrid(SysWmsRulePaHeader entity);

    List<SysWmsRulePaHeader> findSync(SysWmsRulePaHeader entity);

    SysWmsRulePaHeader getByCode(@Param("ruleCode") String ruleCode, @Param("dataSet") String dataSet);
}