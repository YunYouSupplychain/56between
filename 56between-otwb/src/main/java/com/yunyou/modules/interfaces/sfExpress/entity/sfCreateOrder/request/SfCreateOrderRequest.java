package com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.request;

import java.math.BigDecimal;
import java.util.List;

public class SfCreateOrderRequest {

    private String appId;                   // *
    private String requestId;               // *请求id
    private String timestamp;               // *请求时间戳毫秒
    private String sign;                    // *签名
    private String customNumber;            // 月结卡号
    private String orderNumber;             // 订单号
    private String mailNumber;              // 运单号
    private String senderCompany;           // 发件公司
    private String senderName;              // *发件人
    private String senderTel;               // *发件电话
    private String senderAddress;           // *发件地址
    private String receiverCompany;         // 收件公司
    private String receiverName;            // *收件人
    private String receiverTel;             // *收件电话
    private String receiverAddress;         // *收件地址
    private String payMethod;               // *付款方式(1:寄方付 2:收方付 3:第三方付)
    private String expressType;             // *快件产品类别
    private Integer parcelQuantity;         // 包裹数
    private String sendStartTime;           // 要求上门取件开始时间(YYYY-MM-DD HH24:MM:SS)
    private String remark;                  // 备注
    private List<Cargo> cargo;              // *货物
    private List<AddedService> addedService;// *增值服务
    private List<Extra> extra;              // *扩展
    private BigDecimal cargoTotalWeight;    // 货物总重量

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

    public String getCustomNumber() {
        return customNumber;
    }

    public void setCustomNumber(String customNumber) {
        this.customNumber = customNumber;
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

    public String getSenderCompany() {
        return senderCompany;
    }

    public void setSenderCompany(String senderCompany) {
        this.senderCompany = senderCompany;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderTel() {
        return senderTel;
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverCompany() {
        return receiverCompany;
    }

    public void setReceiverCompany(String receiverCompany) {
        this.receiverCompany = receiverCompany;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getExpressType() {
        return expressType;
    }

    public void setExpressType(String expressType) {
        this.expressType = expressType;
    }

    public Integer getParcelQuantity() {
        return parcelQuantity;
    }

    public void setParcelQuantity(Integer parcelQuantity) {
        this.parcelQuantity = parcelQuantity;
    }

    public String getSendStartTime() {
        return sendStartTime;
    }

    public void setSendStartTime(String sendStartTime) {
        this.sendStartTime = sendStartTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Cargo> getCargo() {
        return cargo;
    }

    public void setCargo(List<Cargo> cargo) {
        this.cargo = cargo;
    }

    public List<AddedService> getAddedService() {
        return addedService;
    }

    public void setAddedService(List<AddedService> addedService) {
        this.addedService = addedService;
    }

    public List<Extra> getExtra() {
        return extra;
    }

    public void setExtra(List<Extra> extra) {
        this.extra = extra;
    }

    public BigDecimal getCargoTotalWeight() {
        return cargoTotalWeight;
    }

    public void setCargoTotalWeight(BigDecimal cargoTotalWeight) {
        this.cargoTotalWeight = cargoTotalWeight;
    }
}
