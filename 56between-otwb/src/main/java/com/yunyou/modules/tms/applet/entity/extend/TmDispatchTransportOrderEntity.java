package com.yunyou.modules.tms.applet.entity.extend;

import java.io.Serializable;

public class TmDispatchTransportOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dispatchNo;  // 派车单号
    private String transportNo; // 运输订单号
    private String customerNo;  // 客户订单号
    private String shipper;              // 发货人
    private String shipperTel;           // 发货人电话
    private String shipAddress;          // 发货地址
    private String consignee;            // 收货人
    private String consigneeTel;         // 收货人电话
    private String consigneeAddress;      // 收货地址
    private String cabin;   // 舱室
    private String dispatchSiteOutletCode; // 配送网点编码
    private String dispatchSiteOutletName; // 配送网点名称

    private String consigneeCode;          // 收货方编码
    private String consigneeName;          // 收货方名称
    private String consigneeCityId;        // 目的地城市id
    private String shipCode;               // 发货方编码
    private String shipName;               // 发货方名称
    private String shipCityId;             // 发货城市id
    private String customerCode;           // 客户编码
    private String customerName;           // 客户名称

    // 查询
    private String status;      // 状态
    private String receiveShip;
    private String orgId;       // 机构ID

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


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


    public String getDispatchSiteOutletCode() {
        return dispatchSiteOutletCode;
    }

    public void setDispatchSiteOutletCode(String dispatchSiteOutletCode) {
        this.dispatchSiteOutletCode = dispatchSiteOutletCode;
    }

    public String getDispatchSiteOutletName() {
        return dispatchSiteOutletName;
    }

    public void setDispatchSiteOutletName(String dispatchSiteOutletName) {
        this.dispatchSiteOutletName = dispatchSiteOutletName;
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

    public String getConsigneeCityId() {
        return consigneeCityId;
    }

    public void setConsigneeCityId(String consigneeCityId) {
        this.consigneeCityId = consigneeCityId;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipCityId() {
        return shipCityId;
    }

    public void setShipCityId(String shipCityId) {
        this.shipCityId = shipCityId;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getReceiveShip() {
        return receiveShip;
    }

    public void setReceiveShip(String receiveShip) {
        this.receiveShip = receiveShip;
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
}
