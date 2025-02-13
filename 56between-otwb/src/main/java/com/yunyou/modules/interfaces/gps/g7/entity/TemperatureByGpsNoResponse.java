package com.yunyou.modules.interfaces.gps.g7.entity;

import java.util.Date;

public class TemperatureByGpsNoResponse {

    // 坐标系类型
    private String coordinate;
    // 创建时间
    private Date createtime;
    // GPS设备号
    private String gpsno;
    // 纬度
    private Double lat;
    // 经度
    private Double lng;
    // 温度数据
    private Temperature probeInfo;
    // 温度上报时间
    private Date time;
    // 设备类型 2固定 3便携
    private String type;

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getGpsno() {
        return gpsno;
    }

    public void setGpsno(String gpsno) {
        this.gpsno = gpsno;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Temperature getProbeInfo() {
        return probeInfo;
    }

    public void setProbeInfo(Temperature probeInfo) {
        this.probeInfo = probeInfo;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}