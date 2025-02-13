package com.yunyou.modules.tms.basic.entity.extend;

/**
 * 地图坐标点
 */
public class MapPoint {

    private String lng;// 经度
    private String lat;// 纬度

    public MapPoint(String lng, String lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
