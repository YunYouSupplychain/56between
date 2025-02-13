package com.yunyou.modules.bms.report.entity;

import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 描述：合同科目查询实体
 * 
 * @author liujianhua created on 2019-12-9
 */
public class BmsReportContractSubjectQuery extends DataEntity<BmsReportContractSubjectQuery> {

    private static final long serialVersionUID = 3785432962130922329L;
    // 结算对象编码
    private String settleCode;
    // 系统合同编号
    private String sysContractNo;
    // 合同编号
    private String contractNo;
    // 有效期从
    private Date effectiveDateFm;
    // 有效期到
    private Date effectiveDateTo;
    // 费用科目编码
    private String billSubjectCode;
    // 操作人
    private String operator;
    // 机构
    private String orgId;

    public String getSettleCode() {
        return settleCode;
    }

    public void setSettleCode(String settleCode) {
        this.settleCode = settleCode;
    }

    public String getSysContractNo() {
        return sysContractNo;
    }

    public void setSysContractNo(String sysContractNo) {
        this.sysContractNo = sysContractNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Date getEffectiveDateFm() {
        return effectiveDateFm;
    }

    public void setEffectiveDateFm(Date effectiveDateFm) {
        this.effectiveDateFm = effectiveDateFm;
    }

    public Date getEffectiveDateTo() {
        return effectiveDateTo;
    }

    public void setEffectiveDateTo(Date effectiveDateTo) {
        this.effectiveDateTo = effectiveDateTo;
    }

    public String getBillSubjectCode() {
        return billSubjectCode;
    }

    public void setBillSubjectCode(String billSubjectCode) {
        this.billSubjectCode = billSubjectCode;
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
}
