package com.yunyou.modules.interfaces.gps.g7.entity;

public class VehicleCurrentTempInfo {
    private Integer num;
    private String alarmType;
    private Double temperature;
    private Double lowTemp;
    private Double highTemp;
    private Double humidity;
    private Double lowHum;
    private Double highHum;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(Double lowTemp) {
        this.lowTemp = lowTemp;
    }

    public Double getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(Double highTemp) {
        this.highTemp = highTemp;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getLowHum() {
        return lowHum;
    }

    public void setLowHum(Double lowHum) {
        this.lowHum = lowHum;
    }

    public Double getHighHum() {
        return highHum;
    }

    public void setHighHum(Double highHum) {
        this.highHum = highHum;
    }
}
