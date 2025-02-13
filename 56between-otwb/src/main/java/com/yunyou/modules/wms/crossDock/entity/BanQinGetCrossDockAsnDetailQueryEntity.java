package com.yunyou.modules.wms.crossDock.entity;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-02-14
 */
public class BanQinGetCrossDockAsnDetailQueryEntity {
    private String[] asnNos;
    private String orgId;
    private String ownerCode;
    private String skuCode;
    private String asnNo;
    private String asnType;
    private String lineNo;
    private String skuName;
    private String status;
    private Double qtyPlanUom;
    private Double qtyPlanEa;
    private String uom;
    private String cdprDesc;
    private String packCode;
    private String cdpaFormat;
    private Date fmEta;
    private Date toEta;

    public String[] getAsnNos() {
        return asnNos;
    }

    public void setAsnNos(String[] asnNos) {
        this.asnNos = asnNos;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    public String getAsnType() {
        return asnType;
    }

    public void setAsnType(String asnType) {
        this.asnType = asnType;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getQtyPlanUom() {
        return qtyPlanUom;
    }

    public void setQtyPlanUom(Double qtyPlanUom) {
        this.qtyPlanUom = qtyPlanUom;
    }

    public Double getQtyPlanEa() {
        return qtyPlanEa;
    }

    public void setQtyPlanEa(Double qtyPlanEa) {
        this.qtyPlanEa = qtyPlanEa;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getCdprDesc() {
        return cdprDesc;
    }

    public void setCdprDesc(String cdprDesc) {
        this.cdprDesc = cdprDesc;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getCdpaFormat() {
        return cdpaFormat;
    }

    public void setCdpaFormat(String cdpaFormat) {
        this.cdpaFormat = cdpaFormat;
    }

    public Date getFmEta() {
        return fmEta;
    }

    public void setFmEta(Date fmEta) {
        this.fmEta = fmEta;
    }

    public Date getToEta() {
        return toEta;
    }

    public void setToEta(Date toEta) {
        this.toEta = toEta;
    }
}
