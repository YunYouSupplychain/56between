package com.yunyou.modules.wms.inventory.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvDailyTemp;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvDailyTempMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库存临时表Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmInvDailyTempService extends CrudService<BanQinWmInvDailyTempMapper, BanQinWmInvDailyTemp> {

	public BanQinWmInvDailyTemp get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmInvDailyTemp> findList(BanQinWmInvDailyTemp banQinWmInvDailyTemp) {
		return super.findList(banQinWmInvDailyTemp);
	}
	
	public Page<BanQinWmInvDailyTemp> findPage(Page<BanQinWmInvDailyTemp> page, BanQinWmInvDailyTemp banQinWmInvDailyTemp) {
		return super.findPage(page, banQinWmInvDailyTemp);
	}
	
	@Transactional
	public void save(BanQinWmInvDailyTemp banQinWmInvDailyTemp) {
		super.save(banQinWmInvDailyTemp);
	}
	
	@Transactional
	public void delete(BanQinWmInvDailyTemp banQinWmInvDailyTemp) {
		super.delete(banQinWmInvDailyTemp);
	}
	
}