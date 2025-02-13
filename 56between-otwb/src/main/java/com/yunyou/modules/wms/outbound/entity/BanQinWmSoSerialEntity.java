package com.yunyou.modules.wms.outbound.entity;

/**
 * 描述：出库序列号扩展Entity
 *
 * @auther: Jianhua on 2019/2/15
 */
public class BanQinWmSoSerialEntity extends BanQinWmSoSerial {
    private String ownerName;
    private String skuName;
    private String isUnSerial;

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

    public String getIsUnSerial() {
        return isUnSerial;
    }

    public void setIsUnSerial(String isUnSerial) {
        this.isUnSerial = isUnSerial;
    }
}
