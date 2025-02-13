package com.yunyou.modules.wms.report.entity;

/**
 * 盘点任务报表entity
 * @Author WMJ
 * @version 2019/10/21
 */
public class CountTaskLabel {
    private String countNo;
    private String countType;
    private String countMode;
    private String countId;
    private String traceId;
    private String zoneCode;
    private String locCode;
    private String skuCode;
    private String skuName;
    private String packDesc;
    private Double qtyCountEa;
    private Double invQty;

    public String getCountNo() {
        return countNo;
    }

    public void setCountNo(String countNo) {
        this.countNo = countNo;
    }

    public String getCountType() {
        return countType;
    }

    public void setCountType(String countType) {
        this.countType = countType;
    }

    public String getCountMode() {
        return countMode;
    }

    public void setCountMode(String countMode) {
        this.countMode = countMode;
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
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

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public Double getQtyCountEa() {
        return qtyCountEa;
    }

    public void setQtyCountEa(Double qtyCountEa) {
        this.qtyCountEa = qtyCountEa;
    }

    public Double getInvQty() {
        return invQty;
    }

    public void setInvQty(Double invQty) {
        this.invQty = invQty;
    }
}
