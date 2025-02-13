package com.yunyou.modules.tms.applet.entity.request;

import java.io.Serializable;

public class TmAppDispatchOrderFinishRequest implements Serializable {

    private String dispatchId;
    private String carNo;
    private Double odometerNumber;
    private String trailerNo;

    private String orgId;
    private String baseOrgId;
    private String operator;
    private String userId;

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public Double getOdometerNumber() {
        return odometerNumber;
    }

    public void setOdometerNumber(Double odometerNumber) {
        this.odometerNumber = odometerNumber;
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

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTrailerNo() {
        return trailerNo;
    }

    public void setTrailerNo(String trailerNo) {
        this.trailerNo = trailerNo;
    }
}