package com.yunyou.modules.interfaces.edi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;

import java.util.Date;

public class EdiDispatchOrderInfo {

    private String dispatchNo;// 派车单号
    private String consigneeCode;// 收货方编码
    private String consigneeName;// 收货方名称
    private String startAddress;// 起始地
    private String endAddress;// 目的地
    private String carrierCode;// 承运商编码
    private String carrierName;// 承运商名称
    private String vehicleNo;// 车牌号
    private String gpsNo;// GPS设备号
    private String gpsManufacturer;// GPS厂商（G7、易流、捷依）
    private String driver;// 司机
    private String driverTel;// 司机电话
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date departureTime;// 发车时间
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date arrivalTime;// 抵达时间
    private Double mileage;// 里程
    private Double avgTemp;// 平均温度
    private Double alarmTemp;// 报警温度
    private Double exceptionTemp;// 异常温度
    private String orgId;

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
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

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverTel() {
        return driverTel;
    }

    public void setDriverTel(String driverTel) {
        this.driverTel = driverTel;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(Double avgTemp) {
        this.avgTemp = avgTemp;
    }

    public Double getAlarmTemp() {
        return alarmTemp;
    }

    public void setAlarmTemp(Double alarmTemp) {
        this.alarmTemp = alarmTemp;
    }

    public Double getExceptionTemp() {
        return exceptionTemp;
    }

    public void setExceptionTemp(Double exceptionTemp) {
        this.exceptionTemp = exceptionTemp;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}
