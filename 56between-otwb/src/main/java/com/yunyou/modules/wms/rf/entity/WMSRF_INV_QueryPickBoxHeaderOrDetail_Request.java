package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_INV_QueryPickBoxHeaderOrDetail_Request implements Serializable {
    // 用户Id
    private String userId;
    //
    private String soNo;
    // 托盘编码
    private String toId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}
