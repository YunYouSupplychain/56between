package com.yunyou.modules.wms.crossDock.entity;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-02-14
 */
public class BanQinWmTaskCdSoDetailByIndirectQueryEntity {
    private String[] skuCodes;
    private String[] ownerCodes;
    private Date fmEtdFm;
    private Date fmEtdTo;
    private Date toEtdFm;
    private Date toEtdTo;
    private String soNo;
    private String orgId;

    private String lineNo;
    private String ownerCode;
    private String skuCode;
    private String ownerName;
    private String skuName;
    private String cdType;
    private String status;
    private Double qtySoEa;
    private Double qtyPreallocEa;
    private Double qtyAllocEa;
    private Double qtyPkEa;
    private Double qtyShipEa;

    public String[] getSkuCodes() {
        return skuCodes;
    }

    public void setSkuCodes(String[] skuCodes) {
        this.skuCodes = skuCodes;
    }

    public String[] getOwnerCodes() {
        return ownerCodes;
    }

    public void setOwnerCodes(String[] ownerCodes) {
        this.ownerCodes = ownerCodes;
    }

    public Date getFmEtdFm() {
        return fmEtdFm;
    }

    public void setFmEtdFm(Date fmEtdFm) {
        this.fmEtdFm = fmEtdFm;
    }

    public Date getFmEtdTo() {
        return fmEtdTo;
    }

    public void setFmEtdTo(Date fmEtdTo) {
        this.fmEtdTo = fmEtdTo;
    }

    public Date getToEtdFm() {
        return toEtdFm;
    }

    public void setToEtdFm(Date toEtdFm) {
        this.toEtdFm = toEtdFm;
    }

    public Date getToEtdTo() {
        return toEtdTo;
    }

    public void setToEtdTo(Date toEtdTo) {
        this.toEtdTo = toEtdTo;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
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

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

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

    public String getCdType() {
        return cdType;
    }

    public void setCdType(String cdType) {
        this.cdType = cdType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getQtySoEa() {
        return qtySoEa;
    }

    public void setQtySoEa(Double qtySoEa) {
        this.qtySoEa = qtySoEa;
    }

    public Double getQtyPreallocEa() {
        return qtyPreallocEa;
    }

    public void setQtyPreallocEa(Double qtyPreallocEa) {
        this.qtyPreallocEa = qtyPreallocEa;
    }

    public Double getQtyAllocEa() {
        return qtyAllocEa;
    }

    public void setQtyAllocEa(Double qtyAllocEa) {
        this.qtyAllocEa = qtyAllocEa;
    }

    public Double getQtyPkEa() {
        return qtyPkEa;
    }

    public void setQtyPkEa(Double qtyPkEa) {
        this.qtyPkEa = qtyPkEa;
    }

    public Double getQtyShipEa() {
        return qtyShipEa;
    }

    public void setQtyShipEa(Double qtyShipEa) {
        this.qtyShipEa = qtyShipEa;
    }
}
