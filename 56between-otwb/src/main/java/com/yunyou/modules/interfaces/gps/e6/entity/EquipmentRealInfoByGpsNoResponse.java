package com.yunyou.modules.interfaces.gps.e6.entity;

public class EquipmentRealInfoByGpsNoResponse {

    private String equipCode;// 设备编号
    private String gpsTime;// 定位时间
    private Integer speed;// 速度
    private Double odometer;// 里程
    private Double lon;// 经度
    private Double lat;// 纬度
    private Integer direction;// 设备方向0-360(0为正北 顺时针递加)
    private String status;// 状态
    private String provice;// 省
    private String city;// 市
    private String district;// 区
    private String roadName;// 路名信息
    private Double t1;// 温度1(℃)（没有数据时返回-999）
    private Double t2;// 温度2(℃)（没有数据时返回-999）
    private Double t3;// 温度3(℃)（没有数据时返回-999）
    private Double t4;// 温度4(℃)（没有数据时返回-999）
    private Double h1;// 湿度1(%RH)（没有数据时返回-1）
    private Double h2;// 湿度2(%RH)（没有数据时返回-1）
    private Double h3;// 湿度3(%RH)（没有数据时返回-1）
    private Double h4;// 湿度4(%RH)（没有数据时返回-1）
    private Integer voltage;// 电量百分比数值
    private String powerStatus;// 电源状态

    public String getEquipCode() {
        return equipCode;
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Double getOdometer() {
        return odometer;
    }

    public void setOdometer(Double odometer) {
        this.odometer = odometer;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public Double getT1() {
        return t1;
    }

    public void setT1(Double t1) {
        this.t1 = t1;
    }

    public Double getT2() {
        return t2;
    }

    public void setT2(Double t2) {
        this.t2 = t2;
    }

    public Double getT3() {
        return t3;
    }

    public void setT3(Double t3) {
        this.t3 = t3;
    }

    public Double getT4() {
        return t4;
    }

    public void setT4(Double t4) {
        this.t4 = t4;
    }

    public Double getH1() {
        return h1;
    }

    public void setH1(Double h1) {
        this.h1 = h1;
    }

    public Double getH2() {
        return h2;
    }

    public void setH2(Double h2) {
        this.h2 = h2;
    }

    public Double getH3() {
        return h3;
    }

    public void setH3(Double h3) {
        this.h3 = h3;
    }

    public Double getH4() {
        return h4;
    }

    public void setH4(Double h4) {
        this.h4 = h4;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    public String getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(String powerStatus) {
        this.powerStatus = powerStatus;
    }
}
