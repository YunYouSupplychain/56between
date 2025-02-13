package com.yunyou.common.config;

/**
 * 缓存名称定义
 * 缓存设置的标准：
 * 1 频繁读取，但是很少修改的内容，例如用户菜单，用户，字典
 * 2 读取频率不高，但是数据量很大也很少修改的内容，比如机构，区域
 */
public class CacheNames {

    /**
     * 认证
     */
    public static final String SYS_CACHE_AUTHORIZE = "sys:cache:authorize:";
    /**
     * 激活的Session
     */
    public static final String SYS_CACHE_ACTIVE_SESSIONS = "sys:cache:activeSessions";
    /**
     * 验证码
     */
    public static final String SYS_CACHE_CODE = "sys:cache:code";
    /**
     * 字典
     */
    public static final String SYS_CACHE_DICT_MAP = "sys:cache:dict:map";
    /**
     * 控制参数
     */
    public static final String SYS_CACHE_CONTROL_PARAMS_MAP = "sys:cache:controlParams:map";
    /**
     * 菜单
     */
    public static final String SYS_CACHE_MENU = "sys:cache:menu:map";

    /**
     * 根据id关联用户
     */
    public static final String USER_CACHE_USER_ID = "user:cache:userId:";
    public static final String USER_CACHE_LIST_BY_OFFICE_ID = "user:cache:oid:";
    /**
     * 根据登录名关联用户
     */
    public static final String USER_CACHE_LOGIN_NAME = "user:cache:loginName:";
    /**
     * 登录失败
     */
    public static final String USER_CACHE_LOGIN_FAIL_MAP = "user:cache:loginFail:map";
    /**
     * 在线用户
     */
    public static final String USER_CACHE_ONLINE_USER_NAME = "user:cache:online:userName:";

}