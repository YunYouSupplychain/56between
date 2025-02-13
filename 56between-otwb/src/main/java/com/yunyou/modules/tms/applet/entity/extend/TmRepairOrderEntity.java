package com.yunyou.modules.tms.applet.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TmRepairOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String remarks;
    private String repairNo;        // 维修单号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;         // 报修时间
    private String ownerCode;       // 客户编码
    private String carNo;           // 车牌号
    private String driver;          // 司机
    private String status;          // 状态
    private String needRepairItem;  // 待维修项
    private String repairman;       // 维修员
    private String repairSuggestion;// 维修意见
    private Double amount;          // 合计金额
    private Double workHour;        // 合计工时
    private Double workHourCost;    // 合计工时费
    private Double totalAmount;     // 合计小计
    private Long recVer = 0L;       // 版本号
    private String orgId;           // 机构ID
    private String baseOrgId;       // 基础数据机构ID

    private String ownerName;    // 客户名称
    private String driverName;      // 司机名称

    // 查询条件
    private String isUnConfirm;     // 是否未确认
    private List<String> carNoList = Lists.newArrayList();      // 查询条件-车牌号

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

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNeedRepairItem() {
        return needRepairItem;
    }

    public void setNeedRepairItem(String needRepairItem) {
        this.needRepairItem = needRepairItem;
    }

    public String getRepairman() {
        return repairman;
    }

    public void setRepairman(String repairman) {
        this.repairman = repairman;
    }

    public String getRepairSuggestion() {
        return repairSuggestion;
    }

    public void setRepairSuggestion(String repairSuggestion) {
        this.repairSuggestion = repairSuggestion;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getRecVer() {
        return recVer;
    }

    public void setRecVer(Long recVer) {
        this.recVer = recVer;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getIsUnConfirm() {
        return isUnConfirm;
    }

    public void setIsUnConfirm(String isUnConfirm) {
        this.isUnConfirm = isUnConfirm;
    }

    public List<String> getCarNoList() {
        return carNoList;
    }

    public void setCarNoList(List<String> carNoList) {
        this.carNoList = carNoList;
    }
}
