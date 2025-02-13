package com.yunyou.modules.tms.order.entity.extend;

import java.io.Serializable;
import java.util.Date;

public class TmTransportReceiptInfo implements Serializable {
    private static final long serialVersionUID = 8689582018416501402L;

    // 运输订单号
    private String transportNo;
    // 标签号
    private String labelNo;
    // 回单人
    private String receiptBy;
    // 回单时间
    private Date receiptTime;
    // 备注
    private String remarks;
    // 机构ID
    private String orgId;
    // 基础数据机构ID
    private String baseOrgId;

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getReceiptBy() {
        return receiptBy;
    }

    public void setReceiptBy(String receiptBy) {
        this.receiptBy = receiptBy;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
