package com.yunyou.modules.tms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;

import java.util.Date;

public class TmRepDispatchOrderDrivingSpeed {

    // 派车单号
    private String dispatchNo;
    // 车牌号
    private String vehicleNo;
    // GPS时间
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date gpsTime;
    // 速度(km/h)
    private Integer speed;
    // 地理位置
    private String address;
    // 地标
    private String landmark;

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Date getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(Date gpsTime) {
        this.gpsTime = gpsTime;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
