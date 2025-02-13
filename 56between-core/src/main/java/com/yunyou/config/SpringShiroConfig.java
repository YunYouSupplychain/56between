package com.yunyou.config;

import com.google.common.collect.ImmutableList;
import com.yunyou.common.config.CacheNames;
import com.yunyou.common.config.Global;
import com.yunyou.common.utils.IdGen;
import com.yunyou.core.security.shiro.cache.RedisCacheManager;
import com.yunyou.core.security.shiro.session.CacheSessionDAO;
import com.yunyou.core.security.shiro.session.SessionManager;
import com.yunyou.modules.sys.security.FormAuthenticationFilter;
import com.yunyou.modules.sys.security.LogoutSessionControlFilter;
import com.yunyou.modules.sys.security.SystemAuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro的控制类
 */
@Configuration
@PropertySource(value = "classpath:/properties/yunyou.properties", ignoreResourceNotFound = true)
public class SpringShiroConfig {

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(FormAuthenticationFilter formAuthenticationFilter,
                                                         DefaultWebSecurityManager securityManager,
                                                         LogoutSessionControlFilter logoutSessionControlFilter) {
        String adminPath = Global.getAdminPath();

        Map<String, Filter> filters = new HashMap<>(2);
        filters.put("authc", formAuthenticationFilter);
        filters.put("kickout", logoutSessionControlFilter);

        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setFilters(filters);
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl(adminPath + "/login");
        bean.setSuccessUrl(adminPath);

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 对外接口
        filterChainDefinitionMap.put("/api/**", "anon");
        // 静态资源
        filterChainDefinitionMap.put(adminPath + "/static/**", "anon");
        filterChainDefinitionMap.put(adminPath + "/userfiles/**", "anon");
        // WMS-RF端
        filterChainDefinitionMap.put(adminPath + "/wms/rf/getAppVersion", "anon");
        // 系统
        filterChainDefinitionMap.put(adminPath + "/login", "authc");
        filterChainDefinitionMap.put(adminPath + "/logout", "anon");
        filterChainDefinitionMap.put(adminPath + "/**", "user,kickout");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(SessionManager sessionManager,
                                                     RedisCacheManager shiroCacheManage,
                                                     SystemAuthorizingRealm realm) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(shiroCacheManage);
        securityManager.setRealms(ImmutableList.of(realm));
        securityManager.setRememberMeManager(null);
        return securityManager;
    }

    @Bean(name = "sessionManager")
    public SessionManager sessionManager(CacheSessionDAO sessionDAO, Environment env) {
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setGlobalSessionTimeout(Integer.parseInt(env.getProperty("session.sessionTimeout")));
        sessionManager.setSessionValidationInterval(Integer.parseInt(env.getProperty("session.sessionTimeoutClean")));
        sessionManager.setSessionIdCookie(new SimpleCookie("yunyou.session.id"));
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);

        return sessionManager;
    }

    @Bean(name = "sessionDAO")
    public CacheSessionDAO sessionDAO(RedisCacheManager shiroCacheManage) {
        CacheSessionDAO sessionDAO = new CacheSessionDAO();
        sessionDAO.setCacheManager(shiroCacheManage);
        sessionDAO.setActiveSessionsCacheName(CacheNames.SYS_CACHE_ACTIVE_SESSIONS);
        sessionDAO.setSessionIdGenerator(new IdGen());
        return sessionDAO;
    }

    @Bean(name = "shiroCacheManager")
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager();
    }

    @Bean(name = "formAuthenticationFilter")
    public FormAuthenticationFilter formAuthenticationFilter() {
        return new FormAuthenticationFilter();
    }

    @Bean(name = "logoutSessionControlFilter")
    public LogoutSessionControlFilter logoutSessionControlFilter(RedisCacheManager cacheManager, SessionManager sessionManager) {
        LogoutSessionControlFilter filter = new LogoutSessionControlFilter();
        filter.setCacheManager(cacheManager);
        filter.setSessionManager(sessionManager);
        filter.setLogoutUrl(Global.getAdminPath() + "/login");
        return filter;
    }

    @Bean(name = "systemAuthorizingRealm")
    public SystemAuthorizingRealm authorizingRealm() {
        SystemAuthorizingRealm realm = new SystemAuthorizingRealm();
        realm.setName(CacheNames.SYS_CACHE_AUTHORIZE);
        return realm;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean(name = "authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(DefaultWebSecurityManager[] securityManager) {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        bean.setArguments((Object) securityManager);
        return bean;
    }

}