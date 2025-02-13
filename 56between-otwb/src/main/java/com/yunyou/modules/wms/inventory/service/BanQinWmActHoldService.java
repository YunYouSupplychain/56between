package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActHold;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmActHoldMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 冻结记录Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmActHoldService extends CrudService<BanQinWmActHoldMapper, BanQinWmActHold> {

	public BanQinWmActHold get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmActHold> findList(BanQinWmActHold banQinWmActHold) {
		return super.findList(banQinWmActHold);
	}
	
	public Page<BanQinWmActHold> findPage(Page<BanQinWmActHold> page, BanQinWmActHold banQinWmActHold) {
        banQinWmActHold.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmActHold.getOrgId()));
        dataRuleFilter(banQinWmActHold);
        banQinWmActHold.setPage(page);
        page.setList(mapper.findPage(banQinWmActHold));
		return page;
	}
	
	@Transactional
	public void save(BanQinWmActHold banQinWmActHold) {
		super.save(banQinWmActHold);
	}
	
	@Transactional
	public void delete(BanQinWmActHold banQinWmActHold) {
		super.delete(banQinWmActHold);
	}
	
}