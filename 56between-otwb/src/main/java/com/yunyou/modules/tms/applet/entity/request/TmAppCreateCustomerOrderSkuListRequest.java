package com.yunyou.modules.tms.applet.entity.request;

import java.io.Serializable;

public class TmAppCreateCustomerOrderSkuListRequest implements Serializable {

    private String skuCode;
    private Double qty;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}