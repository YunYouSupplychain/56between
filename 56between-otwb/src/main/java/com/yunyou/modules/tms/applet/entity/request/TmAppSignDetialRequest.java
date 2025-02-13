package com.yunyou.modules.tms.applet.entity.request;

import java.io.Serializable;

public class TmAppSignDetialRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skuCode;
    private Double actualQty;
    private Double caskQty;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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
