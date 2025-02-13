package com.yunyou.modules.interfaces.kd100;

import org.apache.commons.codec.digest.DigestUtils;

public class SignUtils {

    /**
     * 快递100加密方式统一为MD5后转大写
     */
    public static String sign(String msg) {
        return DigestUtils.md5Hex(msg).toUpperCase();
    }

    /**
     * 查询加密
     *
     * @param param
     * @param key
     * @param customer
     * @return
     */
    public static String querySign(String param, String key, String customer) {
        return sign(param + key + customer);
    }

    /**
     * 打印/下单 加密
     *
     * @param param
     * @param t
     * @param key
     * @param secret
     */
    public static String printSign(String param, String t, String key, String secret) {
        return sign(param + t + key + secret);
    }

    /**
     * 云平台 加密
     *
     * @param key
     * @param secret
     */
    public static String cloudSign(String key, String secret) {
        return sign(key + secret);
    }

    /**
     * 短信加密
     *
     * @param key
     * @param userId
     */
    public static String smsSign(String key, String userId) {
        return sign(key + userId);
    }
}
