package com.yunyou.modules.bms.calculate.contract;

import com.yunyou.modules.bms.basic.entity.BmsBillFormulaParameter;
import com.yunyou.modules.bms.finance.entity.extend.BmsCalcTermsParams;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 费用计算合同数据
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsCalcContractData {

    /**
     * 结算日期从
     */
    private Date settleDateFm;
    /**
     * 结算日期到
     */
    private Date settleDateTo;
    /**
     * 结算对象编码
     */
    private String settleObjectCode;
    /**
     * 结算对象名称
     */
    private String settleObjectName;
    /**
     * 模型编码
     */
    private String settleModelCode;
    /**
     * 模型明细ID
     */
    private String settleModelDetailId;
    /**
     * 系统合同号
     */
    private String sysContractNo;
    /**
     * 合同号
     */
    private String contractNo;
    /**
     * 合同明细ID
     */
    private String contractDetailId;
    /**
     * 合同有效日期从
     */
    private Date effectiveDateFm;
    /**
     * 合同有效日期到
     */
    private Date effectiveDateTo;
    /**
     * 费用模块
     */
    private String billModule;
    /**
     * 费用科目编码
     */
    private String billSubjectCode;
    /**
     * 费用科目名称
     */
    private String billSubjectName;
    /**
     * 应收应付
     */
    private String arOrAp;
    /**
     * 计费条款编码
     */
    private String billTermsCode;
    /**
     * 计费条款说明
     */
    private String billTermsDesc;
    /**
     * 处理方法名称
     */
    private String methodName;
    /**
     * 条款输出对象
     */
    private String outputObjects;
    /**
     * 条款包含条件参数
     */
    private List<BmsCalcTermsParams> includeParams;
    /**
     * 条款排除条件参数
     */
    private List<BmsCalcTermsParams> excludeParams;
    /**
     * 公式编码
     */
    private String formulaCode;
    /**
     * 公式名称
     */
    private String formulaName;
    /**
     * 公式
     */
    private String formula;
    /**
     * 公式参数
     */
    private List<BmsBillFormulaParameter> formulaParameters;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 合同系数
     */
    private BigDecimal coefficient;
    /**
     * 最大金额
     */
    private BigDecimal maxAmount;
    /**
     * 最小金额
     */
    private BigDecimal minAmount;
    /**
     * 机构ID
     */
    private String orgId;

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

    public String getSettleModelCode() {
        return settleModelCode;
    }

    public void setSettleModelCode(String settleModelCode) {
        this.settleModelCode = settleModelCode;
    }

    public String getSettleModelDetailId() {
        return settleModelDetailId;
    }

    public void setSettleModelDetailId(String settleModelDetailId) {
        this.settleModelDetailId = settleModelDetailId;
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

    public String getContractDetailId() {
        return contractDetailId;
    }

    public void setContractDetailId(String contractDetailId) {
        this.contractDetailId = contractDetailId;
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

    public String getBillSubjectName() {
        return billSubjectName;
    }

    public void setBillSubjectName(String billSubjectName) {
        this.billSubjectName = billSubjectName;
    }

    public String getArOrAp() {
        return arOrAp;
    }

    public void setArOrAp(String arOrAp) {
        this.arOrAp = arOrAp;
    }

    public String getBillTermsCode() {
        return billTermsCode;
    }

    public void setBillTermsCode(String billTermsCode) {
        this.billTermsCode = billTermsCode;
    }

    public String getBillTermsDesc() {
        return billTermsDesc;
    }

    public void setBillTermsDesc(String billTermsDesc) {
        this.billTermsDesc = billTermsDesc;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getOutputObjects() {
        return outputObjects;
    }

    public void setOutputObjects(String outputObjects) {
        this.outputObjects = outputObjects;
    }

    public List<BmsCalcTermsParams> getIncludeParams() {
        return includeParams;
    }

    public void setIncludeParams(List<BmsCalcTermsParams> includeParams) {
        this.includeParams = includeParams;
    }

    public List<BmsCalcTermsParams> getExcludeParams() {
        return excludeParams;
    }

    public void setExcludeParams(List<BmsCalcTermsParams> excludeParams) {
        this.excludeParams = excludeParams;
    }

    public String getFormulaCode() {
        return formulaCode;
    }

    public void setFormulaCode(String formulaCode) {
        this.formulaCode = formulaCode;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public List<BmsBillFormulaParameter> getFormulaParameters() {
        return formulaParameters;
    }

    public void setFormulaParameters(List<BmsBillFormulaParameter> formulaParameters) {
        this.formulaParameters = formulaParameters;
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
