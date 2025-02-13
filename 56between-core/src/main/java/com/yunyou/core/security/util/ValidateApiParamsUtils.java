package com.yunyou.core.security.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.AesUtils;
import com.yunyou.common.utils.RedisUtils;
import com.yunyou.common.utils.SignatureUtil;
import com.yunyou.common.utils.number.NumberUtil;
import com.yunyou.core.persistence.ApiParams;
import com.yunyou.core.persistence.annotation.ExtendApi;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @author yunyou
 * @since 2023/3/7 18:12
 */
public class ValidateApiParamsUtils {

    public static void validateIp(String apiName, String ip, ExtendApi es) {
        String key = "request:limit:" + apiName + ":ip::".concat(ip);
        int seconds = es.seconds(), maxCount = es.maxCount();
        RedisUtils redisUtils = RedisUtils.getInstance();
        long count = redisUtils.incrBy(key, 1);
        if (1 == count) {
            redisUtils.expire(key, seconds);
        }
        if (count > maxCount) {
            throw new GlobalException("用户IP访问超过了次数限制");
        }
    }

    public static void validateParams(HttpServletRequest request, String appId, String serviceName, String publicKey, String aesKey) throws Exception {
        String params = getBodyString(request);
        try {
            JSONObject.parseObject(params);
        } catch (JSONException e) {
            throw new GlobalException("非法参数!");
        }
        ApiParams apiParams = JSONObject.parseObject(params, ApiParams.class);
        if (StringUtils.isBlank(apiParams.getAppId()) || StringUtils.isBlank(apiParams.getReqData()) || StringUtils.isBlank(apiParams.getSign())) {
            throw new GlobalException("非法参数!");
        }
        if (!StringUtils.equals(appId, apiParams.getAppId())) {
            throw new GlobalException("appId非法!");
        }
        String reqData = AesUtils.ecbDecrypt(apiParams.getReqData(), aesKey);
        if (!reqData.startsWith(serviceName)) {
            throw new GlobalException("serviceName非法!");
        }
        String timestamp = reqData.substring(serviceName.length(), serviceName.length() + 13);
        if (!NumberUtil.isNumber(timestamp)) {
            throw new GlobalException("timestamp非法!");
        }
        long differTimestamp = System.currentTimeMillis() - Long.parseLong(timestamp);
        if (differTimestamp > 15000 || differTimestamp < 0) {
            throw new GlobalException("非法参数!");
        }
        boolean flag = SignatureUtil.verify(apiParams.getReqData(), apiParams.getSign(), publicKey);
        if (!flag) {
            throw new GlobalException("非法签名!");
        }
    }

    public static void render(HttpServletResponse response, String msg) throws IOException {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(400);
        response.getWriter().write(msg);
    }

    private static String getBodyString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }
}
