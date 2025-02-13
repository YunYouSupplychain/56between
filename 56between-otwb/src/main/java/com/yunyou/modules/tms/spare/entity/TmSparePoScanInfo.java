package com.yunyou.modules.tms.spare.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmSparePoScanInfo extends DataEntity<TmSparePoScanInfo> {
    private static final long serialVersionUID = 2504105886132087214L;

    private String sparePoNo;
    private String orderType;
    private String lineNo;
    private String fittingCode;
    private String supplierCode;
    private Double price;
    private String barcode;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date operateTime;
    private String operator;
    private String orgId;
    private String baseOrgId;

    public TmSparePoScanInfo() {
    }

    public TmSparePoScanInfo(String id) {
        super(id);
    }

    public TmSparePoScanInfo(String sparePoNo, String orgId) {
        this.sparePoNo = sparePoNo;
        this.orgId = orgId;
    }

    public String getSparePoNo() {
        return sparePoNo;
    }

    public void setSparePoNo(String sparePoNo) {
        this.sparePoNo = sparePoNo;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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
