package com.yunyou.modules.wms.inventory.entity;

public class BanQinWmAdSerialEntity extends BanQinWmAdSerial {
    // 货主名称
    private String ownerName;
    // 商品名称
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
