package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_TC_QueryTaskCountDetail_Request implements Serializable {
    // 用户Id
    private String userId;
    // 盘点任务号
    private String countNo;
    // 库区
    private String zoneCode;
    // 库位
    private String locCode;
    // 通道
    private String lane;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountNo() {
        return countNo;
    }

    public void setCountNo(String countNo) {
        this.countNo = countNo;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }
}
