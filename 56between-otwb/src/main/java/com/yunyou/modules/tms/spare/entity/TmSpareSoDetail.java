package com.yunyou.modules.tms.spare.entity;

import com.yunyou.core.persistence.DataEntity;

public class TmSpareSoDetail extends DataEntity<TmSpareSoDetail> {
    private static final long serialVersionUID = 5321758646473515932L;

    private String spareSoNo;
    private String lineNo;
    private String fittingCode;
    private Double soQty;
    private String supplierCode;
    private Double price;
    private String orgId;
    private String baseOrgId;

    public TmSpareSoDetail() {
    }

    public TmSpareSoDetail(String id) {
        super(id);
    }

    public TmSpareSoDetail(String spareSoNo, String orgId) {
        this.spareSoNo = spareSoNo;
        this.orgId = orgId;
    }

    public String getSpareSoNo() {
        return spareSoNo;
    }

    public void setSpareSoNo(String spareSoNo) {
        this.spareSoNo = spareSoNo;
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

    public Double getSoQty() {
        return soQty;
    }

    public void setSoQty(Double soQty) {
        this.soQty = soQty;
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
