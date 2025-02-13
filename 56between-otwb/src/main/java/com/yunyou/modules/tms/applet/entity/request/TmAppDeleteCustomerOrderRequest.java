package com.yunyou.modules.tms.applet.entity.request;

import java.io.Serializable;

public class TmAppDeleteCustomerOrderRequest implements Serializable {

    private String planOrderNo;
    private String userId;

    private String orgId;

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


}