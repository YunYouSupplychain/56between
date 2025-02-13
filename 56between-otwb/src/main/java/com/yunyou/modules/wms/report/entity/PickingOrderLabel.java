package com.yunyou.modules.wms.report.entity;

/**
 * 拣货单报表entity
 * @Author WMJ
 * @version 2019/06/13
 */
public class PickingOrderLabel {
    private String soNo;
    private String storehouse;
    private String ownerCode;
    private String ownerName;
    private String soType;
    private String traceId;
    private String lotNum;
    private String lotAtt01;		// 批次属性01
    private String skuCode;
    private String skuName;
    private String packDesc;
    private String pickLoc;
    private Double qty;
    private Double boxQty;
    private Double qtyAvailable;
    private Double totalQty;
    private Double totalBoxQty;
    private String orgId;
    private Double riceNum;
    private Double ricePack;
    private String skuDef3;     // 箱规格

    private String pkSeq;   // 拣货顺序
    private String consigneeCode;   // 收货人
    private String consigneeName;   // 收货人

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSoType() {
        return soType;
    }

    public void setSoType(String soType) {
        this.soType = soType;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public String getPickLoc() {
        return pickLoc;
    }

    public void setPickLoc(String pickLoc) {
        this.pickLoc = pickLoc;
    }

    public String getStorehouse() {
        return storehouse;
    }

    public void setStorehouse(String storehouse) {
        this.storehouse = storehouse;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Double getRiceNum() {
        return riceNum;
    }

    public void setRiceNum(Double riceNum) {
        this.riceNum = riceNum;
    }

    public Double getRicePack() {
        return ricePack;
    }

    public void setRicePack(Double ricePack) {
        this.ricePack = ricePack;
    }

    public Double getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(Double boxQty) {
        this.boxQty = boxQty;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(String lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public Double getTotalBoxQty() {
        return totalBoxQty;
    }

    public void setTotalBoxQty(Double totalBoxQty) {
        this.totalBoxQty = totalBoxQty;
    }

    public String getSkuDef3() {
        return skuDef3;
    }

    public void setSkuDef3(String skuDef3) {
        this.skuDef3 = skuDef3;
    }

    public Double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(Double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    public String getPkSeq() {
        return pkSeq;
    }

    public void setPkSeq(String pkSeq) {
        this.pkSeq = pkSeq;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }
}
