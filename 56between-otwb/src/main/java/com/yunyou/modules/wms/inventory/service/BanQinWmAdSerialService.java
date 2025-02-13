package com.yunyou.modules.wms.inventory.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdSerial;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdSerialEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmAdSerialMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 序列号调整Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmAdSerialService extends CrudService<BanQinWmAdSerialMapper, BanQinWmAdSerial> {

	public BanQinWmAdSerial get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmAdSerial> findList(BanQinWmAdSerial banQinWmAdSerial) {
		return super.findList(banQinWmAdSerial);
	}
	
	public Page<BanQinWmAdSerialEntity> findPage(Page page, BanQinWmAdSerialEntity entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(mapper.findPage(entity));
		return page;
	}
	
	@Transactional
	public void save(BanQinWmAdSerial banQinWmAdSerial) {
		super.save(banQinWmAdSerial);
	}
	
	@Transactional
	public void delete(BanQinWmAdSerial banQinWmAdSerial) {
		super.delete(banQinWmAdSerial);
	}
	
	public Double wmAdAbsSerialQuery(BanQinWmAdSerial banQinWmAdSerial) {
	    return mapper.wmAdAbsSerialQuery(banQinWmAdSerial);
    }
    
    public List<BanQinWmAdSerial> getModelsByAdNo(String adNo, String lotNum, String orgId) {
        BanQinWmAdSerial wmAdSerial = new BanQinWmAdSerial();
        wmAdSerial.setAdNo(adNo);
        wmAdSerial.setLotNum(lotNum);
        wmAdSerial.setOrgId(orgId);
        return findList(wmAdSerial);
    }
	
}