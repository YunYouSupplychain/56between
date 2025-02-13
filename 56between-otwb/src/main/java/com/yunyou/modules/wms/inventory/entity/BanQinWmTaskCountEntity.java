package com.yunyou.modules.wms.inventory.entity;

/**
 * 盘点任务Entity
 * @author WMJ
 * @version 2019-06-11
 */
public class BanQinWmTaskCountEntity extends BanQinWmTaskCount {
    private String ownerName;
    private String skuName;
    private String areaName;
    private String zoneName;
    private String packDesc;
    private String uomDesc;
    private Double uomQuantity;
    private String preCountOp;
    private String preCountNo;
    private Double preQtyCountEa;

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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public Double getUomQuantity() {
        return uomQuantity;
    }

    public void setUomQuantity(Double uomQuantity) {
        this.uomQuantity = uomQuantity;
    }

    public String getPreCountOp() {
        return preCountOp;
    }

    public void setPreCountOp(String preCountOp) {
        this.preCountOp = preCountOp;
    }

    public String getPreCountNo() {
        return preCountNo;
    }

    public void setPreCountNo(String preCountNo) {
        this.preCountNo = preCountNo;
    }

    public Double getPreQtyCountEa() {
        return preQtyCountEa;
    }

    public void setPreQtyCountEa(Double preQtyCountEa) {
        this.preQtyCountEa = preQtyCountEa;
    }
}
