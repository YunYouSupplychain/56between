package com.yunyou.modules.sys.security;

import com.yunyou.common.config.CacheNames;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.RedisUtils;
import com.yunyou.core.security.shiro.session.SessionManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * shiro 自定义filter 实现 并发登录控制
 */
public class LogoutSessionControlFilter extends AccessControlFilter {

    /**
     * 踢出后到的地址
     */
    private String logoutUrl;

    /**
     * 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
     */
    private boolean logoutAfter = false;

    /**
     * 同一个帐号最大会话数 默认1
     */
    private int maxSession = 1;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public void setLogoutAfter(boolean logoutAfter) {
        this.logoutAfter = logoutAfter;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-activeSessionCache");
    }

    /**
     * 是否允许访问，返回true表示允许
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    /**
     * 表示访问拒绝时是否自己处理，如果返回true表示自己不处理且继续拦截器链执行，返回false表示自己已经处理了（比如重定向到另一个页面）。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (Global.isDemoMode()) {
            return true;
        }

        Subject subject = getSubject(request, response);
        // 如果没有登录，直接进行之后的流程
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }

        Session session = subject.getSession();
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) subject.getPrincipal();
        String username = principal.getLoginName();
        Serializable sessionId = session.getId();
        String key = CacheNames.USER_CACHE_ONLINE_USER_NAME + username;

        // 初始化用户的队列放到缓存里
        RedisUtils redisUtils = RedisUtils.getInstance();
        Deque<Serializable> deque = (Deque<Serializable>) redisUtils.get(key);
        if (deque == null) {
            deque = new LinkedList<>();
            redisUtils.set(key, deque);
        }

        // 如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute("logout") == null) {
            deque.push(sessionId);
            redisUtils.set(key, deque);
        }
        // 如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable logoutSessionId = deque.removeLast();
            try {
                Session logoutSession = sessionManager.getSession(new DefaultSessionKey(logoutSessionId));
                if (logoutSession != null) {
                    // 设置会话的logout属性表示踢出了
                    logoutSession.setAttribute("logout", true);
                }
            } catch (Exception ignored) {
            }
        }

        // 如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("logout") != null) {
            // 会话被踢出了
            try {
                subject.logout();
            } catch (Exception ignored) {
            }
            if (principal.isMobileLogin()) {
                AjaxJson j = new AjaxJson();
                j.setSuccess(false);
                j.setErrorCode("0");
                j.setMsg("登录已过期!");
                renderString((HttpServletResponse) response, j.getJsonStr());
            } else {
                WebUtils.issueRedirect(request, response, logoutUrl);
            }
            return false;
        }

        return true;
    }

    protected void renderString(HttpServletResponse response, String string) {
        try {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException ignored) {
        }
    }
}