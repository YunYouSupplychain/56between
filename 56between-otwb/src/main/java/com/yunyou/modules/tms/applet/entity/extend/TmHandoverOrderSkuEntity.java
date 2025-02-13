package com.yunyou.modules.tms.applet.entity.extend;

import java.io.Serializable;
import java.math.BigDecimal;

public class TmHandoverOrderSkuEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String remarks;
    private String handoverNo;		// 交接单号
    private String transportNo;		// 运输订单号
    private String customerNo;		// 客户订单号
    private String ownerCode;		// 货主编码
    private String skuCode;			// 商品编码
    private BigDecimal orderQty;	// 计划数量
    private BigDecimal actualQty;	// 实际数量
    private BigDecimal unloadingTime;	// 卸货时长
    private String receiveShip;		// 收/发
    private String orgId;			// 机构ID
    private String baseOrgId;   	// 基础数据机构ID

    private String ownerName;
    private String skuName;

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

    public String getHandoverNo() {
        return handoverNo;
    }

    public void setHandoverNo(String handoverNo) {
        this.handoverNo = handoverNo;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getActualQty() {
        return actualQty;
    }

    public void setActualQty(BigDecimal actualQty) {
        this.actualQty = actualQty;
    }

    public String getReceiveShip() {
        return receiveShip;
    }

    public void setReceiveShip(String receiveShip) {
        this.receiveShip = receiveShip;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public BigDecimal getUnloadingTime() {
        return unloadingTime;
    }

    public void setUnloadingTime(BigDecimal unloadingTime) {
        this.unloadingTime = unloadingTime;
    }
}
