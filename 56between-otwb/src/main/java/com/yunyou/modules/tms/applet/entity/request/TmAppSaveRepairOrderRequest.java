package com.yunyou.modules.tms.applet.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class TmAppSaveRepairOrderRequest implements Serializable {

    private String id;
    // 报修时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;
    // 客户编码
    private String ownerCode;
    // 状态
    private String status;
    // 车牌号
    private String carNo;
    // 司机
    private String driver;
    // 待维修项
    private String needRepairItem;
    // 机构ID
    private String orgId;
    // 基础数据机构ID
    private String baseOrgId;
    private String userId;
    private String operator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNeedRepairItem() {
        return needRepairItem;
    }

    public void setNeedRepairItem(String needRepairItem) {
        this.needRepairItem = needRepairItem;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}