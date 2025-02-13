package com.yunyou.modules.tms.order.entity.extend;

import java.io.Serializable;
import java.util.Date;

public class TmTransportSignInfo implements Serializable {
    private static final long serialVersionUID = 7437742956547132243L;

    // 运输订单号
    private String transportNo;
    // 标签号
    private String labelNo;
    // 签收人
    private String signBy;
    // 签收时间
    private Date signTime;
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

    public String getSignBy() {
        return signBy;
    }

    public void setSignBy(String signBy) {
        this.signBy = signBy;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
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
