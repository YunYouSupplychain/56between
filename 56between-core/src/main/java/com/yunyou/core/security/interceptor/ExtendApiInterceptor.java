package com.yunyou.core.security.interceptor;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.annotation.ExtendApi;
import com.yunyou.core.security.util.ApiUtils;
import com.yunyou.core.security.util.ValidateApiParamsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @author yunyou
 * @since 2023/3/7 18:15
 */
public class ExtendApiInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(ExtendApiInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ExtendApi classAnnotation = handlerMethod.getMethod().getDeclaringClass().getAnnotation(ExtendApi.class);
            ExtendApi methodAnnotation = handlerMethod.getMethod().getAnnotation(ExtendApi.class);
            if (null == classAnnotation || null == methodAnnotation) {
                return true;
            }
            String ip = StringUtils.getRemoteAddr(request);
            String apiName = this.getApiName(request.getServletPath());
            String appId = classAnnotation.appId();
            String serviceName = handlerMethod.getMethod().getName();
            try {
                ValidateApiParamsUtils.validateIp(apiName, ip, methodAnnotation);
                ValidateApiParamsUtils.validateParams(request, appId, serviceName, ApiUtils.getServerPublicKey(appId), ApiUtils.getServerAesKey(appId));
            } catch (GlobalException e) {
                log.info("外部[ip:" + ip + "]访问异常：" + e.getMessage());
                ValidateApiParamsUtils.render(response, e.getMessage());
                return false;
            } catch (Exception e) {
                log.info("外部[ip:" + ip + "]访问异常：签名错误!");
                ValidateApiParamsUtils.render(response, "非法签名!");
                return false;
            }
        }

        return true;
    }

    private String getApiName(String requestUri) {
        if (requestUri.startsWith("/api/")) {
            int i = requestUri.indexOf("/", 5);
            if (i != -1) {
                return requestUri.substring(5, i);
            }
        }
        return "***";
    }
}
