package com.yunyou.modules.wms.inbound.entity;

/**
 * 描述：入库序列扩展Entity
 *
 * @auther: Jianhua on 2019/1/28
 */
public class BanQinWmAsnSerialEntity extends BanQinWmAsnSerial {
    private String ownerName;
    private String skuName;
    private String barCode;
    private String supBarCode;
    private String lineNo;

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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSupBarCode() {
        return supBarCode;
    }

    public void setSupBarCode(String supBarCode) {
        this.supBarCode = supBarCode;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }
}
