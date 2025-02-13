package com.yunyou.modules.tms.applet.entity.extend;

import java.io.Serializable;

public class TmDispatchTransportOrderDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String transportNo; // 运输订单号
    private String orgId;       // 机构ID
    private String ownerCode;   // 货主编码
    private String skuCode;     // 商品编码
    private String skuName;     // 商品名称
    private Double orderQty;    // 计划数量
    private Double actualQty;   // 实际提货数量
    private Double caskQty;     // 罐桶数

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Double orderQty) {
        this.orderQty = orderQty;
    }

    public Double getActualQty() {
        return actualQty;
    }

    public void setActualQty(Double actualQty) {
        this.actualQty = actualQty;
    }

    public Double getCaskQty() {
        return caskQty;
    }

    public void setCaskQty(Double caskQty) {
        this.caskQty = caskQty;
    }
}
