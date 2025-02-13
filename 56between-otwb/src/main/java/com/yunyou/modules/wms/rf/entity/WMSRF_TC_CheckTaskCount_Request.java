package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_TC_CheckTaskCount_Request implements Serializable {
    // 用户Id
    private String userId;
    // 盘点任务号
    private String countNo;

    public String getCountNo() {
        return countNo;
    }

    public void setCountNo(String countNo) {
        this.countNo = countNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
