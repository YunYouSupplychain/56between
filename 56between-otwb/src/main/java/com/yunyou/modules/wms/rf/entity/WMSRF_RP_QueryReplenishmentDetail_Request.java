package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_RP_QueryReplenishmentDetail_Request implements Serializable {
    // 用户Id
    private String userId;
    // 补货任务ID
    private String rpId;

    public String getRpId() {
        return rpId;
    }

    public void setRpId(String rpId) {
        this.rpId = rpId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
