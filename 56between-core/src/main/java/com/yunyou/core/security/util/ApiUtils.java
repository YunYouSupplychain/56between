package com.yunyou.core.security.util;

import com.yunyou.common.config.Global;

/**
 * 应用对接工具类
 *
 * @author yunyou
 * @since 2023/3/8 10:47
 */
public class ApiUtils {

    private static final String API_SERVER_PREFIX = "api.server.";
    private static final String API_CLIENT_PREFIX = "api.client.";
    private static final String PRIVATE_KEY_SUFFIX = ".privateKey";
    private static final String PUBLIC_KEY_SUFFIX = ".publicKey";
    private static final String AES_KEY_SUFFIX = ".aesKey";
    private static final String URL_SUFFIX = ".url";

    /**
     * 获取AES密钥（自身作为服务方）
     *
     * @param requestAppId 请求方应用编号
     */
    public static String getServerAesKey(String requestAppId) {
        return Global.getConfig(API_SERVER_PREFIX + requestAppId + AES_KEY_SUFFIX);
    }

    /**
     * 获取请求方公钥（自身作为服务方）
     *
     * @param requestAppId 请求方应用编号
     */
    public static String getServerPublicKey(String requestAppId) {
        return Global.getConfig(API_SERVER_PREFIX + requestAppId + PUBLIC_KEY_SUFFIX);
    }

    /**
     * 获取私钥（自身作为服务方）
     *
     * @param requestAppId 请求方应用编号
     */
    public static String getServerPrivateKey(String requestAppId) {
        return Global.getConfig(API_SERVER_PREFIX + requestAppId + PRIVATE_KEY_SUFFIX);
    }


    /**
     * 获取私钥（自身作为请求方）
     *
     * @param serviceAppId 服务方应用编号
     */
    public static String getClientPrivateKey(String serviceAppId) {
        return Global.getConfig(API_CLIENT_PREFIX + serviceAppId + PRIVATE_KEY_SUFFIX);
    }

    /**
     * 获取AES密钥（自身作为请求方）
     *
     * @param serviceAppId 服务方应用编号
     */
    public static String getClientAesKey(String serviceAppId) {
        return Global.getConfig(API_CLIENT_PREFIX + serviceAppId + AES_KEY_SUFFIX);
    }

    /**
     * 获取服务方URL（自身作为请求方）
     *
     * @param serviceAppId 服务方应用编号
     */
    public static String getClientUrl(String serviceAppId) {
        return Global.getConfig(API_CLIENT_PREFIX + serviceAppId + URL_SUFFIX);
    }

    /**
     * 获取服务方公钥（自身作为请求方）
     *
     * @param serviceAppId 服务方应用编号
     */
    public static String getClientPublicKey(String serviceAppId) {
        return Global.getConfig(API_CLIENT_PREFIX + serviceAppId + PUBLIC_KEY_SUFFIX);
    }
}
