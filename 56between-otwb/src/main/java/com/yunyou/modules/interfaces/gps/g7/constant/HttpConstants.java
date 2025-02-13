package com.yunyou.modules.interfaces.gps.g7.constant;

public class HttpConstants {
    // 签名Header
    public static final String X_CA_SIGNATURE = "Authorization";

    // JSON类型Content-Type
    public static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";

    // 请求Header Accept
    public static final String HTTP_HEADER_ACCEPT = "Accept";
    // 请求Body内容MD5 Header
    public static final String HTTP_HEADER_CONTENT_MD5 = "Content-MD5";
    // 请求Header Content-Type
    public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    // 请求Header UserAgent
    public static final String HTTP_HEADER_USER_AGENT = "User-Agent";
    // 请求Header Date
    public static final String HTTP_HEADER_G7_TIMESTAMP = "X-G7-OpenAPI-Timestamp";
    // 参与签名的系统Header前缀,只有指定前缀的Header才会参与到签名中
    public static final String CA_HEADER_TO_SIGN_PREFIX_SYSTEM = "X-G7-Ca-";

}
