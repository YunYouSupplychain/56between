package com.yunyou.modules.tms.applet.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TmWaitingTimeApplyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String remarks;

    private String outletCode;          // 网点编码
    private String carNo;               // 车牌号
    private String driver;              // 司机
    private String incident;            // 事由
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date happenTime;            // 发生时间
    private BigDecimal waitingTime;     // 等待时长
    private String status;              // 状态
    private String result;              // 处理结果
    private String orgId;               // 机构ID

    private String outletName;
    private String driverName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

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

    public Date getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(Date happenTime) {
        this.happenTime = happenTime;
    }

    public BigDecimal getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(BigDecimal waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
