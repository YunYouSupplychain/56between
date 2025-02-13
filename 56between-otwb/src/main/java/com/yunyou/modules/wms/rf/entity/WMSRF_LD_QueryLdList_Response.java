package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_LD_QueryLdList_Response implements Serializable {
    // 装车单号
    private String ldNo;
    // 车牌号
    private String vehicleNo;
    // 车型
    private String vehicleType;

    public String getLdNo() {
        return ldNo;
    }

    public void setLdNo(String ldNo) {
        this.ldNo = ldNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
