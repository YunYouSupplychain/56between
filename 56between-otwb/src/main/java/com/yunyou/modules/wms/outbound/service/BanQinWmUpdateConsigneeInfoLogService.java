package com.yunyou.modules.wms.outbound.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmUpdateConsigneeInfoLog;
import com.yunyou.modules.wms.outbound.entity.BanQinWmUpdateConsigneeInfoLogEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmUpdateConsigneeInfoLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 更新收货信息日志Service
 * @author WMJ
 * @version 2020-03-19
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmUpdateConsigneeInfoLogService extends CrudService<BanQinWmUpdateConsigneeInfoLogMapper, BanQinWmUpdateConsigneeInfoLog> {

	public List<BanQinWmUpdateConsigneeInfoLog> findList(BanQinWmUpdateConsigneeInfoLog wmUpdateConsigneeInfoLog) {
		return mapper.findList(wmUpdateConsigneeInfoLog);
	}
	
	public Page<BanQinWmUpdateConsigneeInfoLogEntity> findPage(Page page, BanQinWmUpdateConsigneeInfoLogEntity entity) {
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
	}
	
	@Transactional
	public void save(BanQinWmUpdateConsigneeInfoLog wmUpdateConsigneeInfoLog) {
		super.save(wmUpdateConsigneeInfoLog);
	}
	
	@Transactional
	public void delete(BanQinWmUpdateConsigneeInfoLog wmUpdateConsigneeInfoLog) {
		super.delete(wmUpdateConsigneeInfoLog);
	}

}