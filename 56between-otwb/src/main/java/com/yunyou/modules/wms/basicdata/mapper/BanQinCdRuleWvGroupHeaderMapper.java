package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 波次规则组MAPPER接口
 *
 * @author WMJ
 * @version 2020-02-09
 */
@MyBatisMapper
public interface BanQinCdRuleWvGroupHeaderMapper extends BaseMapper<BanQinCdRuleWvGroupHeader> {
    List<BanQinCdRuleWvGroupHeader> findPage(BanQinCdRuleWvGroupHeader cdRuleWvGroupHeader);

    BanQinCdRuleWvGroupHeader getByGroupCode(@Param("groupCode") String groupCode, @Param("orgId") String orgId);

    void remove(@Param("groupCode") String groupCode, @Param("orgId") String orgId);
}