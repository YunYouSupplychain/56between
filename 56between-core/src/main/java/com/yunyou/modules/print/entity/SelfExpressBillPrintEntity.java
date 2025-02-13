package com.yunyou.modules.print.entity;

public class SelfExpressBillPrintEntity {

    private String mailNo;              // 面单号
    private String receiveMan;          // 收货人
    private String receiveManPhone;     // 收货人电话
    private String receiveManAddress;   // 收货人地址
    private String sendMan;             // 发货人
    private String sendManPhone;        // 发货人电话
    private String sendManAddress;      // 发货人地址
    private Double totalQty;            // 总数量
    private String orderNo1;            // 订单号1
    private String orderNo2;            // 订单号2
    private String customerOrderNo;     // 客户单号

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getReceiveMan() {
        return receiveMan;
    }

    public void setReceiveMan(String receiveMan) {
        this.receiveMan = receiveMan;
    }

    public String getReceiveManPhone() {
        return receiveManPhone;
    }

    public void setReceiveManPhone(String receiveManPhone) {
        this.receiveManPhone = receiveManPhone;
    }

    public String getReceiveManAddress() {
        return receiveManAddress;
    }

    public void setReceiveManAddress(String receiveManAddress) {
        this.receiveManAddress = receiveManAddress;
    }

    public String getSendMan() {
        return sendMan;
    }

    public void setSendMan(String sendMan) {
        this.sendMan = sendMan;
    }

    public String getSendManPhone() {
        return sendManPhone;
    }

    public void setSendManPhone(String sendManPhone) {
        this.sendManPhone = sendManPhone;
    }

    public String getSendManAddress() {
        return sendManAddress;
    }

    public void setSendManAddress(String sendManAddress) {
        this.sendManAddress = sendManAddress;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public String getOrderNo1() {
        return orderNo1;
    }

    public void setOrderNo1(String orderNo1) {
        this.orderNo1 = orderNo1;
    }

    public String getOrderNo2() {
        return orderNo2;
    }

    public void setOrderNo2(String orderNo2) {
        this.orderNo2 = orderNo2;
    }

    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }
}
