package com.yunyou.modules.print.entity;

public class StoPrintEntity {

    private String mailno;

    private String bigWord;

    private String packagePlace;

    private String receiverName;

    private String receiverMobile;

    private String receiverAddress;

    private String senderName;

    private String senderMobile;

    private String senderAddress;

    private String cusArea1;

    private String cusArea2;

    private String label;               // 订单/波次

    public String getMailno() {
        return mailno;
    }

    public void setMailno(String mailno) {
        this.mailno = mailno;
    }

    public String getBigWord() {
        return bigWord;
    }

    public void setBigWord(String bigWord) {
        this.bigWord = bigWord;
    }

    public String getPackagePlace() {
        return packagePlace;
    }

    public void setPackagePlace(String packagePlace) {
        this.packagePlace = packagePlace;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getCusArea1() {
        return cusArea1;
    }

    public void setCusArea1(String cusArea1) {
        this.cusArea1 = cusArea1;
    }

    public String getCusArea2() {
        return cusArea2;
    }

    public void setCusArea2(String cusArea2) {
        this.cusArea2 = cusArea2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
