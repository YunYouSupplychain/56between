package com.yunyou.modules.interfaces.gps.g7.constant;


import com.yunyou.common.config.Global;

/**
 * 常量类 - G7 GPS
 */
public class Constants {
    // 签名算法HmacSha256
    public static final String HMAC_SHA256 = "HmacSHA256";
    // UserAgent
    public static final String USER_AGENT = "altair.G7.java";
    // 认证前缀
    public static final String AUTH_PREFIX = "g7ac";
    // 沙箱环境
    /*private static final String BASE_URL = "demo.dsp.chinawayltd.com/altair/rest/";*/
    // 正式环境
    public static final String BASE_URL = Global.getConfig("gps.g7.url");
    // APP KEY
    public static final String ACCESS_ID = Global.getConfig("gps.g7.accessId");
    // APP密钥
    public static final String SECRET_KEY = Global.getConfig("gps.g7.secretKey");
}
