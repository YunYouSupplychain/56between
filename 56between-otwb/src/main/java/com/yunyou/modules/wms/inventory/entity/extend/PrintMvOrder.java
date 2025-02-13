package com.yunyou.modules.wms.inventory.entity.extend;

import java.util.Date;

public class PrintMvOrder {

    private String mvNo;
    private String ownerName;
    private String reasonCode;
    private String reason;
    private String createOp;
    private Date createTime;
    private String auditOp;
    private Date auditTime;
    private String orgId;
    private String lineNo;
    private String skuCode;
    private String skuName;
    private String lotNum;
    private String fmLocCode;
    private String fmTraceId;
    private String toLocCode;
    private String toTraceId;
    private String uom;
    private Double qtyEa;
    private Double qtyUom;

    public String getMvNo() {
        return mvNo;
    }

    public void setMvNo(String mvNo) {
        this.mvNo = mvNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAuditOp() {
        return auditOp;
    }

    public void setAuditOp(String auditOp) {
        this.auditOp = auditOp;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
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

    public String getFmLocCode() {
        return fmLocCode;
    }

    public void setFmLocCode(String fmLocCode) {
        this.fmLocCode = fmLocCode;
    }

    public String getFmTraceId() {
        return fmTraceId;
    }

    public void setFmTraceId(String fmTraceId) {
        this.fmTraceId = fmTraceId;
    }

    public String getToLocCode() {
        return toLocCode;
    }

    public void setToLocCode(String toLocCode) {
        this.toLocCode = toLocCode;
    }

    public String getToTraceId() {
        return toTraceId;
    }

    public void setToTraceId(String toTraceId) {
        this.toTraceId = toTraceId;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQtyEa() {
        return qtyEa;
    }

    public void setQtyEa(Double qtyEa) {
        this.qtyEa = qtyEa;
    }

    public Double getQtyUom() {
        return qtyUom;
    }

    public void setQtyUom(Double qtyUom) {
        this.qtyUom = qtyUom;
    }
}
