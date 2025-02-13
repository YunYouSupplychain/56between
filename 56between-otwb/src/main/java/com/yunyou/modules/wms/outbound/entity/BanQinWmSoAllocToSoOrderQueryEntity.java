package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-03-05
 */
public class BanQinWmSoAllocToSoOrderQueryEntity {
    private String orgId;
    private String[] carrierCodes;
    private String consigneeCode;
    private String consigneeName;
    private String consigneeTel;
    private String consigneeAddr;
    private String contactName;
    private String contactTel;
    private String contactAddr;
    private String line;
    private String logisticNo;
    private String soNo;
    private String[] ownerCodes;
    private String[] soTypes;
    private String[] statuss;
    private Date fmEtd;
    private Date toEtd;
    private Date fmOrderDate;
    private Date toOrderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;

    private String ownerCode;
    private String ownerName;
    private String soType;
    private String status;
    private String carrierCode;
    private String carrierName;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String[] getCarrierCodes() {
        return carrierCodes;
    }

    public void setCarrierCodes(String[] carrierCodes) {
        this.carrierCodes = carrierCodes;
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLogisticNo() {
        return logisticNo;
    }

    public void setLogisticNo(String logisticNo) {
        this.logisticNo = logisticNo;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String[] getOwnerCodes() {
        return ownerCodes;
    }

    public void setOwnerCodes(String[] ownerCodes) {
        this.ownerCodes = ownerCodes;
    }

    public String[] getSoTypes() {
        return soTypes;
    }

    public void setSoTypes(String[] soTypes) {
        this.soTypes = soTypes;
    }

    public String[] getStatuss() {
        return statuss;
    }

    public void setStatuss(String[] statuss) {
        this.statuss = statuss;
    }

    public Date getFmEtd() {
        return fmEtd;
    }

    public void setFmEtd(Date fmEtd) {
        this.fmEtd = fmEtd;
    }

    public Date getToEtd() {
        return toEtd;
    }

    public void setToEtd(Date toEtd) {
        this.toEtd = toEtd;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSoType() {
        return soType;
    }

    public void setSoType(String soType) {
        this.soType = soType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getFmOrderDate() {
        return fmOrderDate;
    }

    public void setFmOrderDate(Date fmOrderDate) {
        this.fmOrderDate = fmOrderDate;
    }

    public Date getToOrderDate() {
        return toOrderDate;
    }

    public void setToOrderDate(Date toOrderDate) {
        this.toOrderDate = toOrderDate;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
