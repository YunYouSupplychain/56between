package com.yunyou.modules.tms.order.entity.extend;

import java.util.Date;

/**
 * 派车单车辆行驶信息
 */
public class TmDispatchVehicleEntity {
    // 派车单号
    private String dispatchNo;
    // 派车单状态
    private String dispatchStatus;
    // 车牌号
    private String vehicleNo;
    // 设备号
    private String equipmentNo;
    // 设备厂商
    private String gpsManufacturer;
    // 派车时间
    private Date dispatchTime;
    // 最后行驶时间
    private Date lastTravelTime;

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public String getGpsManufacturer() {
        return gpsManufacturer;
    }

    public void setGpsManufacturer(String gpsManufacturer) {
        this.gpsManufacturer = gpsManufacturer;
    }

    public String getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public Date getLastTravelTime() {
        return lastTravelTime;
    }

    public void setLastTravelTime(Date lastTravelTime) {
        this.lastTravelTime = lastTravelTime;
    }
}
