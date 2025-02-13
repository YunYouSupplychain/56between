package com.yunyou.modules.tms.applet.entity.request;

import java.io.Serializable;

public class TmAppDepartRequest implements Serializable {

    private String dispatchId;
    private String vehicleNo;
    private String userId;

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}