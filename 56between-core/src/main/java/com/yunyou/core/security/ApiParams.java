package com.yunyou.core.security;

/**
 * TODO
 *
 * @author yunyou
 * @since 2023/3/7 18:18
 */
public class ApiParams {
    /**
     * 业务请求报文json格式
     */
    private String reqData;
    /**
     * 签名值
     */
    private String sign;
    /**
     * 接入编号
     */
    private String appId;

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}