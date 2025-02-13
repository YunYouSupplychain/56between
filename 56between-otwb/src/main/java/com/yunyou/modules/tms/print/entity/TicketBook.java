package com.yunyou.modules.tms.print.entity;

import java.util.Date;

public class TicketBook {
    private String dispatchNo;       //
    private String driverName;
    private String carNo;
    private String shift;
    private String platform;
    private Date dispatchTime;
    private String consigneeCode;
    private String consigneeName;
    private String supplierCode;
    private String supplierName;
    private String carrierCode;
    private String carrierName;
    private String customerNo;
    private String storeDispatchNo;
    private Double shipBoxQty;
    private Integer ticketQty;

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getStoreDispatchNo() {
        return storeDispatchNo;
    }

    public void setStoreDispatchNo(String storeDispatchNo) {
        this.storeDispatchNo = storeDispatchNo;
    }

    public Double getShipBoxQty() {
        return shipBoxQty;
    }

    public void setShipBoxQty(Double shipBoxQty) {
        this.shipBoxQty = shipBoxQty;
    }

    public Integer getTicketQty() {
        return ticketQty;
    }

    public void setTicketQty(Integer ticketQty) {
        this.ticketQty = ticketQty;
    }
}
