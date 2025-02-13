package com.yunyou.modules.tms.order.entity.extend;

import java.util.Date;

public class TmNoReturnVehicleInfo {

    private String vehicleNo;// 车牌号
    private String driverName;// 司机名称
    private String driverTel;// 司机电话
    private Date startTime;// 起始时间
    private Date endTime;// 结束时间
    private String gpsNo;// GPS设备号
    private String gpsManufacturer;// GPS厂商
    private Double warningTempFm;// 预警温度
    private Double warningTempTo;// 预警温度
    private String baseOrgId;
    private String orgId;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverTel() {
        return driverTel;
    }

    public void setDriverTel(String driverTel) {
        this.driverTel = driverTel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getGpsNo() {
        return gpsNo;
    }

    public void setGpsNo(String gpsNo) {
        this.gpsNo = gpsNo;
    }

    public String getGpsManufacturer() {
        return gpsManufacturer;
    }

    public void setGpsManufacturer(String gpsManufacturer) {
        this.gpsManufacturer = gpsManufacturer;
    }

    public Double getWarningTempFm() {
        return warningTempFm;
    }

    public void setWarningTempFm(Double warningTempFm) {
        this.warningTempFm = warningTempFm;
    }

    public Double getWarningTempTo() {
        return warningTempTo;
    }

    public void setWarningTempTo(Double warningTempTo) {
        this.warningTempTo = warningTempTo;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
