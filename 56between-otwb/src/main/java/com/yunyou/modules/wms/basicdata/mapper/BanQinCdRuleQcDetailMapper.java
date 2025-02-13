package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 质检规则MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdRuleQcDetailMapper extends BaseMapper<BanQinCdRuleQcDetail> {
    
	void deleteByHeaderId(String headerId);

    void remove(@Param("ruleCode") String ruleCode, @Param("orgId") String orgId);
}