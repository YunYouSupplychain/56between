package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmRepairOrderInboundInfo extends DataEntity<TmRepairOrderInboundInfo> {
    private static final long serialVersionUID = 1L;
    private String repairNo;        // 维修单号
    private String lineNo;          // 行号
    private String fittingCode;     // 备件编码
    private String barcode;         // 备件条码
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date operateTime;       // 操作时间
    private String operator;        // 操作人
    private String supplierCode;    // 供应商编码
    private Double price;           // 金额
    private String orgId;           // 机构ID
    private String baseOrgId;       // 基础数据机构ID

    public TmRepairOrderInboundInfo() {
    }

    public TmRepairOrderInboundInfo(String id) {
        super(id);
    }

    public String getRepairNo() {
        return repairNo;
    }

    public void setRepairNo(String repairNo) {
        this.repairNo = repairNo;
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
