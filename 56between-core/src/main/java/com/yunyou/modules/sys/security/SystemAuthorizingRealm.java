package com.yunyou.modules.sys.security;

import com.yunyou.common.config.Global;
import com.yunyou.common.utils.Encodes;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.core.web.Servlets;
import com.yunyou.modules.sys.entity.Menu;
import com.yunyou.modules.sys.entity.Role;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.SystemService;
import com.yunyou.modules.sys.utils.LogUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 系统安全认证实现类
 *
 * @author yunyou
 * @version 2017-7-5
 */
public class SystemAuthorizingRealm extends AuthorizingRealm {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private SystemService systemService;

    @PostConstruct
    public void initCredentialsMatcher() {
        // 设定密码校验的Hash算法与迭代次数
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
        matcher.setHashIterations(SystemService.HASH_INTERACTIONS);
        setCredentialsMatcher(matcher);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;

        int activeSessionSize = getSystemService().getSessionDao().getActiveSessions(false).size();
        if (logger.isDebugEnabled()) {
            logger.debug("login submit, active session size: {}, username: {}", activeSessionSize, token.getUsername());
        }

        // 校验用户名密码
        User user = getSystemService().getUserByLoginName(token.getUsername());
        if (user != null) {
            if (Global.NO.equals(user.getLoginFlag())) {
                throw new AuthenticationException("msg:该已帐号禁止登录.");
            }
            byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
            return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin()),
                    user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
        } else {
            return null;
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) getAvailablePrincipal(principals);
        // 获取当前已登录的用户
        if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
            Collection<Session> sessions = getSystemService().getSessionDao().getActiveSessions(true, principal, UserUtils.getSession());
            if (!sessions.isEmpty()) {
                // 如果是登录进来的，则踢出已在线用户
                if (UserUtils.getSubject().isAuthenticated()) {
                    for (Session session : sessions) {
                        getSystemService().getSessionDao().delete(session);
                    }
                } else {
                    // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
                    UserUtils.getSubject().logout();
                    throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
                }
            }
        }
        User user = getSystemService().getUserByLoginName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<Menu> list = UserUtils.getMenuList();
            for (Menu menu : list) {
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    // 添加基于Permission的权限信息
                    for (String permission : StringUtils.split(menu.getPermission(), ",")) {
                        info.addStringPermission(permission);
                    }
                }
            }
            // 添加用户权限
            info.addStringPermission("user");
            // 添加用户角色信息
            for (Role role : user.getRoleList()) {
                info.addRole(role.getEnname());
            }
            // 更新登录IP和时间
            getSystemService().updateUserLoginInfo(user);
            // 记录登录日志
            LogUtils.saveLog(Servlets.getRequest(), "系统登录");
            return info;
        } else {
            return null;
        }
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    private void authorizationValidate(Permission permission) {

    }

    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id; // 编号
        private String loginName; // 登录名
        private String name; // 姓名
        private boolean mobileLogin; // 是否手机登录

        public Principal(User user, boolean mobileLogin) {
            this.id = user.getId();
            this.loginName = user.getLoginName();
            this.name = user.getName();
            this.mobileLogin = mobileLogin;
        }

        public String getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getName() {
            return name;
        }

        public boolean isMobileLogin() {
            return mobileLogin;
        }

        /**
         * 获取SESSIONID
         */
        public String getSessionid() {
            try {
                return (String) UserUtils.getSession().getId();
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        public String toString() {
            return id;
        }

    }

    public SystemService getSystemService() {
        if (systemService == null) {
            systemService = SpringContextHolder.getBean(SystemService.class);
        }
        return systemService;
    }

}
