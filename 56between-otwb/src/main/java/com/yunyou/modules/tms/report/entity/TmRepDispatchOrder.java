package com.yunyou.modules.tms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.number.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.Date;

public class TmRepDispatchOrder extends DataEntity<TmRepDispatchOrder> {

    private String dispatchNo;
    private String carrierName;
    private String startAddress;
    private String endAddress;
    private String consigneeName;
    private String vehicleNo;
    private String driverName;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date departureTime;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date arrivalTime;
    private Double mileage;
    private Double avgTemp;
    private Double alarmTemp;
    private String orgId;
    private String gpsNo;// GPS设备号
    private String gpsManufacturer;// GPS厂商

    @ExcelField(title = "派车单号", sort = 1)
    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    @ExcelField(title = "承运商", sort = 2)
    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    @ExcelField(title = "起始地", sort = 3)
    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    @ExcelField(title = "目的地", sort = 4)
    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    @ExcelField(title = "门店", sort = 5)
    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    @ExcelField(title = "车牌号", sort = 6)
    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    @ExcelField(title = "司机", sort = 7)
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @ExcelField(title = "发车时间", sort = 8)
    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    @ExcelField(title = "抵达时间", sort = 9)
    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @ExcelField(title = "公里数", sort = 10)
    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    @ExcelField(title = "时长", sort = 11)
    public Double getDuration() {
        if (getDepartureTime() == null || getArrivalTime() == null)
            return null;
        double sub = BigDecimalUtil.sub(getArrivalTime().getTime(), getDepartureTime().getTime());
        return BigDecimalUtil.div(sub, 3600000, 2, BigDecimal.ROUND_HALF_UP);
    }

    @ExcelField(title = "时速", sort = 12)
    public Double getSpeed() {
        if (getMileage() == null || getDuration() == null || getDuration() == 0D)
            return null;
        return BigDecimalUtil.div(getMileage(), getDuration(), 2, BigDecimal.ROUND_HALF_UP);
    }

    @ExcelField(title = "平均温度(℃)", sort = 13)
    public Double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(Double avgTemp) {
        this.avgTemp = avgTemp;
    }

    @ExcelField(title = "报警温度(℃)", sort = 14)
    public Double getAlarmTemp() {
        return alarmTemp;
    }

    public void setAlarmTemp(Double alarmTemp) {
        this.alarmTemp = alarmTemp;
    }

    @ExcelField(title = "异常温度(℃)", sort = 15)
    public Double getExceptionTemp() {
        if (getAlarmTemp() == null || getAvgTemp() == null)
            return null;
        return BigDecimalUtil.sub(getAlarmTemp(), getAvgTemp());
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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
}
