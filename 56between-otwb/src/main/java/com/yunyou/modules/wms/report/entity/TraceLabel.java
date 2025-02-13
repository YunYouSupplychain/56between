package com.yunyou.modules.wms.report.entity;

/**
 * 托盘标签entity
 * @Author WMJ
 * @version 2019/08/08
 */
public class TraceLabel {
    private String traceId;
    private String skuCode;
    private String skuName;
    private String ownerCode;
    private String ownerName;
    private String packCode;
    private String packDesc;
    private Double qtyPlanEa;
    private String toLoc;
    private String lotAtt01;
    private String lotAtt02;
    private String lotAtt03;
    private String orgId;
    private String orgName;
    private String skuDef3;
    private Double unitQty;
    private Double qtyPlanBox;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public Double getQtyPlanEa() {
        return qtyPlanEa;
    }

    public void setQtyPlanEa(Double qtyPlanEa) {
        this.qtyPlanEa = qtyPlanEa;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(String lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public String getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(String lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(String lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    public String getSkuDef3() {
        return skuDef3;
    }

    public void setSkuDef3(String skuDef3) {
        this.skuDef3 = skuDef3;
    }

    public Double getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(Double unitQty) {
        this.unitQty = unitQty;
    }

    public Double getQtyPlanBox() {
        return qtyPlanBox;
    }

    public void setQtyPlanBox(Double qtyPlanBox) {
        this.qtyPlanBox = qtyPlanBox;
    }
}
