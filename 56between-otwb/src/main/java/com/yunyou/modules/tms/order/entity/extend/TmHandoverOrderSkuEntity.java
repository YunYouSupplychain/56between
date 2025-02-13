package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmHandoverOrderSku;

public class TmHandoverOrderSkuEntity extends TmHandoverOrderSku {
    private static final long serialVersionUID = 1L;

    private String ownerName;
    private String skuName;

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
}
