package com.yunyou.modules.interfaces.gps;

import com.yunyou.common.utils.time.DateFormatUtil;

import java.io.Serializable;
import java.text.ParseException;

/**
 * GPS数据 - 运行轨迹信息
 */
public class GpsRunTrackInfo implements Serializable {
    // GPS或车牌号
    private String no;
    // 时间：yyyy-MM-dd HH:mm:ss
    private String time;
    // 经度，GCJ02坐标系
    private Double lon;
    private Double lat;
    // 速度
    private Integer speed;
    // 方向角度
    private Integer course;
    // 位置
    private String address;
    // 里程
    private Double odometer;

    public GpsRunTrackInfo() {
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getOdometer() {
        return odometer;
    }

    public void setOdometer(Double odometer) {
        this.odometer = odometer;
    }

    public Long getLongTime() {
        try {
            return DateFormatUtil.pareDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, this.time).getTime();
        } catch (ParseException e) {
            return null;
        }
    }
}
