package com.yunyou.modules.tms.common.map.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 坐标点
 */
public class Point {
    // 经度
    public Double lng;
    // 维度
    public Double lat;

    public Point() {
    }

    public Point(Double lng, Double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    @JsonIgnore
    public String getLngLat() {
        return lng + "," + lat;
    }

    @JsonIgnore
    public String getLatLng() {
        return lat + "," + lng;
    }
}
