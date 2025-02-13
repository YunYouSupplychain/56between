package com.yunyou.core.security.interceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunyou.common.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class ParamsRequestInterceptor implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        String bodyStr = IOUtils.toString(httpInputMessage.getBody(), String.valueOf(StandardCharsets.UTF_8));
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                String key = "dataScope";
                String jsonString = null;
                if ("java.util.List".equals(methodParameter.getParameterType().getName())) {
                    JSONArray objects = JSONObject.parseArray(bodyStr);
                    for (Object object : objects) {
                        JSONObject next = (JSONObject) object;
                        if (next.containsKey(key) && StringUtils.isNotBlank(next.getString(key))) {
                            next.put(key, null);
                        }
                    }
                    jsonString = objects.toJSONString();
                } else {
                    JSONObject jsonObject = JSONObject.parseObject(bodyStr);
                    if (jsonObject.containsKey(key) && StringUtils.isNotBlank(jsonObject.getString(key))) {
                        jsonObject.put(key, null);
                    }
                    jsonString = jsonObject.toJSONString();
                }
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