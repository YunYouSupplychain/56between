package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTranSerial;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTranSerialEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmActTranSerialMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库存序列号交易Service
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmActTranSerialService extends CrudService<BanQinWmActTranSerialMapper, BanQinWmActTranSerial> {

	public BanQinWmActTranSerial get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmActTranSerial> findList(BanQinWmActTranSerial banQinWmActTranSerial) {
		return super.findList(banQinWmActTranSerial);
	}
	
	public Page<BanQinWmActTranSerialEntity> findPage(Page page, BanQinWmActTranSerialEntity entity) {
		entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
		dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(mapper.findPage(entity));
		return page;
	}
	
	@Transactional
	public void save(BanQinWmActTranSerial banQinWmActTranSerial) {
		super.save(banQinWmActTranSerial);
	}
	
	@Transactional
	public void delete(BanQinWmActTranSerial banQinWmActTranSerial) {
		super.delete(banQinWmActTranSerial);
	}
	
}