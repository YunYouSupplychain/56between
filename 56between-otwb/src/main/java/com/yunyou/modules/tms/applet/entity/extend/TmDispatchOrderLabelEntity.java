package com.yunyou.modules.tms.applet.entity.extend;

import java.io.Serializable;

public class TmDispatchOrderLabelEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String remarks;
    private String labelNo;     // 标签号
    private String dispatchNo;  // 派车单号
    private String transportNo; // 运输订单号
    private String customerNo;  // 客户订单号
    private String receiveShip; // 提货(R) or 送货(S)
    private String orgId;       // 机构ID
    private String baseOrgId;   // 基础数据机构ID
    private String dispatchSiteOutletCode; // 配送网点编码
    private String dispatchSiteOutletName; // 配送网点名称
    private String status;  // 状态
    private String cabin;   // 舱室
    private String ownerCode;   // 货主编码
    private String skuCode;     // 商品编码
    private Double qty;
    private Double weight;
    private Double cubic;

    private String principalCode;        // 委托方编码
    private String principalName;        // 委托方名称
    private String customerCode;        // 客户编码
    private String customerName;        // 客户名称
    private String shipper;              // 发货人
    private String shipperTel;           // 发货人电话
    private String shipAddress;          // 发货地址
    private String consignee;            // 收货人
    private String consigneeTel;         // 收货人电话
    private String consigneeAddress;      // 收货地址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getReceiveShip() {
        return receiveShip;
    }

    public void setReceiveShip(String receiveShip) {
        this.receiveShip = receiveShip;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getDispatchSiteOutletCode() {
        return dispatchSiteOutletCode;
    }

    public void setDispatchSiteOutletCode(String dispatchSiteOutletCode) {
        this.dispatchSiteOutletCode = dispatchSiteOutletCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getPrincipalCode() {
        return principalCode;
    }

    public void setPrincipalCode(String principalCode) {
        this.principalCode = principalCode;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperTel() {
        return shipperTel;
    }

    public void setShipperTel(String shipperTel) {
        this.shipperTel = shipperTel;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
    }

    public String getDispatchSiteOutletName() {
        return dispatchSiteOutletName;
    }

    public void setDispatchSiteOutletName(String dispatchSiteOutletName) {
        this.dispatchSiteOutletName = dispatchSiteOutletName;
    }
}
