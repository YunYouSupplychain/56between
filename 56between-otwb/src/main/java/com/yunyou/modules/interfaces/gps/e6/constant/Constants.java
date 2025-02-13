package com.yunyou.modules.interfaces.gps.e6.constant;

import com.yunyou.common.config.Global;

/**
 * 常量类 - 易流GPS
 */
public class Constants {

    public static final String BASE_URL = Global.getConfig("gps.e6.url");
    // 示例公钥和密钥
    /*public static final String ACCESS_ID = "abe10b89-8185-483a-b28f-894f7957f78d";
    public static final String SECRET_KEY = "184CD4DF-C5D2-4F55-8539-ED45D4E91FBB";*/
    // 正式公钥和密钥
    public static final String ACCESS_ID = Global.getConfig("gps.e6.accessId");
    public static final String SECRET_KEY = Global.getConfig("gps.e6.secretKey");
}
