package com.yunyou.modules.wms.crossDock.entity;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-02-14
 */
public class BanQinGetCrossDockAsnDetaiCountQueryEntity {
    private String[] ownerCodes;
    private String[] skuCodes;
    private String orgId;
    private String asnNo;
    private String[] asnTypes;
    private Date fmEtaFm;
    private Date fmEtaTo;
    private Date toEtaFm;
    private Date toEtaTo;
    private String ownerCode;
    private String ownerName;
    private String skuCode;
    private String skuName;
    private Double qty;
    private Double qtyPlanEa;
    private Long asnNum;
    private Date fmEta;
    private Date toEta;

    public String[] getOwnerCodes() {
        return ownerCodes;
    }

    public void setOwnerCodes(String[] ownerCodes) {
        this.ownerCodes = ownerCodes;
    }

    public String[] getSkuCodes() {
        return skuCodes;
    }

    public void setSkuCodes(String[] skuCodes) {
        this.skuCodes = skuCodes;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    public String[] getAsnTypes() {
        return asnTypes;
    }

    public void setAsnTypes(String[] asnTypes) {
        this.asnTypes = asnTypes;
    }

    public Date getFmEtaFm() {
        return fmEtaFm;
    }

    public void setFmEtaFm(Date fmEtaFm) {
        this.fmEtaFm = fmEtaFm;
    }

    public Date getFmEtaTo() {
        return fmEtaTo;
    }

    public void setFmEtaTo(Date fmEtaTo) {
        this.fmEtaTo = fmEtaTo;
    }

    public Date getToEtaFm() {
        return toEtaFm;
    }

    public void setToEtaFm(Date toEtaFm) {
        this.toEtaFm = toEtaFm;
    }

    public Date getToEtaTo() {
        return toEtaTo;
    }

    public void setToEtaTo(Date toEtaTo) {
        this.toEtaTo = toEtaTo;
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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getQtyPlanEa() {
        return qtyPlanEa;
    }

    public void setQtyPlanEa(Double qtyPlanEa) {
        this.qtyPlanEa = qtyPlanEa;
    }

    public Long getAsnNum() {
        return asnNum;
    }

    public void setAsnNum(Long asnNum) {
        this.asnNum = asnNum;
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
