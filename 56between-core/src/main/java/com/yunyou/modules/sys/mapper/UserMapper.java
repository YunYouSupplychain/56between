package com.yunyou.modules.sys.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.User;

import java.util.List;

/**
 * 用户MAPPER接口
 *
 * @author yunyou
 * @version 2017-05-16
 */
@MyBatisMapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据登录名称查询用户
     */
    User getByLoginName(User user);

    /**
     * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
     */
    List<User> findUserByOfficeId(User user);

    /**
     * 更新用户信息
     */
    int updateUserInfo(User user);

    /**
     * 更新用户密码
     */
    int updatePasswordById(User user);

    /**
     * 更新登录信息，如：登录IP、登录时间
     */
    int updateLoginInfo(User user);

    /**
     * 插入用户角色关联数据
     */
    int insertUserRole(User user);

    /**
     * 删除用户角色关联数据
     */
    int deleteUserRole(User user);

}
