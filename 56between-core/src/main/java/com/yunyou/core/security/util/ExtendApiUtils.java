package com.yunyou.core.security.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.http.HttpClientUtil;
import com.yunyou.common.utils.AesUtils;
import com.yunyou.common.utils.SignatureUtil;
import com.yunyou.core.security.ApplicationID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 外部接口工具类
 *
 * @author liujianhua
 */
public class ExtendApiUtils {

    private static final Logger log = LoggerFactory.getLogger(ExtendApiUtils.class);

    private static final String URL = AesUtils.ecbDecrypt(ApiUtils.getClientUrl(ApplicationID.AUTH_PLATFORM), ApiUtils.getClientAesKey(ApplicationID.AUTH_PLATFORM));

    public static String post(String params, String serviceName) {
        String responseMsg = HttpClientUtil.getInstance().sendHttpPost(URL + serviceName, getParams(params, serviceName));
        return AesUtils.ecbDecrypt(responseMsg, ApiUtils.getClientAesKey(ApplicationID.AUTH_PLATFORM));
    }

    public static void postAsync(String params, String serviceName) {
        HttpClientUtil.getInstance().sendHttpPostAsync(URL + serviceName, getParams(params, serviceName));
    }

    private static String getParams(String params, String serviceName) {
        String plain = serviceName + System.currentTimeMillis() + params;
        String reqData = AesUtils.ecbEncrypt(plain, ApiUtils.getClientAesKey(ApplicationID.AUTH_PLATFORM));
        String sign;
        try {
            sign = SignatureUtil.sign(reqData, ApiUtils.getClientPrivateKey(ApplicationID.AUTH_PLATFORM));
        } catch (Exception e) {
            log.error("rsa签名异常", e);
            throw new GlobalException(e);
        }
        return JSONObject.toJSONString(ImmutableMap.of("appId", ApplicationID.BACK_END, "reqData", reqData, "sign", sign));
    }

}