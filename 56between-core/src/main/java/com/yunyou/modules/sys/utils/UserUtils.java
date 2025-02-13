package com.yunyou.modules.sys.utils;

import com.yunyou.common.utils.RedisUtils;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.OfficeType;
import com.yunyou.common.config.CacheNames;
import com.yunyou.modules.sys.entity.*;
import com.yunyou.modules.sys.mapper.*;
import com.yunyou.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户工具类
 *
 * @author yunyou
 * @version 2016-12-05
 */
public class UserUtils {
    public static final String CACHE_ROLE_LIST = "roleList";
    public static final String CACHE_MENU_LIST = "menuList";
    public static final String CACHE_DATA_RULE_LIST = "dataRuleList";
    public static final String CACHE_AREA_LIST = "areaList";
    public static final String CACHE_OFFICE_LIST = "officeList";
    public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
    public static final String CACHE_TM_ORG = "tmOrganization";
    public static final String CACHE_TM_DISPATCH = "tmDispatch";
    private static final UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private static final RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
    private static final MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
    private static final AreaMapper areaMapper = SpringContextHolder.getBean(AreaMapper.class);
    private static final OfficeMapper officeMapper = SpringContextHolder.getBean(OfficeMapper.class);
    private static final DataRuleMapper dataRuleMapper = SpringContextHolder.getBean(DataRuleMapper.class);
    private static final RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);

    public static Office getOffice(String id) {
        Office office = officeMapper.get(id);
        if (office == null) {
            return new Office();
        }
        return office;
    }

    public static Office getOfficeByCode(String code) {
        return officeMapper.getByCode(code);
    }

    /**
     * 根据ID获取用户
     */
    public static User get(String id) {
        User user = (User) redisUtils.get(CacheNames.USER_CACHE_USER_ID + id);
        if (user == null) {
            user = userMapper.get(id);
            if (user == null) {
                return null;
            }
            user.setRoleList(roleMapper.findList(new Role(user)));
            redisUtils.set(CacheNames.USER_CACHE_USER_ID + user.getId(), user);
            redisUtils.set(CacheNames.USER_CACHE_LOGIN_NAME + user.getLoginName(), user);
        }
        return user;
    }

    /**
     * 根据登录名获取用户
     */
    public static User getByLoginName(String loginName) {
        User user = (User) redisUtils.get(CacheNames.USER_CACHE_LOGIN_NAME + loginName);
        if (user == null) {
            user = userMapper.getByLoginName(new User(null, loginName));
            if (user == null) {
                return null;
            }
            user.setRoleList(roleMapper.findList(new Role(user)));
            redisUtils.set(CacheNames.USER_CACHE_USER_ID + user.getId(), user);
            redisUtils.set(CacheNames.USER_CACHE_LOGIN_NAME + user.getLoginName(), user);
        }
        return user;
    }

    /**
     * 清除当前用户缓存
     */
    public static void clearCache() {
        UserUtils.clearRoleCache();
        removeCache(CACHE_AREA_LIST);
        removeCache(CACHE_OFFICE_LIST);
        removeCache(CACHE_OFFICE_ALL_LIST);
        removeCache(CACHE_TM_ORG);
        removeCache(CACHE_TM_DISPATCH);
        UserUtils.clearCache(getUser());
    }

    /**
     * 清除当前用户缓存
     */
    public static void clearRoleCache() {
        removeCache(CACHE_ROLE_LIST);
        removeCache(CACHE_DATA_RULE_LIST);
        removeCache(CACHE_MENU_LIST);
    }

    /**
     * 清除认证信息缓存
     */
    public static void clearCachedAuthorizationInfo() {
        Set<String> keys = redisUtils.keys(CacheNames.SYS_CACHE_AUTHORIZE + "*");
        for (String key : keys) {
            redisUtils.delete(key);
        }
    }

    /**
     * 清除指定用户认证信息缓存
     *
     * @see com.yunyou.core.security.shiro.cache.RedisCache
     * @see org.apache.shiro.realm.AuthorizingRealm getAuthorizationCacheName()
     */
    public static void clearCachedAuthorizationInfo(User user) {
        Set<String> keys = redisUtils.keys(CacheNames.SYS_CACHE_AUTHORIZE + "*" + user.getId());
        for (String key : keys) {
            redisUtils.delete(key);
        }
    }

    /**
     * 清除指定用户缓存
     */
    public static void clearCache(User user) {
        redisUtils.delete(CacheNames.USER_CACHE_USER_ID + user.getId());
        redisUtils.delete(CacheNames.USER_CACHE_LOGIN_NAME + user.getLoginName());
        redisUtils.delete(CacheNames.USER_CACHE_LOGIN_NAME + user.getOldLoginName());
        if (user.getOffice() != null && user.getOffice().getId() != null) {
            redisUtils.delete(CacheNames.USER_CACHE_LIST_BY_OFFICE_ID + user.getOffice().getId());
        }
    }

    /**
     * 获取当前用户
     *
     * @return 取不到返回 new User()
     */
    public static User getUser() {
        Principal principal = getPrincipal();
        if (principal != null) {
            User user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new User();
        }
        return new User();
    }

    /**
     * 判断当前用户是否是超级管理员
     */
    public static boolean isAdmin() {
        return User.isAdmin(getUser().getId());
    }

    /**
     * 获取当前用户角色列表
     */
    @SuppressWarnings("unchecked")
    public static List<Role> getRoleList() {
        List<Role> roleList = (List<Role>) getCache(CACHE_ROLE_LIST);
        if (roleList == null) {
            User user = getUser();
            if (user.isAdmin()) {
                roleList = roleMapper.findAllList(new Role());
            } else {
                Role role = new Role();
                BaseService.dataRuleFilter(role);
                roleList = roleMapper.findList(role);
            }
            putCache(CACHE_ROLE_LIST, roleList);
        }
        return roleList;
    }

    /**
     * 获取当前用户授权菜单
     */
    @SuppressWarnings("unchecked")
    public static List<Menu> getMenuList() {
        List<Menu> menuList = (List<Menu>) getCache(CACHE_MENU_LIST);
        if (menuList == null) {
            User user = getUser();
            if (user.isAdmin()) {
                menuList = menuMapper.findAllList(new Menu());
            } else {
                Menu m = new Menu();
                m.setUserId(user.getId());
                menuList = menuMapper.findByUserId(m);
            }
            putCache(CACHE_MENU_LIST, menuList);
        }
        return menuList;
    }

    /**
     * 获取当前用户授权数据权限
     */
    @SuppressWarnings("unchecked")
    public static List<DataRule> getDataRuleList() {
        List<DataRule> dataRuleList = (List<DataRule>) getCache(CACHE_DATA_RULE_LIST);
        if (dataRuleList == null) {
            User user = getUser();
            if (user.isAdmin()) {
                dataRuleList = Lists.newArrayList();
            } else {
                dataRuleList = dataRuleMapper.findByUserId(user);
            }
            putCache(CACHE_DATA_RULE_LIST, dataRuleList);
        }
        return dataRuleList;
    }

    /**
     * 获取当前用户授权菜单
     */
    public static Menu getTopMenu() {
        if (getMenuList().isEmpty()) {
            return new Menu();
        }
        return getMenuList().get(0);
    }

    /**
     * 获取当前用户授权的区域
     */
    @SuppressWarnings("unchecked")
    public static List<Area> getAreaList() {
        List<Area> areaList = (List<Area>) getCache(CACHE_AREA_LIST);
        if (areaList == null) {
            areaList = areaMapper.findAllList(new Area());
            putCache(CACHE_AREA_LIST, areaList);
        }
        return areaList;
    }

    /**
     * 获取当前用户有权限访问的机构
     */
    @SuppressWarnings("unchecked")
    public static List<Office> getOfficeList() {
        List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_LIST);
        if (officeList == null) {
            User user = getUser();
            if (user.isAdmin()) {
                officeList = officeMapper.findAllList(new Office());
            } else {
                Office office = new Office();
                BaseService.dataRuleFilter(office);
                officeList = officeMapper.findList(office);
            }
            putCache(CACHE_OFFICE_LIST, officeList);
        }
        return officeList;
    }

    /**
     * 获取当前用户有权限访问的机构
     */
    @SuppressWarnings("unchecked")
    public static List<Office> getOfficeAllList() {
        List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_ALL_LIST);
        if (officeList == null) {
            officeList = officeMapper.findAllList(new Office());
        }
        return officeList;
    }

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录者对象
     */
    public static Principal getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
        } catch (UnavailableSecurityManagerException | InvalidSessionException ignored) {
        }
        return null;
    }

    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
        } catch (InvalidSessionException ignored) {
        }
        return null;
    }

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        Object obj = getSession().getAttribute(key);
        return obj == null ? defaultValue : obj;
    }

    public static void putCache(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static void removeCache(String key) {
        getSession().removeAttribute(key);
    }

    /**
     * 导出Excel调用,根据姓名转换为ID
     */
    public static User getByUserName(String name) {
        User u = new User();
        u.setName(name);
        List<User> list = userMapper.findList(u);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return new User();
    }

    /**
     * 导出Excel使用，根据名字转换为id
     */
    public static Office getByOfficeName(String name) {
        List<Office> list = officeMapper.getByName(name);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return new Office();
    }

    /**
     * 导出Excel使用，根据名字转换为id
     */
    public static Area getByAreaName(String name) {
        Area a = new Area();
        a.setName(name);
        List<Area> list = areaMapper.findList(a);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return new Area();
    }

    public static boolean hasPermission(String permission) {
        return SecurityUtils.getSubject().isPermitted(permission);
    }

    /**
     * 描述：获取虚拟用户（SYSTEM）
     *
     * @author Jianhua on 2020-2-18
     */
    public static User getSystemUser() {
        User user = UserUtils.getByLoginName("SYSTEM");
        if (user == null) {
            user = new User("SYSTEM", "SYSTEM");
        }
        return user;
    }

    /**
     * 描述：根据机构ID获取组织中心
     */
    @SuppressWarnings("unchecked")
    public static Office getOrgCenter(String officeId) {
        Map<String, Office> map = (Map<String, Office>) getCache(CACHE_TM_ORG);
        if (MapUtil.isNotEmpty(map) && map.containsKey(officeId)) {
            return map.get(officeId);
        }
        if (map == null) {
            map = Maps.newHashMap();
        }
        Office office = officeMapper.get(officeId);
        if (office == null) {
            return new Office();
        }
        if (OfficeType.ORG_CENTER.getValue().equals(office.getType())) {
            map.put(officeId, office);
            putCache(CACHE_TM_ORG, map);
            return office;
        }
        if (StringUtils.isBlank(office.getParentIds())) {
            return new Office();
        }
        List<String> list = Arrays.stream(office.getParentIds().split(",")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        for (String id : list) {
            Office o = officeMapper.get(id);
            if (o != null && OfficeType.ORG_CENTER.getValue().equals(o.getType())) {
                map.put(officeId, o);
                putCache(CACHE_TM_ORG, map);
                return o;
            }
        }
        return new Office();
    }

    /**
     * 描述：根据机构ID获取调度中心
     */
    @SuppressWarnings("unchecked")
    public static Office getDispatchCenter(String officeId) {
        Map<String, Office> map = (Map<String, Office>) getCache(CACHE_TM_DISPATCH);
        if (MapUtil.isNotEmpty(map) && map.containsKey(officeId)) {
            return map.get(officeId);
        }
        if (map == null) {
            map = Maps.newHashMap();
        }
        Office office = officeMapper.get(officeId);
        if (office == null) {
            return new Office();
        }
        if (OfficeType.DISPATCH_CENTER.getValue().equals(office.getType())) {
            map.put(officeId, office);
            putCache(CACHE_TM_DISPATCH, map);
            return office;
        }
        if (StringUtils.isBlank(office.getParentIds())) {
            return new Office();
        }
        List<String> list = Arrays.stream(office.getParentIds().split(",")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        for (String id : list) {
            Office o = officeMapper.get(id);
            if (o != null && OfficeType.DISPATCH_CENTER.getValue().equals(o.getType())) {
                map.put(officeId, o);
                putCache(CACHE_TM_DISPATCH, map);
                return o;
            }
        }
        return new Office();
    }
}