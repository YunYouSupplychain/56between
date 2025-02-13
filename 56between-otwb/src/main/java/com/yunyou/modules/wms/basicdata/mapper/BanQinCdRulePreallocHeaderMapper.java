package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePreallocHeader;

import java.util.List;

/**
 * 预配规则MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdRulePreallocHeaderMapper extends BaseMapper<BanQinCdRulePreallocHeader> {
	List<BanQinCdRulePreallocHeader> findPage(BanQinCdRulePreallocHeader cdRulePreallocHeader);
}