package com.yunyou.modules.interfaces.yto.entity.createOrder.response;

public class YTOCreateOrderResponseData {

    // 请求结果 true(成功)false（失败）
    private String success;
    // 成功或失败编码
    private String code;
    // 物流公司ID（YTO）
    private String logisticProviderID;
    // 成功绑定的面单号
    private String mailNo;
    // 物流订单号
    private String txLogisticID;
    // 客户编码
    private String clientID;
    //
    private YTOCreateOrderResponseDistributeInfo distributeInfo;
    // 始发网点编码
    private String originateOrgCode;
    // 二维码
    private String qrCode;
    // 失败原因
    private String reason;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogisticProviderID() {
        return logisticProviderID;
    }

    public void setLogisticProviderID(String logisticProviderID) {
        this.logisticProviderID = logisticProviderID;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getTxLogisticID() {
        return txLogisticID;
    }

    public void setTxLogisticID(String txLogisticID) {
        this.txLogisticID = txLogisticID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public YTOCreateOrderResponseDistributeInfo getDistributeInfo() {
        return distributeInfo;
    }

    public void setDistributeInfo(YTOCreateOrderResponseDistributeInfo distributeInfo) {
        this.distributeInfo = distributeInfo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOriginateOrgCode() {
        return originateOrgCode;
    }

    public void setOriginateOrgCode(String originateOrgCode) {
        this.originateOrgCode = originateOrgCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
