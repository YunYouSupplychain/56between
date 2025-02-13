package com.yunyou.core.security.interceptor;

import com.yunyou.common.utils.AesUtils;
import com.yunyou.core.persistence.annotation.ExtendApi;
import com.yunyou.core.security.util.ApiUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author yunyou
 * @since 2023/3/7 18:18
 */
public class ResponseInterceptor implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        ExtendApi api = methodParameter.getMethodAnnotation(ExtendApi.class);
        if (api == null) {
            api = methodParameter.getDeclaringClass().getAnnotation(ExtendApi.class);
        }
        return api != null;
    }

    @Override
    public Object beforeBodyWrite(Object entity, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (null != entity) {
            ExtendApi api = methodParameter.getDeclaringClass().getAnnotation(ExtendApi.class);
            return AesUtils.ecbEncrypt(entity.toString(), ApiUtils.getServerAesKey(api.appId()));
        }
        return entity;
    }
}