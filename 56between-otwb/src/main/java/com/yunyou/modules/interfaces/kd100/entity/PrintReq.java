package com.yunyou.modules.interfaces.kd100.entity;

public class PrintReq extends BaseRequest {

    /**
     * 业务类型（默认：getPrintImg）
     */
    private String method;
    /**
     * 快递100分配给贵司的的授权key
     */
    private String key;
    /**
     * 加密签名信息：MD5(param+t+key+secret)；加密后字符串转大写
     */
    private String sign;
    /**
     * 当前请求时间戳
     */
    private String t;
    /**
     * 其他参数
     */
    private String param;
    /**
     * 快递100分配给贵司的的授权key
     */
    private String secret;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
