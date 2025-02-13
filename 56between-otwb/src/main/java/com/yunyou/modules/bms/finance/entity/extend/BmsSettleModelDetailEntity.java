package com.yunyou.modules.bms.finance.entity.extend;

import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.modules.bms.finance.entity.BmsSettleModelDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 描述：结算模型明细扩展实体
 * <p>
 *
 * @author Jianhua
 * @version 2019-11-6
 */
public class BmsSettleModelDetailEntity extends BmsSettleModelDetail {
    private static final long serialVersionUID = 9057654003387985385L;
    // 模型明细包含参数
    private List<BmsSettleModelDetailParamsEntity> includeParams;
    // 模型明细排除参数
    private List<BmsSettleModelDetailParamsEntity> excludeParams;
    // 结算对象编码
    private String settleObjectCode;
    // 结算对象名称
    private String settleObjectName;
    // 系统合同编号
    private String sysContractNo;
    // 客户合同编号
    private String contractNo;
    // 子合同编号
    private String subcontractNo;
    // 合同状态
    private String contractStatus;
    // 有效开始日期
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date effectiveDateFm;
    // 有效结束日期
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date effectiveDateTo;
    // 费用模块
    private String billModule;
    // 费用科目代码
    private String billSubjectCode;
    // 费用科目名称
    private String billSubjectName;
    // 应收应付
    private String receivablePayable;
    // 费用类别
    private String billCategory;
    // 计费条款代码
    private String billTermsCode;
    // 计费条款说明
    private String billTermsDesc;
    // 输出对象
    private String outputObjects;
    // 发生量
    private String occurrenceQuantity;
    // 计费公式编码
    private String formulaCode;
    // 计费公式名称
    private String formulaName;
    // 机构名称
    private String orgName;

    public BmsSettleModelDetailEntity() {
    }

    public BmsSettleModelDetailEntity(String id) {
        super(id);
    }

    public List<BmsSettleModelDetailParamsEntity> getIncludeParams() {
        return includeParams;
    }

    public void setIncludeParams(List<BmsSettleModelDetailParamsEntity> includeParams) {
        this.includeParams = includeParams;
    }

    public List<BmsSettleModelDetailParamsEntity> getExcludeParams() {
        return excludeParams;
    }

    public void setExcludeParams(List<BmsSettleModelDetailParamsEntity> excludeParams) {
        this.excludeParams = excludeParams;
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

    public String getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(String receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public String getBillCategory() {
        return billCategory;
    }

    public void setBillCategory(String billCategory) {
        this.billCategory = billCategory;
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

    public String getOutputObjects() {
        return outputObjects;
    }

    public void setOutputObjects(String outputObjects) {
        this.outputObjects = outputObjects;
    }

    public String getOccurrenceQuantity() {
        return occurrenceQuantity;
    }

    public void setOccurrenceQuantity(String occurrenceQuantity) {
        this.occurrenceQuantity = occurrenceQuantity;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
