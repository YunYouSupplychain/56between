package com.yunyou.modules.bms.finance.entity;

import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class BmsBill extends DataEntity<BmsBill> {
    private static final long serialVersionUID = -5565129023873703478L;

    private String confirmNo;    // 费用单号
    private String status;       // 状态
    private String settleObjCode;// 结算对象编码
    private String settleObjName;// 结算对象名称
    private Double amount;       // 总费用金额
    private Double writeOffAmount;// 核销金额
    private String orgId;        // 机构

    private Date createTimeFm;
    private Date createTimeTo;
    private String orgName;

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSettleObjCode() {
        return settleObjCode;
    }

    public void setSettleObjCode(String settleObjCode) {
        this.settleObjCode = settleObjCode;
    }

    public String getSettleObjName() {
        return settleObjName;
    }

    public void setSettleObjName(String settleObjName) {
        this.settleObjName = settleObjName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getWriteOffAmount() {
        return writeOffAmount;
    }

    public void setWriteOffAmount(Double writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Date getCreateTimeFm() {
        return createTimeFm;
    }

    public void setCreateTimeFm(Date createTimeFm) {
        this.createTimeFm = createTimeFm;
    }

    public Date getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(Date createTimeTo) {
        this.createTimeTo = createTimeTo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
