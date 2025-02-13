package com.yunyou.modules.interfaces.sfExpress.entity.sfRouteQuery.request;

public class SfRouteQueryRequest {

    private String appId;
    private String requestId;       // 请求id
    private String timestamp;       // 请求时间戳毫秒
    private String sign;            // 签名
    private String orderNumber;     // 运单号
    private String mailNumber;      // 订单号

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getMailNumber() {
        return mailNumber;
    }

    public void setMailNumber(String mailNumber) {
        this.mailNumber = mailNumber;
    }
}
