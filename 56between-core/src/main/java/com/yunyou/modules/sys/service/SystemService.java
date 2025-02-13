package com.yunyou.modules.sys.service;

import com.google.common.collect.Lists;
import com.yunyou.common.config.CacheNames;
import com.yunyou.common.exception.ServiceException;
import com.yunyou.common.utils.Encodes;
import com.yunyou.common.utils.RedisUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.secure.Digests;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.security.shiro.session.SessionDAO;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.entity.*;
import com.yunyou.modules.sys.mapper.MenuMapper;
import com.yunyou.modules.sys.mapper.RoleMapper;
import com.yunyou.modules.sys.mapper.UserMapper;
import com.yunyou.modules.sys.security.SystemAuthorizingRealm;
import com.yunyou.modules.sys.utils.UserUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 *
 * @author yunyou
 * @version 2016-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERACTIONS = 1024;
    public static final int SALT_SIZE = 8;

    @Autowired
    private DataRuleService dataRuleService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private SessionDAO sessionDao;
    @Autowired
    private RedisUtils redisUtils;

    public SessionDAO getSessionDao() {
        return sessionDao;
    }

    /**
     * 获取用户
     */
    public User getUser(String id) {
        return UserUtils.get(id);
    }

    /**
     * 根据登录名获取用户
     */
    public User getUserByLoginName(String loginName) {
        return UserUtils.getByLoginName(loginName);
    }

    public Page<User> findUser(Page<User> page, User user) {
        dataRuleFilter(user);
        // 设置分页参数
        user.setPage(page);
        // 执行分页查询
        page.setList(userMapper.findList(user));
        return page;
    }

    /**
     * 无分页查询人员列表
     */
    public List<User> findUser(User user) {
        dataRuleFilter(user);
        return userMapper.findList(user);
    }

    /**
     * 通过机构ID获取用户列表，仅返回用户id和name（树查询用户时用）
     */
    @SuppressWarnings("unchecked")
    public List<User> findUserByOfficeId(String officeId) {
        List<User> list = (List<User>) redisUtils.get(CacheNames.USER_CACHE_LIST_BY_OFFICE_ID + officeId);
        if (list == null) {
            User user = new User();
            user.setOffice(new Office(officeId));
            list = userMapper.findUserByOfficeId(user);
            redisUtils.set(CacheNames.USER_CACHE_LIST_BY_OFFICE_ID + officeId, list);
        }
        return list;
    }

    @Transactional
    public void saveUser(User user) {
        if (StringUtils.isBlank(user.getId())) {
            user.preInsert();
            userMapper.insert(user);
        } else {
            // 清除原用户机构用户缓存
            User oldUser = userMapper.get(user.getId());
            if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
                redisUtils.delete(CacheNames.USER_CACHE_LIST_BY_OFFICE_ID + oldUser.getOffice().getId());
            }
            // 更新用户数据
            user.preUpdate();
            userMapper.update(user);
        }
        if (StringUtils.isNotBlank(user.getId())) {
            // 更新用户与角色关联
            userMapper.deleteUserRole(user);
            if (user.getRoleList() != null && user.getRoleList().size() > 0) {
                userMapper.insertUserRole(user);
            } else {
                throw new ServiceException(user.getLoginName() + "没有设置角色！");
            }
            // 清除用户缓存
            UserUtils.clearCache(user);
            // 清除权限缓存
            UserUtils.clearCachedAuthorizationInfo(user);
        }
    }

    @Transactional
    public void updateUserInfo(User user) {
        user.preUpdate();
        userMapper.updateUserInfo(user);
        // 清除用户缓存
        UserUtils.clearCache(user);
        // 清除权限缓存
        UserUtils.clearCachedAuthorizationInfo(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userMapper.deleteUserRole(user);
        userMapper.delete(user);
        // 清除用户缓存
        UserUtils.clearCache(user);
    }

    @Transactional
    public void updatePasswordById(String id, String loginName, String newPassword) {
        User user = new User(id);
        user.setPassword(encryptPassword(newPassword));
        userMapper.updatePasswordById(user);
        // 清除用户缓存
        user.setLoginName(loginName);
        UserUtils.clearCache(user);
    }

    @Transactional
    public void updateUserLoginInfo(User user) {
        // 保存上次登录信息
        user.setOldLoginIp(user.getLoginIp());
        user.setOldLoginDate(user.getLoginDate());
        // 更新本次登录信息
        user.setLoginIp(UserUtils.getSession().getHost());
        user.setLoginDate(new Date());
        userMapper.updateLoginInfo(user);
    }

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String encryptPassword(String plainPassword) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERACTIONS);
        return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
    }

    /**
     * 验证密码
     *
     * @param plainPassword 明文密码
     * @param password      密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        byte[] salt = Encodes.decodeHex(password.substring(0, 16));
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERACTIONS);
        return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
    }

    /**
     * 获得活动会话
     */
    public Collection<Session> getActiveSessions() {
        return sessionDao.getActiveSessions(false);
    }

    //-- Role Service --//
    public Role getRole(String id) {
        return roleMapper.get(id);
    }

    public Role getRoleByName(String name) {
        Role r = new Role();
        r.setName(name);
        return roleMapper.getByName(r);
    }

    public Role getRoleByEnname(String enname) {
        Role r = new Role();
        r.setEnname(enname);
        return roleMapper.getByEnname(r);
    }

    public List<Role> findRole(Role role) {
        return roleMapper.findList(role);
    }

    public List<Role> findAllRole() {
        return UserUtils.getRoleList();
    }

    @Transactional
    public void saveRole(Role role) {
        if (StringUtils.isBlank(role.getId())) {
            role.preInsert();
            roleMapper.insert(role);
        } else {
            role.preUpdate();
            roleMapper.update(role);
        }
        // 更新角色与菜单关联
        roleMapper.deleteRoleMenu(role);
        if (role.getMenuList().size() > 0) {
            roleMapper.insertRoleMenu(role);
        }
        // 更新角色与数据权限关联
        roleMapper.deleteRoleDataRule(role);
        if (role.getDataRuleList().size() > 0) {
            roleMapper.insertRoleDataRule(role);
        }
        // 清除用户角色缓存
        UserUtils.clearRoleCache();
        // 清除权限缓存
        UserUtils.clearCachedAuthorizationInfo();
    }

    @Transactional
    public void deleteRole(Role role) {
        roleMapper.deleteRoleMenu(role);
        roleMapper.deleteRoleDataRule(role);
        roleMapper.delete(role);
        // 清除用户角色缓存
        UserUtils.clearRoleCache();
        // 清除权限缓存
        UserUtils.clearCachedAuthorizationInfo();
    }

    @Transactional
    public Boolean outUserInRole(Role role, User user) {
        List<Role> roles = user.getRoleList();
        for (Role e : roles) {
            if (e.getId().equals(role.getId())) {
                roles.remove(e);
                saveUser(user);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public User assignUserToRole(Role role, User user) {
        if (user == null) {
            return null;
        }
        List<String> roleIds = user.getRoleIdList();
        if (roleIds.contains(role.getId())) {
            return null;
        }
        user.getRoleList().add(role);
        saveUser(user);
        return user;
    }

    //-- Menu Service --//
    public Menu getMenu(String id) {
        return menuMapper.get(id);
    }

    public List<Menu> findAllMenu() {
        return UserUtils.getMenuList();
    }

    public List<Menu> getChildren(String parentId) {
        return menuMapper.getChildren(parentId);
    }

    @Transactional
    public void saveMenu(Menu menu) {
        // 获取父节点实体
        menu.setParent(this.getMenu(menu.getParent().getId()));
        // 获取修改前的parentIds，用于更新子节点的parentIds
        String oldParentIds = menu.getParentIds();
        // 设置新的父节点串
        menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");
        // 保存或更新实体
        if (StringUtils.isBlank(menu.getId())) {
            menu.preInsert();
            menuMapper.insert(menu);
        } else {
            menu.preUpdate();
            menuMapper.update(menu);
        }
        // 更新子节点 parentIds
        Menu m = new Menu();
        m.setParentIds("%," + menu.getId() + ",%");
        List<Menu> list = menuMapper.findByParentIdsLike(m);
        for (Menu e : list) {
            e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
            menuMapper.updateParentIds(e);
        }
        // 清除用户角色缓存
        UserUtils.clearRoleCache();
        // 清除权限缓存
        UserUtils.clearCachedAuthorizationInfo();
        // 清除日志相关缓存
        redisUtils.delete(CacheNames.SYS_CACHE_MENU);
    }

    @Transactional
    public void updateMenuSort(Menu menu) {
        menuMapper.updateSort(menu);
        // 清除用户角色缓存
        UserUtils.clearRoleCache();
        // 清除日志相关缓存
        redisUtils.delete(CacheNames.SYS_CACHE_MENU);
    }

    @Transactional
    public void deleteMenu(Menu menu) {
        // 解除菜单角色关联
        List<Object> mrList = menuMapper.execSelectSql(
                "SELECT distinct a.menu_id as id FROM sys_role_menu a left join sys_menu menu on a.menu_id = menu.id WHERE a.menu_id ='"
                        + menu.getId() + "' OR menu.parent_ids LIKE  '%," + menu.getId() + ",%'");
        for (Object mr : mrList) {
            menuMapper.deleteMenuRole(mr.toString());
        }
        // 删除菜单关联的数据权限数据，以及解除角色数据权限关联
        List<Object> mdList = menuMapper.execSelectSql(
                "SELECT distinct a.id as id FROM sys_datarule a left join sys_menu menu on a.menu_id = menu.id WHERE a.menu_id ='"
                        + menu.getId() + "' OR menu.parent_ids LIKE  '%," + menu.getId() + ",%'");
        for (Object md : mdList) {
            DataRule dataRule = new DataRule(md.toString());
            dataRuleService.delete(dataRule);
        }
        menuMapper.delete(menu);
        // 清除用户角色缓存
        UserUtils.clearRoleCache();
        // 清除权限缓存
        UserUtils.clearCachedAuthorizationInfo();
        // 清除日志相关缓存
        redisUtils.delete(CacheNames.SYS_CACHE_MENU);
    }

    /**
     * 获取产品信息
     */
    public static boolean printKeyLoadMessage() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public List<Session> getActiveSession() {
        List<Session> result = Lists.newArrayList();
        try {
            Collection<Session> sessions = this.getSessionDao().getActiveSessions();
            if (sessions.size() > 0) {
                for (Session session : sessions) {
                    if (null != session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)) {
                        result.add(session);
                    }
                }
            }
        } catch (NullPointerException ignored) {

        }
        return result;
    }

    public List<Session> getActiveSession(User user) {
        List<Session> result = Lists.newArrayList();
        try {
            Collection<Session> sessions = this.getSessionDao().getActiveSessions();
            if (!sessions.isEmpty()) {
                for (Session session : sessions) {
                    if (null != session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)) {
                        SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                        SystemAuthorizingRealm.Principal primaryPrincipal = (SystemAuthorizingRealm.Principal) principalCollection.getPrimaryPrincipal();
                        if (user.getLoginName().equals(primaryPrincipal.getLoginName())) {
                            result.add(session);
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
        return result;
    }

}
