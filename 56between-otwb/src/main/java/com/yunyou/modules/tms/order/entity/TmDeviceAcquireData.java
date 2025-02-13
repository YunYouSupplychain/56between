package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 设备温度数据
 * @author liujianhua
 * @version 2022.8.8
 */
public class TmDeviceAcquireData extends DataEntity<TmDeviceAcquireData> {

    private String vehicleNo;
    private String gpsNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date acquireTime;
    private String coordinate;
    private Double temperature1;
    private Double temperature2;
    private Double temperature3;
    private Double temperature4;
    private Double temperature5;
    private String baseOrgId;
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

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
