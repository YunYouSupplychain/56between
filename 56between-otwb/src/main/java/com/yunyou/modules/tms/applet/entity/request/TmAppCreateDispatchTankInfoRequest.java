package com.yunyou.modules.tms.applet.entity.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class TmAppCreateDispatchTankInfoRequest implements Serializable {

    private String id;
    private String remarks;
    // 派车单号
    private String dispatchNo;
    // 派车网点
    private String outletCode;
    // 运输订单号
    private String transportNo;
    // 卸油数
    private BigDecimal offloadingQty;
    // 装罐数
    private BigDecimal tankQty;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public BigDecimal getOffloadingQty() {
        return offloadingQty;
    }

    public void setOffloadingQty(BigDecimal offloadingQty) {
        this.offloadingQty = offloadingQty;
    }

    public BigDecimal getTankQty() {
        return tankQty;
    }

    public void setTankQty(BigDecimal tankQty) {
        this.tankQty = tankQty;
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
}