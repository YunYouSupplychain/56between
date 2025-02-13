package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhCycle;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhCycleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 循环级别Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhCycleService extends CrudService<BanQinCdWhCycleMapper, BanQinCdWhCycle> {

	public Page<BanQinCdWhCycle> findPage(Page<BanQinCdWhCycle> page, BanQinCdWhCycle banQinCdWhCycle) {
		dataRuleFilter(banQinCdWhCycle);
		banQinCdWhCycle.setPage(page);
		page.setList(mapper.findPage(banQinCdWhCycle));
		return page;
	}

	public BanQinCdWhCycle getByCode(String cycleCode, String orgId) {
		return mapper.getByCode(cycleCode, orgId);
	}

	@Transactional
    public void remove(String cycleCode, String orgId) {
		mapper.remove(cycleCode, orgId);
    }
}