package com.yunyou.modules.tms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmRepVehicleMonitoring extends DataEntity<TmRepVehicleMonitoring> {

    // 车牌号
    private String vehicleNo;
    // GPS设备号
    private String gpsNo;
    // GPS厂商
    private String gpsManufacturer;
    // 司机
    private String driverName;
    // 承运商
    private String carrierName;

    // GPS状态(0=无法定位；1=信号中断；2=未定位；3=车辆静止；4=车辆运动中;5=未绑定;6=未启用;8=数据上传延迟)
    private Integer gpsStatus;
    // 设备与服务端最新通信时间
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date time;

    // GPS时间
    private String gpsTime;
    // 经度
    private Double lng;
    // 纬度
    private Double lat;
    // 当前坐标对应地址
    private String address;
    // 速度（单位：km/h）
    private Integer speed;
    // 方向角
    private Integer course;
    // 定位类型|0/GPS定位、1/基站定位
    private Integer locationType;

    // 采集时间
    private String acquireTime;
    private Double temperature1;
    private Double temperature2;
    private Double temperature3;
    private Double temperature4;
    private Double envTemp;// 环境温度

    // 机构
    private String baseOrgId;
    private String baseOrgName;

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

    public String getGpsManufacturer() {
        return gpsManufacturer;
    }

    public void setGpsManufacturer(String gpsManufacturer) {
        this.gpsManufacturer = gpsManufacturer;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public Integer getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(Integer gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    public String getAcquireTime() {
        return acquireTime;
    }

    public void setAcquireTime(String acquireTime) {
        this.acquireTime = acquireTime;
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

    public Double getEnvTemp() {
        return envTemp;
    }

    public void setEnvTemp(Double envTemp) {
        this.envTemp = envTemp;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getBaseOrgName() {
        return baseOrgName;
    }

    public void setBaseOrgName(String baseOrgName) {
        this.baseOrgName = baseOrgName;
    }

    public boolean hasGps() {
        return getLng() != null && getLat() != null;
    }
}
