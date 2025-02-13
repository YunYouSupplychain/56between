package com.yunyou.modules.sys.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.DataRule;
import com.yunyou.modules.sys.mapper.DataRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据权限Service
 * @author lgf
 * @version 2017-04-02
 */
@Service
@Transactional(readOnly = true)
public class DataRuleService extends CrudService<DataRuleMapper, DataRule> {
	@Autowired
	private DataRuleMapper dataRuleMapper;
	
	public DataRule get(String id) {
		return super.get(id);
	}
	
	public List<DataRule> findList(DataRule dataRule) {
		return super.findList(dataRule);
	}
	
	public Page<DataRule> findPage(Page<DataRule> page, DataRule dataRule) {
		return super.findPage(page, dataRule);
	}
	
	@Transactional
	public void save(DataRule dataRule) {
		super.save(dataRule);
	}
	
	@Transactional
	public void delete(DataRule dataRule) {
		// 解除数据权限角色关联
		dataRuleMapper.deleteRoleDataRule(dataRule);
		super.delete(dataRule);
	}
}