package com.yunyou.modules.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.Menu;

/**
 * 菜单MAPPER接口
 * @author yunyou
 * @version 2017-05-16
 */
@MyBatisMapper
public interface MenuMapper extends BaseMapper<Menu> {

	List<Menu> findByParentIdsLike(Menu menu);

	List<Menu> findByUserId(Menu menu);
	
	int updateParentIds(Menu menu);
	
	int updateSort(Menu menu);
	
	List<Menu> getChildren(String parentId);
	
	void deleteMenuRole(@Param(value = "menu_id") String menu_id);
	
	void deleteMenuDataRule(@Param(value = "menu_id") String menu_id);
	
	List<Menu> findAllDataRuleList(Menu menu);

}
