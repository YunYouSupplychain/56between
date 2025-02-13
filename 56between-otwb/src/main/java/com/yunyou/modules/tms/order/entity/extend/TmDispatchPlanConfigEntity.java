package com.yunyou.modules.tms.order.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.modules.tms.order.entity.TmDispatchPlanConfig;

import java.math.BigDecimal;
import java.util.Date;

public class TmDispatchPlanConfigEntity extends TmDispatchPlanConfig {
    private static final long serialVersionUID = 1L;
    // 提货点名称
    private String pickUpPointName;
    // 送货点名称
    private String deliveryPointName;
    // 商品名称
    private String skuName;
    // 商品类型
    private String skuType;
    private String skuTypeDesc;
    // 路线
    private String lineCode;
    private String lineName;
    // 驾驶员名称
    private String driverName;
    // 押送员名称
    private String escortName;
    // 计划数量
    private BigDecimal planQty;
    // 送达时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivalTime;
    // 车辆类型
    private String vehicleType;
    private String vehicleTypeDesc;
    // 设备类型
    private String equipmentTypeCode;
    private String equipmentTypeName;

    public String getPickUpPointName() {
        return pickUpPointName;
    }

    public void setPickUpPointName(String pickUpPointName) {
        this.pickUpPointName = pickUpPointName;
    }

    public String getDeliveryPointName() {
        return deliveryPointName;
    }

    public void setDeliveryPointName(String deliveryPointName) {
        this.deliveryPointName = deliveryPointName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public String getSkuTypeDesc() {
        return skuTypeDesc;
    }

    public void setSkuTypeDesc(String skuTypeDesc) {
        this.skuTypeDesc = skuTypeDesc;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getEscortName() {
        return escortName;
    }

    public void setEscortName(String escortName) {
        this.escortName = escortName;
    }

    public BigDecimal getPlanQty() {
        return planQty;
    }

    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleTypeDesc() {
        return vehicleTypeDesc;
    }

    public void setVehicleTypeDesc(String vehicleTypeDesc) {
        this.vehicleTypeDesc = vehicleTypeDesc;
    }

    public String getEquipmentTypeCode() {
        return equipmentTypeCode;
    }

    public void setEquipmentTypeCode(String equipmentTypeCode) {
        this.equipmentTypeCode = equipmentTypeCode;
    }

    public String getEquipmentTypeName() {
        return equipmentTypeName;
    }

    public void setEquipmentTypeName(String equipmentTypeName) {
        this.equipmentTypeName = equipmentTypeName;
    }
}
