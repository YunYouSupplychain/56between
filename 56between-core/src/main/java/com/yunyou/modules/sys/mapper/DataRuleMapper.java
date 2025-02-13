package com.yunyou.modules.sys.mapper;

import java.util.List;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.DataRule;
import com.yunyou.modules.sys.entity.User;

/**
 * 数据权限MAPPER接口
 * @author lgf
 * @version 2017-04-02
 */
@MyBatisMapper
public interface DataRuleMapper extends BaseMapper<DataRule> {

	void deleteRoleDataRule(DataRule dataRule);
	
	List<DataRule> findByUserId(User user);
}