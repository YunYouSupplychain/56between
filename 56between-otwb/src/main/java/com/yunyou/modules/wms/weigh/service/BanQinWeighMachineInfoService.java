package com.yunyou.modules.wms.weigh.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.weigh.entity.BanQinWeighMachineInfo;
import com.yunyou.modules.wms.weigh.entity.BanQinWeighMachineInfoEntity;
import com.yunyou.modules.wms.weigh.mapper.BanQinWeighMachineInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 称重设备表Service
 * @author zyf
 * @version 2019-09-21
 */
@Service
@Transactional(readOnly = true)
public class BanQinWeighMachineInfoService extends CrudService<BanQinWeighMachineInfoMapper, BanQinWeighMachineInfo> {

	public BanQinWeighMachineInfoEntity getEntity(String id) {
		return mapper.getEntity(id);
	}

	public Page<BanQinWeighMachineInfoEntity> findPage(Page page, BanQinWeighMachineInfoEntity entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(mapper.findPage(entity));
		return page;
	}

	public BanQinWeighMachineInfoEntity getBySN(String machineNo) {
		return mapper.getBySN(machineNo);
	}

	@Transactional
	public void remove(String machineNo, String orgId) {
		mapper.remove(machineNo, orgId);
	}
	
}