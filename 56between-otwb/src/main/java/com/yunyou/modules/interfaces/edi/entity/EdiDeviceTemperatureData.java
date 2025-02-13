package com.yunyou.modules.interfaces.edi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;

import java.util.Date;

public class EdiDeviceTemperatureData {

    private String vehicleNo;// 车牌号
    private String gpsNo;// GPS设备号
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date acquireTime;// 采集时间
    private String coordinate;// 坐标位置
    private Double temperature1;// 温度1
    private Double temperature2;// 温度2
    private Double temperature3;// 温度3
    private Double temperature4;// 温度4
    private Double temperature5;// 温度5
    private String orgId;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getGpsNo() {
        return gpsNo;
    }

    public void setGpsNo(String gpsNo) {
        this.gpsNo = gpsNo;
    }

    public Date getAcquireTime() {
        return acquireTime;
    }

    public void setAcquireTime(Date acquireTime) {
        this.acquireTime = acquireTime;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public Double getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(Double temperature1) {
        this.temperature1 = temperature1;
    }

    public Double getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(Double temperature2) {
        this.temperature2 = temperature2;
    }

    public Double getTemperature3() {
        return temperature3;
    }

    public void setTemperature3(Double temperature3) {
        this.temperature3 = temperature3;
    }

    public Double getTemperature4() {
        return temperature4;
    }

    public void setTemperature4(Double temperature4) {
        this.temperature4 = temperature4;
    }

    public Double getTemperature5() {
        return temperature5;
    }

    public void setTemperature5(Double temperature5) {
        this.temperature5 = temperature5;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
