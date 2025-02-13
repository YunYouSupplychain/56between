package com.yunyou.modules.tms.applet.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class TmAppWaitingTimeApplyConfirmRequest extends TmAppConfirmRequest {

    private String outletCode;          // 网点编码
    private String carNo;               // 车牌号
    private String driver;              // 司机
    private String incident;            // 事由
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date happenDate;            // 发生时间
    private BigDecimal waitingTime;     // 等待时长

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getIncident() {
        return incident;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public Date getHappenDate() {
        return happenDate;
    }

    public void setHappenDate(Date happenDate) {
        this.happenDate = happenDate;
    }

    public BigDecimal getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(BigDecimal waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}