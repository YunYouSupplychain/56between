package com.yunyou.modules.tms.print.entity;

import java.util.Date;

public class RepairOrder {
    private String repairNo;
    private Date orderTime;
    private String carNo;
    private String driver;
    private String driverName;
    private String needRepairItem;
    private String repairSuggestion;
    private Double totalAmount;
    private Double totalWorkHour;
    private Double totalWorkHourCost;
    private Double totalCost;

    private String repairCode;
    private String repairName;
    private String skuCode;
    private String skuName;
    private String skuModel;
    private Double qty;
    private Double price;
    private Double amount;
    private Double workHour;
    private Double workHourCost;
    private Double cost;
    private String orgId;

    public String getRepairNo() {
        return repairNo;
    }

    public void setRepairNo(String repairNo) {
        this.repairNo = repairNo;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getNeedRepairItem() {
        return needRepairItem;
    }

    public void setNeedRepairItem(String needRepairItem) {
        this.needRepairItem = needRepairItem;
    }

    public String getRepairSuggestion() {
        return repairSuggestion;
    }

    public void setRepairSuggestion(String repairSuggestion) {
        this.repairSuggestion = repairSuggestion;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalWorkHour() {
        return totalWorkHour;
    }

    public void setTotalWorkHour(Double totalWorkHour) {
        this.totalWorkHour = totalWorkHour;
    }

    public Double getTotalWorkHourCost() {
        return totalWorkHourCost;
    }

    public void setTotalWorkHourCost(Double totalWorkHourCost) {
        this.totalWorkHourCost = totalWorkHourCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getRepairCode() {
        return repairCode;
    }

    public void setRepairCode(String repairCode) {
        this.repairCode = repairCode;
    }

    public String getRepairName() {
        return repairName;
    }

    public void setRepairName(String repairName) {
        this.repairName = repairName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuModel() {
        return skuModel;
    }

    public void setSkuModel(String skuModel) {
        this.skuModel = skuModel;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
    }

    public Double getWorkHourCost() {
        return workHourCost;
    }

    public void setWorkHourCost(Double workHourCost) {
        this.workHourCost = workHourCost;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
