package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcClass;
import org.apache.ibatis.annotations.Param;

/**
 * 质检规则MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdRuleQcClassMapper extends BaseMapper<BanQinCdRuleQcClass> {

    void deleteByHeaderId(String headerId);

    void remove(@Param("ruleCode") String ruleCode, @Param("orgId") String orgId);
}