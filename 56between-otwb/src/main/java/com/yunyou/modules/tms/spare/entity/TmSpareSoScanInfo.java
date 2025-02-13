package com.yunyou.modules.tms.spare.entity;

import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmSpareSoScanInfo extends DataEntity<TmSpareSoScanInfo> {
    private static final long serialVersionUID = -9057003035437648354L;

    private String spareSoNo;
    private String orderType;
    private String lineNo;
    private String fittingCode;
    private String supplierCode;
    private String price;
    private String barcode;
    private Date operateTime;
    private String operator;
    private String orgId;
    private String baseOrgId;

    public TmSpareSoScanInfo() {
    }

    public TmSpareSoScanInfo(String id) {
        super(id);
    }

    public TmSpareSoScanInfo(String spareSoNo, String orgId) {
        this.spareSoNo = spareSoNo;
        this.orgId = orgId;
    }

    public String getSpareSoNo() {
        return spareSoNo;
    }

    public void setSpareSoNo(String spareSoNo) {
        this.spareSoNo = spareSoNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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
