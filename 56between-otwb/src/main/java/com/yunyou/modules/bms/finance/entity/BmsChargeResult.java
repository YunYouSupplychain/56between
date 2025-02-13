package com.yunyou.modules.bms.finance.entity;

import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 费用结果Entity
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsChargeResult extends DataEntity<BmsChargeResult> {

    /**
     * 结算批次号
     */
    private String settleLotNo;
    /**
     * 结算日期从
     */
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date settleDateFm;
    /**
     * 结算日期到
     */
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date settleDateTo;
    /**
     * 业务机构编码
     */
    private String businessOrgCode;
    /**
     * 业务机构名称
     */
    private String businessOrgName;
    /**
     * 结算模型编码
     */
    private String settleModelCode;
    /**
     * 结算对象编码
     */
    private String settleCode;
    /**
     * 结算对象名称
     */
    private String settleName;
    /**
     * 系统合同号
     */
    private String sysContractNo;
    /**
     * 合同号
     */
    private String contractNo;
    /**
     * 费用科目编码
     */
    private String subjectCode;
    /**
     * 费用科目名称
     */
    private String subjectName;
    /**
     * 条款编码
     */
    private String termsCode;
    /**
     * 条款说明
     */
    private String termsDesc;
    /**
     * 条款输出
     */
    private String termsOutput;
    /**
     * 计费公式
     */
    private String formula;
    /**
     * 应收应付
     */
    private String arOrAp;
    /**
     * 费用金额
     */
    private BigDecimal amount;
    /**
     * 机构ID
     */
    private String orgId;

    public BmsChargeResult() {
        super();
    }

    public BmsChargeResult(String id) {
        super(id);
    }

    public String getSettleLotNo() {
        return settleLotNo;
    }

    public void setSettleLotNo(String settleLotNo) {
        this.settleLotNo = settleLotNo;
    }

    public Date getSettleDateFm() {
        return settleDateFm;
    }

    public void setSettleDateFm(Date settleDateFm) {
        this.settleDateFm = settleDateFm;
    }

    public Date getSettleDateTo() {
        return settleDateTo;
    }

    public void setSettleDateTo(Date settleDateTo) {
        this.settleDateTo = settleDateTo;
    }

    public String getBusinessOrgCode() {
        return businessOrgCode;
    }

    public void setBusinessOrgCode(String businessOrgCode) {
        this.businessOrgCode = businessOrgCode;
    }

    public String getBusinessOrgName() {
        return businessOrgName;
    }

    public void setBusinessOrgName(String businessOrgName) {
        this.businessOrgName = businessOrgName;
    }

    public String getSettleModelCode() {
        return settleModelCode;
    }

    public void setSettleModelCode(String settleModelCode) {
        this.settleModelCode = settleModelCode;
    }

    public String getSettleCode() {
        return settleCode;
    }

    public void setSettleCode(String settleCode) {
        this.settleCode = settleCode;
    }

    public String getSettleName() {
        return settleName;
    }

    public void setSettleName(String settleName) {
        this.settleName = settleName;
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

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTermsCode() {
        return termsCode;
    }

    public void setTermsCode(String termsCode) {
        this.termsCode = termsCode;
    }

    public String getTermsDesc() {
        return termsDesc;
    }

    public void setTermsDesc(String termsDesc) {
        this.termsDesc = termsDesc;
    }

    public String getTermsOutput() {
        return termsOutput;
    }

    public void setTermsOutput(String termsOutput) {
        this.termsOutput = termsOutput;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getArOrAp() {
        return arOrAp;
    }

    public void setArOrAp(String arOrAp) {
        this.arOrAp = arOrAp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
