package com.yunyou.modules.sys.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.Role;

/**
 * 角色MAPPER接口
 *
 * @author yunyou
 * @version 2016-12-05
 */
@MyBatisMapper
public interface RoleMapper extends BaseMapper<Role> {

    Role getByName(Role role);

    Role getByEnname(Role role);

    /**
     * 维护角色与菜单权限关系
     */
    int insertRoleMenu(Role role);

    int deleteRoleMenu(Role role);

    /**
     * 维护角色与数据权限关系
     */
    int insertRoleDataRule(Role role);

    int deleteRoleDataRule(Role role);

}
