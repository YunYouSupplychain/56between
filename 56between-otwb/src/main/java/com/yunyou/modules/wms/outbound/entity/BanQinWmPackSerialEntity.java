package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 打包序列号Entity
 * @author WMJ
 * @version 2020-05-13
 */
public class BanQinWmPackSerialEntity extends BanQinWmPackSerial {
    private String trackingNo;
    private String customerNo;
    private String caseNo;
    private String packOp;
    private Date packTime;
    private String ownerName;
    private String skuName;
    private Date packTimeFm;
    private Date packTimeTo;

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public Date getPackTimeFm() {
        return packTimeFm;
    }

    public void setPackTimeFm(Date packTimeFm) {
        this.packTimeFm = packTimeFm;
    }

    public Date getPackTimeTo() {
        return packTimeTo;
    }

    public void setPackTimeTo(Date packTimeTo) {
        this.packTimeTo = packTimeTo;
    }

    public String getPackOp() {
        return packOp;
    }

    public void setPackOp(String packOp) {
        this.packOp = packOp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getPackTime() {
        return packTime;
    }

    public void setPackTime(Date packTime) {
        this.packTime = packTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}