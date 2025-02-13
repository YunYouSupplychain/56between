package com.yunyou.modules.sys.web;

import com.google.common.collect.Maps;
import com.yunyou.common.config.CacheNames;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.CookieUtils;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.RedisUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.security.shiro.session.SessionDAO;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.security.FormAuthenticationFilter;
import com.yunyou.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunyou.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 登录Controller
 */
@Controller
public class LoginController extends BaseController {
    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 管理登录
     */
    @RequestMapping(value = "${adminPath}/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Principal principal = UserUtils.getPrincipal();
        if (logger.isDebugEnabled()) {
            logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }
        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
            CookieUtils.setCookie(response, "LOGINED", "false");
        }
        // 如果已经登录，则跳转到管理首页
        if (principal != null && !principal.isMobileLogin()) {
            return "redirect:" + adminPath;
        }
        // 获取跳转到login之前的URL
        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        // 如果是手机没有登录跳转到到login，则返回JSON字符串
        if (savedRequest != null) {
            String queryStr = savedRequest.getQueryString();
            if (queryStr != null && (queryStr.contains("__ajax") || queryStr.contains("mobileLogin"))) {
                AjaxJson j = new AjaxJson();
                j.setSuccess(false);
                j.setErrorCode("0");
                j.setMsg("没有登录!");
                return renderString(response, j);
            }
        }
        return "modules/sys/login/sysLogin";
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        Principal principal = UserUtils.getPrincipal();
        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            return "redirect:" + adminPath;
        }
        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
            message = "用户或密码错误!";
        }
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
        if (logger.isDebugEnabled()) {
            logger.debug("login fail, active session size: {}, message: {}, exception: {}", sessionDAO.getActiveSessions(false).size(), message, exception);
        }
        // 验证失败清空验证码
        request.getSession().setAttribute(FormAuthenticationFilter.DEFAULT_CAPTCHA_PARAM, IdGen.uuid());
        // 如果是手机登录，则返回JSON字符串
        if (mobile) {
            AjaxJson j = new AjaxJson();
            j.setSuccess(false);
            j.setMsg(message);
            j.put("username", username);
            j.put("name", "");
            j.put("mobileLogin", mobile);
            j.put("JSESSIONID", "");
            return renderString(response, j.getJsonStr());
        }
        return "modules/sys/login/sysLogin";
    }

    /**
     * 管理登录
     */
    @RequestMapping(value = "${adminPath}/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        Principal principal = UserUtils.getPrincipal();
        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            UserUtils.getSubject().logout();
        }
        // 如果是手机客户端退出, 则返回JSON字符串
        String queryStr = request.getQueryString();
        if (queryStr != null && (queryStr.contains("__ajax") || queryStr.contains("mobileLogin"))) {
            AjaxJson j = new AjaxJson();
            j.setMsg("退出成功!");
            return renderString(response, j);
        }
        return "redirect:" + adminPath + "/login";
    }

    /**
     * 登录成功，进入管理首页
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "${adminPath}")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        // 登录成功后，验证码计算器清零
        // isValidateCodeLogin(principal.getLoginName(), false, true);
        if (logger.isDebugEnabled()) {
            logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }
        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
            String logined = CookieUtils.getCookie(request, "LOGINED");
            if (StringUtils.isBlank(logined) || "false".equals(logined)) {
                CookieUtils.setCookie(response, "LOGINED", "true");
            } else if (StringUtils.equals(logined, "true")) {
                UserUtils.getSubject().logout();
                return "redirect:" + adminPath + "/login";
            }
        }
        // 如果是手机登录，则返回JSON字符串
        if (principal.isMobileLogin()) {
            if (request.getParameter("login") != null) {
                return renderString(response, principal);
            }
            if (request.getParameter("index") != null) {
                return "modules/sys/login/sysIndex";
            }
            return "redirect:" + adminPath + "/login";
        }
        if (UserUtils.getMenuList().size() == 0) {
            return "modules/sys/login/noAuth";
        }
        return "modules/sys/login/sysIndex";
    }

    /**
     * 获取主题方案
     */
    @RequestMapping(value = "/theme/{theme}")
    public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNotBlank(theme)) {
            CookieUtils.setCookie(response, "theme", theme);
        }
        return "redirect:" + request.getParameter("url");
    }

    /**
     * 是否启用tab
     */
    @RequestMapping(value = "/tab/{tab}")
    public String getTabInCookie(@PathVariable String tab, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNotBlank(tab)) {
            CookieUtils.setCookie(response, "tab", tab);
        }
        return "redirect:" + request.getParameter("url");
    }

    /**
     * 是否是验证码登录
     *
     * @param userName 用户名
     * @param isFail   计数加1
     * @param clean    计数清零
     */
    @SuppressWarnings("unchecked")
    public static boolean isValidateCodeLogin(String userName, boolean isFail, boolean clean) {
        RedisUtils redisUtils = RedisUtils.getInstance();
        Map<String, Integer> loginFailMap = (Map<String, Integer>) redisUtils.get(CacheNames.USER_CACHE_LOGIN_FAIL_MAP);
        if (loginFailMap == null) {
            loginFailMap = Maps.newHashMap();
            redisUtils.set(CacheNames.USER_CACHE_LOGIN_FAIL_MAP, loginFailMap);
        }
        Integer loginFailNum = loginFailMap.get(userName);
        if (loginFailNum == null) {
            loginFailNum = 0;
        }
        if (isFail) {
            loginFailNum++;
            loginFailMap.put(userName, loginFailNum);
        }
        if (clean) {
            loginFailMap.remove(userName);
        }
        return loginFailNum >= 3;
    }

}
