package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_SC_QueryLot_Request implements Serializable {
    private String userId;
    private String lotNum;
    private String skuCode;
    private String ownerCode;
    private String allocI;
    private String serialNo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getAllocI() {
        return allocI;
    }

    public void setAllocI(String allocI) {
        this.allocI = allocI;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
