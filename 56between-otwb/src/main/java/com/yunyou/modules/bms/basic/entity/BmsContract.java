package com.yunyou.modules.bms.basic.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同Entity
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
public class BmsContract extends DataEntity<BmsContract> {

    private static final long serialVersionUID = 1L;
    // 系统合同编号
    private String sysContractNo;
    // 客户合同编号
    private String contractNo;
    // 子合同编号
    private String subcontractNo;
    // 结算对象编码
    private String settleObjectCode;
    // 有效开始日期
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date effectiveDateFm;
    // 有效结束日期
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date effectiveDateTo;
    // 合同类型
    private String contractType;
    // 归属公司
    private String belongToCompany;
    // 合同类别
    private String contractCategory;
    // 合同归属
    private String contractAttribution;
    // 签约人
    private String contractor;
    // 客户对账人
    private String checkAccountsPerson;
    // 对账负责人
    private String checkAccountsDirector;
    // 对账时间
    private Date checkAccountsTime;
    // 开票要求
    private String billingRequirement;
    // 发票类型
    private String invoiceType;
    // 发票税率
    private BigDecimal invoiceTaxRate;
    // 合同状态(新建、有效、无效)
    private String contractStatus;
    // 机构ID
    private String orgId;

    public BmsContract() {
        super();
    }

    public BmsContract(String id) {
        super(id);
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

    public String getSubcontractNo() {
        return subcontractNo;
    }

    public void setSubcontractNo(String subcontractNo) {
        this.subcontractNo = subcontractNo;
    }

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getBelongToCompany() {
        return belongToCompany;
    }

    public void setBelongToCompany(String belongToCompany) {
        this.belongToCompany = belongToCompany;
    }

    public String getContractCategory() {
        return contractCategory;
    }

    public void setContractCategory(String contractCategory) {
        this.contractCategory = contractCategory;
    }

    public String getContractAttribution() {
        return contractAttribution;
    }

    public void setContractAttribution(String contractAttribution) {
        this.contractAttribution = contractAttribution;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getCheckAccountsPerson() {
        return checkAccountsPerson;
    }

    public void setCheckAccountsPerson(String checkAccountsPerson) {
        this.checkAccountsPerson = checkAccountsPerson;
    }

    public String getCheckAccountsDirector() {
        return checkAccountsDirector;
    }

    public void setCheckAccountsDirector(String checkAccountsDirector) {
        this.checkAccountsDirector = checkAccountsDirector;
    }

    public Date getCheckAccountsTime() {
        return checkAccountsTime;
    }

    public void setCheckAccountsTime(Date checkAccountsTime) {
        this.checkAccountsTime = checkAccountsTime;
    }

    public String getBillingRequirement() {
        return billingRequirement;
    }

    public void setBillingRequirement(String billingRequirement) {
        this.billingRequirement = billingRequirement;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public BigDecimal getInvoiceTaxRate() {
        return invoiceTaxRate;
    }

    public void setInvoiceTaxRate(BigDecimal invoiceTaxRate) {
        this.invoiceTaxRate = invoiceTaxRate;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}