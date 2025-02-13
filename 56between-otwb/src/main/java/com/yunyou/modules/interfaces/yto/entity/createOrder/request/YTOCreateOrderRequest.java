package com.yunyou.modules.interfaces.yto.entity.createOrder.request;

import java.util.Date;

public class YTOCreateOrderRequest {

    // 商家代码     **必填
    private String clientID;
    // 物流公司ID（必须为YTO）   **必填
    private String logisticProviderID;
    // 商家代码，同 clientID 一致    **必填
    private String customerId;
    // 物流订单号    **必填
    // 只能包含大小写字母， 数字和-（减号），且字符串长度不能大于 64
    //  注：clientID +数字，是圆通和电商平台的订单通讯标识，
    //      每个订单的txLogisticID必须唯一，
    //      最后一位必须是数字
    //      （拉取代收货款的单号，相同的订单号前两次会返回不同的运单号，之后会提示单号重复）
    private String txLogisticID;
    // 拉单渠道，同 clientID 一致    **必填
    private String tradeNo;
    // 物流运单号
    private String mailNo;
    // 订单类型（预留）
    private Integer type;
    // 订单类型(0-COD,1-普通订单,2-便携式订单,3-退货单,4-到付)    **必填
    private Integer orderType;
    // 服务类型(1-上门揽收, 0-自己联系)。默认为0      **必填
    private Long serviceType;
    // 订单flag标识（无意义）
    private Integer flag;
    // 发件人信息        **必填
    private YTOCreateOrderSenderRequest sender;
    // 收件人信息        **必填
    private YTOCreateOrderReceiverRequest receiver;
    // 物流公司上门取货时间段，通过”yyyy-MM-dd HH:mm:ss”格式化
    private Date sendStartTime;
    private Date sendEndTime;
    // 商品金额，包括优惠和运费，但无服务费
    private Double goodsValue;
    // goodsValue + 总服务费
    private Double totalValue;
    // 代收金额
    private Double agencyFund;
    // 货物价值
    private Double itemsValue;
    // 货物总重量
    private Double itemsWeight;
    // 商品名称         **必填
    private String itemName;
    // 商品数量         **必填
    private Integer number;
    // 商品单价（两位小数）
    private Double itemValue;
    // 商品类型
    private Integer special;
    // 备注
    private String remark;
    // 保值金额
    private Double insuranceValue;
    // 保值金额 = insuranceValue * 货品数量
    private Double totalServiceFee;
    // 物流公司分润[COD]
    private Double codSplitFee;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getLogisticProviderID() {
        return logisticProviderID;
    }

    public void setLogisticProviderID(String logisticProviderID) {
        this.logisticProviderID = logisticProviderID;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTxLogisticID() {
        return txLogisticID;
    }

    public void setTxLogisticID(String txLogisticID) {
        this.txLogisticID = txLogisticID;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getServiceType() {
        return serviceType;
    }

    public void setServiceType(Long serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public YTOCreateOrderSenderRequest getSender() {
        return sender;
    }

    public void setSender(YTOCreateOrderSenderRequest sender) {
        this.sender = sender;
    }

    public YTOCreateOrderReceiverRequest getReceiver() {
        return receiver;
    }

    public void setReceiver(YTOCreateOrderReceiverRequest receiver) {
        this.receiver = receiver;
    }

    public Date getSendStartTime() {
        return sendStartTime;
    }

    public void setSendStartTime(Date sendStartTime) {
        this.sendStartTime = sendStartTime;
    }

    public Date getSendEndTime() {
        return sendEndTime;
    }

    public void setSendEndTime(Date sendEndTime) {
        this.sendEndTime = sendEndTime;
    }

    public Double getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(Double goodsValue) {
        this.goodsValue = goodsValue;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Double getAgencyFund() {
        return agencyFund;
    }

    public void setAgencyFund(Double agencyFund) {
        this.agencyFund = agencyFund;
    }

    public Double getItemsValue() {
        return itemsValue;
    }

    public void setItemsValue(Double itemsValue) {
        this.itemsValue = itemsValue;
    }

    public Double getItemsWeight() {
        return itemsWeight;
    }

    public void setItemsWeight(Double itemsWeight) {
        this.itemsWeight = itemsWeight;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getItemValue() {
        return itemValue;
    }

    public void setItemValue(Double itemValue) {
        this.itemValue = itemValue;
    }

    public Integer getSpecial() {
        return special;
    }

    public void setSpecial(Integer special) {
        this.special = special;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getInsuranceValue() {
        return insuranceValue;
    }

    public void setInsuranceValue(Double insuranceValue) {
        this.insuranceValue = insuranceValue;
    }

    public Double getTotalServiceFee() {
        return totalServiceFee;
    }

    public void setTotalServiceFee(Double totalServiceFee) {
        this.totalServiceFee = totalServiceFee;
    }

    public Double getCodSplitFee() {
        return codSplitFee;
    }

    public void setCodSplitFee(Double codSplitFee) {
        this.codSplitFee = codSplitFee;
    }
}
