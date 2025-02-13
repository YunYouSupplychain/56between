package com.yunyou.modules.interfaces.gps;

import java.io.Serializable;

/**
 * GPS数据 - 最新位置信息
 */
public class GpsNewestLocationInfo implements Serializable {
    // GPS或车牌号
    private String no;
    // 时间：yyyy-MM-dd HH:mm:ss
    private String time;
    // 经度
    private Double lon;
    // 纬度
    private Double lat;
    // 速度
    private Integer speed;
    // 方向角度
    private Integer course;
    // 省
    private String province;
    // 市
    private String city;
    // 区
    private String district;
    // 位置
    private String address;
    // 里程
    private Double odometer;
    // 温度
    private GpsTemperatureInfo temperature;
    // 湿度
    private GpsHumidityInfo humidity;

    public GpsNewestLocationInfo() {
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public GpsTemperatureInfo getTemperature() {
        return temperature;
    }

    public void setTemperature(GpsTemperatureInfo temperature) {
        this.temperature = temperature;
    }

    public GpsHumidityInfo getHumidity() {
        return humidity;
    }

    public void setHumidity(GpsHumidityInfo humidity) {
        this.humidity = humidity;
    }

    public static class GpsTemperatureInfo {
        private String time1;
        private String time2;
        private String time3;
        private String time4;
        private Double t1;
        private Double t2;
        private Double t3;
        private Double t4;

        public String getTime1() {
            return time1;
        }

        public void setTime1(String time1) {
            this.time1 = time1;
        }

        public String getTime2() {
            return time2;
        }

        public void setTime2(String time2) {
            this.time2 = time2;
        }

        public String getTime3() {
            return time3;
        }

        public void setTime3(String time3) {
            this.time3 = time3;
        }

        public String getTime4() {
            return time4;
        }

        public void setTime4(String time4) {
            this.time4 = time4;
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
    }

    public static class GpsHumidityInfo {
        private String time1;
        private String time2;
        private String time3;
        private String time4;
        private Double h1;
        private Double h2;
        private Double h3;
        private Double h4;

        public String getTime1() {
            return time1;
        }

        public void setTime1(String time1) {
            this.time1 = time1;
        }

        public String getTime2() {
            return time2;
        }

        public void setTime2(String time2) {
            this.time2 = time2;
        }

        public String getTime3() {
            return time3;
        }

        public void setTime3(String time3) {
            this.time3 = time3;
        }

        public String getTime4() {
            return time4;
        }

        public void setTime4(String time4) {
            this.time4 = time4;
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
    }
}