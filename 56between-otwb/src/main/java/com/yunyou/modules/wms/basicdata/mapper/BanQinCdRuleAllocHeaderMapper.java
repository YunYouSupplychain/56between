package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleAllocHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分配规则MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdRuleAllocHeaderMapper extends BaseMapper<BanQinCdRuleAllocHeader> {
    List<BanQinCdRuleAllocHeader> findPage(BanQinCdRuleAllocHeader cdRuleAllocHeader);

    void remove(@Param("ruleCode") String ruleCode, @Param("orgId") String orgId);
}