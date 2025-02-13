package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_INV_PickBoxDetail_Response implements Serializable {
    // 出库单号
    private String soNo;
    // 状态
    private String status;
    // 复核状态(00未复核、99已复核、90不复核)
    private String checkStatus;
    // 货主编码
    private String ownerCode;
    // 货主名称
    private String ownerName;
    // 商品编码
    private String skuCode;
    // 商品名称
    private String skuName;
    // 批次号
    private String lotNum;
    // 库位编码
    private String locCode;
    // 跟踪号
    private String traceId;
    // 包装编码
    private String packCode;
    // 包装规格
    private String packDesc;
    // 包装单位
    private String uom;
    // 包装数量
    private Double qtyUom;
    // EA数量
    private Double qtyEa;
    // 目标库位编码
    private String toLoc;
    // 目标跟踪号
    private String toId;

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
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

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQtyUom() {
        return qtyUom;
    }

    public void setQtyUom(Double qtyUom) {
        this.qtyUom = qtyUom;
    }

    public Double getQtyEa() {
        return qtyEa;
    }

    public void setQtyEa(Double qtyEa) {
        this.qtyEa = qtyEa;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}
