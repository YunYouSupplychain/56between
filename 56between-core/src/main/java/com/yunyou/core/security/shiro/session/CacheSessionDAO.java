package com.yunyou.core.security.shiro.session;

import com.google.common.collect.Sets;
import com.yunyou.common.config.Global;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.core.web.Servlets;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * 系统安全认证实现类
 *
 * @author yunyou
 * @version 2017-7-24
 */
public class CacheSessionDAO extends EnterpriseCacheSessionDAO implements SessionDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CacheSessionDAO() {
        super();
    }

    @Override
    protected Serializable doCreate(Session session) {
        HttpServletRequest request = Servlets.getRequest();
        if (request != null) {
            String uri = request.getServletPath();
            // 如果是静态文件，则不创建SESSION
            if (Servlets.isStaticFile(uri)) {
                return null;
            }
        }
        super.doCreate(session);
        logger.debug("doCreate {} {}", session, request != null ? request.getRequestURI() : "");
        return session.getId();
    }

    @Override
    protected void doUpdate(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }

        HttpServletRequest request = Servlets.getRequest();
        if (request != null) {
            String uri = request.getServletPath();
            // 如果是静态文件，则不更新SESSION
            if (Servlets.isStaticFile(uri)) {
                return;
            }
            // 如果是视图文件，则不更新SESSION
            if (StringUtils.startsWith(uri, Global.getConfig("web.view.prefix"))
                    && StringUtils.endsWith(uri, Global.getConfig("web.view.suffix"))) {
                return;
            }
            // 手动控制不更新SESSION
            String updateSession = request.getParameter("updateSession");
            if (Global.FALSE.equals(updateSession) || Global.NO.equals(updateSession)) {
                return;
            }
        }
        super.doUpdate(session);
        logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : "");
    }

    @Override
    protected void doDelete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }

        super.doDelete(session);
        logger.debug("delete {} ", session.getId());
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return super.doReadSession(sessionId);
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        try {
            Session s = null;
            HttpServletRequest request = Servlets.getRequest();
            if (request != null) {
                String uri = request.getServletPath();
                // 如果是静态文件，则不获取SESSION
                if (Servlets.isStaticFile(uri)) {
                    return null;
                }
                s = (Session) request.getAttribute("session_" + sessionId);
            }
            if (s != null) {
                return s;
            }

            Session session = super.readSession(sessionId);
            logger.debug("readSession {} {}", sessionId, request != null ? request.getRequestURI() : "");

            if (request != null && session != null) {
                request.setAttribute("session_" + sessionId, session);
            }

            return session;
        } catch (UnknownSessionException e) {
            return null;
        }
    }

    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave) {
        return getActiveSessions(includeLeave, null, null);
    }

    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
        // 如果包括离线，并无登录者条件。
        if (includeLeave && principal == null) {
            return getActiveSessions();
        }
        Set<Session> sessions = Sets.newHashSet();
        if (null == getActiveSessions()) {
            return sessions;
        }
        for (Session session : getActiveSessions()) {
            // 不包括离线并符合最后访问时间小于等于3分钟条件
            boolean isActiveSession = includeLeave || DateUtils.pastMinutes(session.getLastAccessTime()) <= 3;
            // 符合登陆者条件。
            if (principal != null) {
                PrincipalCollection pc = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                if (principal.toString().equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY)) {
                    isActiveSession = true;
                }
            }
            // 过滤掉的SESSION
            if (filterSession != null && filterSession.getId().equals(session.getId())) {
                isActiveSession = false;
            }
            if (isActiveSession) {
                sessions.add(session);
            }
        }
        return sessions;
    }

}
