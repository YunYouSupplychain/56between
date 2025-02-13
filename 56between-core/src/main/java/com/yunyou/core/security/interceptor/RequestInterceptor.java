package com.yunyou.core.security.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.yunyou.common.utils.AesUtils;
import com.yunyou.core.persistence.ApiParams;
import com.yunyou.core.persistence.annotation.ExtendApi;
import com.yunyou.core.security.util.ApiUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author yunyou
 * @since 2023/3/7 18:16
 */
public class RequestInterceptor implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        ExtendApi api = methodParameter.getMethodAnnotation(ExtendApi.class);
        if (api == null) {
            api = methodParameter.getDeclaringClass().getAnnotation(ExtendApi.class);
        }
        return api != null;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        String bodyStr = IOUtils.toString(httpInputMessage.getBody(), String.valueOf(StandardCharsets.UTF_8));
        String serviceName = methodParameter.getMethod().getName();
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                ApiParams apiParams = JSONObject.parseObject(bodyStr, ApiParams.class);
                String jsonString = AesUtils.ecbDecrypt(apiParams.getReqData(), ApiUtils.getServerAesKey(apiParams.getAppId())).substring(serviceName.length() + 13);
                return new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public HttpHeaders getHeaders() {
                return httpInputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }
}