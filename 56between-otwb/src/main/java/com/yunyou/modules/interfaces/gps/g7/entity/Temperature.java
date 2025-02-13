package com.yunyou.modules.interfaces.gps.g7.entity;

public class Temperature {
    private Double t1Temperature;
    private Double t2Temperature;
    private Double t1Humidity;
    private Double t2Humidity;

    public Double getT1Temperature() {
        return t1Temperature;
    }

    public void setT1Temperature(Double t1Temperature) {
        this.t1Temperature = t1Temperature;
    }

    public Double getT2Temperature() {
        return t2Temperature;
    }

    public void setT2Temperature(Double t2Temperature) {
        this.t2Temperature = t2Temperature;
    }

    public Double getT1Humidity() {
        return t1Humidity;
    }

    public void setT1Humidity(Double t1Humidity) {
        this.t1Humidity = t1Humidity;
    }

    public Double getT2Humidity() {
        return t2Humidity;
    }

    public void setT2Humidity(Double t2Humidity) {
        this.t2Humidity = t2Humidity;
    }
}
