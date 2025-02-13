package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质检规则MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdRuleWvHeaderMapper extends BaseMapper<BanQinCdRuleWvHeader> {
    List<BanQinCdRuleWvHeader> findPage(BanQinCdRuleWvHeader cdRuleWvHeader);

    void remove(@Param("ruleCode") String ruleCode, @Param("orgId") String orgId);
}