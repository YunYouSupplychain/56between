package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_MV_QueryMovement_Request implements Serializable {
    // 用户Id
    private String userId;
    // 商品编码
    private String skuCode;
    // 托盘编码
    private String traceId;
    // 库位编码
    private String locationCode;
    // 批次号
    private String lotNum;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }
}
