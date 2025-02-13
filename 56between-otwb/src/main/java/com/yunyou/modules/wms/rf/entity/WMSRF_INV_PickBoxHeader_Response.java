package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_INV_PickBoxHeader_Response implements Serializable {
    // 出库单号
    private String soNo;
    // 状态
    private String status;
    // 复核状态(00未复核、99已复核、90不复核)
    private String checkStatus;
    // 收货人编码
    private String consigneeCode;
    // 收货人名称
    private String consigneeName;
    // 收货人电话
    private String consigneeTel;
    // 收货人地址
    private String consigneeAddr;
    // 联系人编码
    private String contactCode;
    // 联系人名称
    private String contactName;
    // 联系人电话
    private String contactTel;
    // 联系人地址
    private String contactAddr;
    // 商品数
    private Double qtySku;
    // 单品数
    private Double qtyTotal;
    // 目标跟踪号
    private String toId;

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getConsigneeAddr() {
        return consigneeAddr;
    }

    public void setConsigneeAddr(String consigneeAddr) {
        this.consigneeAddr = consigneeAddr;
    }

    public String getContactCode() {
        return contactCode;
    }

    public void setContactCode(String contactCode) {
        this.contactCode = contactCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }

    public Double getQtySku() {
        return qtySku;
    }

    public void setQtySku(Double qtySku) {
        this.qtySku = qtySku;
    }

    public Double getQtyTotal() {
        return qtyTotal;
    }

    public void setQtyTotal(Double qtyTotal) {
        this.qtyTotal = qtyTotal;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}
