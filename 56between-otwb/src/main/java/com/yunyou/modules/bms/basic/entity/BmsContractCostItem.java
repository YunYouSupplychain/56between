package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 描述：合同明细费用
 *
 * @author liujianhua
 * @version 2019-11-14
 */
public class BmsContractCostItem extends DataEntity<BmsContractCostItem> {

    private static final long serialVersionUID = 1L;
    // 系统合同编号
    private String sysContractNo;
    // 费用模块
    private String billModule;
    // 费用科目代码
    private String billSubjectCode;
    // 计费条款代码
    private String billTermsCode;
    // 应收应付
    private String receivablePayable;
    // 公式编码
    private String formulaCode;
    // 运输价格体系编码
    private String transportGroupCode;
    // 税率
    private BigDecimal taxRate;
    //系数
    private BigDecimal coefficient;
    // 最大金额
    private BigDecimal maxAmount;
    // 最小金额
    private BigDecimal minAmount;
    // 机构
    private String orgId;

    public BmsContractCostItem() {
        super();
    }

    public BmsContractCostItem(String id) {
        super(id);
    }

    public BmsContractCostItem(String sysContractNo, String orgId) {
        this.sysContractNo = sysContractNo;
        this.orgId = orgId;
    }

    public String getSysContractNo() {
        return sysContractNo;
    }

    public void setSysContractNo(String sysContractNo) {
        this.sysContractNo = sysContractNo;
    }

    public String getBillModule() {
        return billModule;
    }

    public void setBillModule(String billModule) {
        this.billModule = billModule;
    }

    public String getBillSubjectCode() {
        return billSubjectCode;
    }

    public void setBillSubjectCode(String billSubjectCode) {
        this.billSubjectCode = billSubjectCode;
    }

    public String getBillTermsCode() {
        return billTermsCode;
    }

    public void setBillTermsCode(String billTermsCode) {
        this.billTermsCode = billTermsCode;
    }

    public String getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(String receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public String getFormulaCode() {
        return formulaCode;
    }

    public void setFormulaCode(String formulaCode) {
        this.formulaCode = formulaCode;
    }

    public String getTransportGroupCode() {
        return transportGroupCode;
    }

    public void setTransportGroupCode(String transportGroupCode) {
        this.transportGroupCode = transportGroupCode;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}