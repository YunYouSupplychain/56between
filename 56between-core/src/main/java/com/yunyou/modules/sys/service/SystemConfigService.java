package com.yunyou.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.SystemConfig;
import com.yunyou.modules.sys.mapper.SystemConfigMapper;

/**
 * 系统配置Service
 * @author liugf
 * @version 2016-02-07
 */
@Service
@Transactional(readOnly = true)
public class SystemConfigService extends CrudService<SystemConfigMapper, SystemConfig> {

	public SystemConfig get(String id) {
		return super.get(id);
	}
	
	public List<SystemConfig> findList(SystemConfig systemConfig) {
		return super.findList(systemConfig);
	}
	
	public Page<SystemConfig> findPage(Page<SystemConfig> page, SystemConfig systemConfig) {
		return super.findPage(page, systemConfig);
	}
	
	@Transactional
	public void save(SystemConfig systemConfig) {
		super.save(systemConfig);
	}
	
	@Transactional
	public void delete(SystemConfig systemConfig) {
		super.delete(systemConfig);
	}
	
}