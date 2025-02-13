package com.yunyou.modules.wms.inbound.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetailNew;
import com.yunyou.modules.wms.inbound.mapper.BanQinWmPoDetailNewMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 采购单明细Service
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmPoDetailNewService extends CrudService<BanQinWmPoDetailNewMapper, BanQinWmPoDetailNew> {

	public BanQinWmPoDetailNew get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmPoDetailNew> findList(BanQinWmPoDetailNew banQinWmPoDetailNew) {
		return super.findList(banQinWmPoDetailNew);
	}
	
	public Page<BanQinWmPoDetailNew> findPage(Page<BanQinWmPoDetailNew> page, BanQinWmPoDetailNew banQinWmPoDetailNew) {
		return super.findPage(page, banQinWmPoDetailNew);
	}
	
	@Transactional
	public void save(BanQinWmPoDetailNew banQinWmPoDetailNew) {
		super.save(banQinWmPoDetailNew);
	}
	
	@Transactional
	public void delete(BanQinWmPoDetailNew banQinWmPoDetailNew) {
		super.delete(banQinWmPoDetailNew);
	}

	@Transactional
    public void removeByPoId(String[] poIds) {
	    mapper.removeByPoId(poIds);
    }
}