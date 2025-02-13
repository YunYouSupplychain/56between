package com.yunyou.modules.sys.security;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunyou.modules.sys.utils.UserUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 表单验证（包含验证码）过滤类
 *
 * @author yunyou
 * @version 2017-5-19
 */
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

    public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
    public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
    public static final String DEFAULT_MESSAGE_PARAM = "message";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
    private String messageParam = DEFAULT_MESSAGE_PARAM;

    public String getCaptchaParam() {
        return captchaParam;
    }

    public String getMobileLoginParam() {
        return mobileLoginParam;
    }

    public String getMessageParam() {
        return messageParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected boolean isMobileLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getMobileLoginParam());
    }

    public void write(HttpServletResponse response, String content) {
        response.reset();
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter pw = response.getWriter();
            pw.write(content);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        if (password == null) {
            password = "";
        }
        boolean rememberMe = isRememberMe(request);
        String host = StringUtils.getRemoteAddr((HttpServletRequest) request);
        String captcha = getCaptcha(request);
        boolean mobile = isMobileLogin(request);
        return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, mobile);
    }

    @Override
    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        Principal p = UserUtils.getPrincipal();
        if (p != null && !p.isMobileLogin()) {
            WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
        } else if (p != null) {
            AjaxJson j = new AjaxJson();
            j.setSuccess(true);
            j.setMsg("登录成功!");
            j.put("username", p.getLoginName());
            j.put("name", p.getName());
            j.put("mobileLogin", p.isMobileLogin());
            j.put("JSESSIONID", p.getSessionid());
            write((HttpServletResponse) response, j.getJsonStr());
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        String className = e.getClass().getName(), message;
        if (IncorrectCredentialsException.class.getName().equals(className)
                || UnknownAccountException.class.getName().equals(className)) {
            message = "用户或密码错误, 请重试！";
            request.setAttribute(getFailureKeyAttribute(), className);
        } else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
            message = StringUtils.replace(e.getMessage(), "msg:", "");
        } else {
            message = "系统出现点问题，请稍后再试！";
            e.printStackTrace();
        }
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 校验登录验证码
        /*boolean mobileLogin = isMobileLogin(request);
        String username = getUsername(request);
        String validateCode = getCaptcha(request);
        if (!mobileLogin && LoginController.isValidateCodeLogin(username, false, false)) {
            Session session = SecurityUtils.getSubject().getSession();
            String code = (String) session.getAttribute(DEFAULT_CAPTCHA_PARAM);
            if (validateCode == null || !validateCode.toUpperCase().equals(code)) {
                throw new AuthenticationException("msg:验证码错误, 请重试.");
            }
        }*/
        return super.onAccessDenied(request, response, mappedValue);
    }
}