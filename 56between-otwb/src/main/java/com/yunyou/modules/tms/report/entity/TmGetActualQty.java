package com.yunyou.modules.tms.report.entity;

import java.util.Date;

public class TmGetActualQty {
    private Date fmDate;
    private Date toDate;
    private String ownerCode;
    private String skuCode;
    private String baseOrgId;
    private String orgId;

    public Date getFmDate() {
        return fmDate;
    }

    public void setFmDate(Date fmDate) {
        this.fmDate = fmDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
