package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsRuleRotationHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存周转规则MAPPER接口
 */
@MyBatisMapper
public interface SysWmsRuleRotationHeaderMapper extends BaseMapper<SysWmsRuleRotationHeader> {
    List<SysWmsRuleRotationHeader> findPage(SysWmsRuleRotationHeader entity);

    List<SysWmsRuleRotationHeader> findGrid(SysWmsRuleRotationHeader entity);

    List<SysWmsRuleRotationHeader> findSync(SysWmsRuleRotationHeader entity);

    SysWmsRuleRotationHeader getByCode(@Param("ruleCode") String ruleCode, @Param("dataSet") String dataSet);
}