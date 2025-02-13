package com.yunyou.modules.tms.applet.entity.request;

import java.io.Serializable;

public class TmAppHandoverConfirmSkuListRequest implements Serializable {

    private String ownerCode;
    private String skuCode;
    private Double orderQty;
    private Double actualQty;
    private Double unloadingTime;

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

    public Double getUnloadingTime() {
        return unloadingTime;
    }

    public void setUnloadingTime(Double unloadingTime) {
        this.unloadingTime = unloadingTime;
    }
}