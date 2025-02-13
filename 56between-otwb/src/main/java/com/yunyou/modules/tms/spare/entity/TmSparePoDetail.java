package com.yunyou.modules.tms.spare.entity;

import com.yunyou.core.persistence.DataEntity;

public class TmSparePoDetail extends DataEntity<TmSparePoDetail> {
    private static final long serialVersionUID = 4784880927586082570L;

    private String sparePoNo;
    private String lineNo;
    private String fittingCode;
    private Double poQty;
    private String supplierCode;
    private Double price;
    private String orgId;
    private String baseOrgId;

    public TmSparePoDetail() {
    }

    public TmSparePoDetail(String id) {
        super(id);
    }

    public String getSparePoNo() {
        return sparePoNo;
    }

    public void setSparePoNo(String sparePoNo) {
        this.sparePoNo = sparePoNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getFittingCode() {
        return fittingCode;
    }

    public void setFittingCode(String fittingCode) {
        this.fittingCode = fittingCode;
    }

    public Double getPoQty() {
        return poQty;
    }

    public void setPoQty(Double poQty) {
        this.poQty = poQty;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }
}
