package com.yunyou.modules.bms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 合同台账
 */
public class BmsContractAccount extends DataEntity<BmsContractAccount> {
    private static final long serialVersionUID = 1602581750264058770L;
    // 区域编码
    private String regionCode;
    @ExcelField(title = "区域", type = 1)
    private String regionName;
    // 城市ID
    private String areaId;
    @ExcelField(title = "城市", type = 1)
    private String area;
    @ExcelField(title = "项目", type = 1)
    private String project;
    @ExcelField(title = "仓别", type = 1)
    private String orgCode;
    @ExcelField(title = "合同号", type = 1)
    private String sysContractNo;
    @ExcelField(title = "应收应付", type = 1)
    private String receivablePayable;
    // 结算对象编码
    private String settleObjectCode;
    @ExcelField(title = "结算对象", type = 1)
    private String settleObjectName;
    // 开票对象编码
    private String invoiceObjectCode;
    @ExcelField(title = "开票对象", type = 1)
    private String invoiceObjectName;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "合同起租日", format = DateFormatUtil.PATTERN_ISO_ON_DATE, type = 1)
    private Date effectiveDateFm;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "合同终止日", format = DateFormatUtil.PATTERN_ISO_ON_DATE, type = 1)
    private Date effectiveDateTo;
    @ExcelField(title = "合同类别", dictType = "BMS_CONTRACT_CATEGORY", type = 1)
    private String contractCategory;
    @ExcelField(title = "合同类型", dictType = "BMS_CONTRACT_TYPE", type = 1)
    private String contractType;
    @ExcelField(title = "合同概述", type = 1)
    private String contractDesc;
    @ExcelField(title = "归属公司", type = 1)
    private String belongToCompany;
    @ExcelField(title = "签约人", type = 1)
    private String contractor;
    @ExcelField(title = "客户对账人", type = 1)
    private String checkAccountsPerson;
    @ExcelField(title = "对账负责人", type = 1)
    private String checkAccountsDirector;
    @ExcelField(title = "对账时间", type = 1)
    private String checkAccountsTime;
    @ExcelField(title = "开票要求", type = 1)
    private String billingRequirement;
    @ExcelField(title = "发票类型", dictType = "BMS_INVOICE_TYPE", type = 1)
    private String invoiceType;
    @ExcelField(title = "发票税率", type = 1)
    private Double invoiceTaxRate;
    @ExcelField(title = "合同状态", dictType = "BMS_CONTRACT_STATUS", type = 1)
    private String contractStatus;
    // 机构ID
    private String orgId;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSysContractNo() {
        return sysContractNo;
    }

    public void setSysContractNo(String sysContractNo) {
        this.sysContractNo = sysContractNo;
    }

    public String getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(String receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    public String getInvoiceObjectCode() {
        return invoiceObjectCode;
    }

    public void setInvoiceObjectCode(String invoiceObjectCode) {
        this.invoiceObjectCode = invoiceObjectCode;
    }

    public String getInvoiceObjectName() {
        return invoiceObjectName;
    }

    public void setInvoiceObjectName(String invoiceObjectName) {
        this.invoiceObjectName = invoiceObjectName;
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

    public String getContractCategory() {
        return contractCategory;
    }

    public void setContractCategory(String contractCategory) {
        this.contractCategory = contractCategory;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractDesc() {
        return contractDesc;
    }

    public void setContractDesc(String contractDesc) {
        this.contractDesc = contractDesc;
    }

    public String getBelongToCompany() {
        return belongToCompany;
    }

    public void setBelongToCompany(String belongToCompany) {
        this.belongToCompany = belongToCompany;
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

    public String getCheckAccountsTime() {
        return checkAccountsTime;
    }

    public void setCheckAccountsTime(String checkAccountsTime) {
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

    public Double getInvoiceTaxRate() {
        return invoiceTaxRate;
    }

    public void setInvoiceTaxRate(Double invoiceTaxRate) {
        this.invoiceTaxRate = invoiceTaxRate;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
