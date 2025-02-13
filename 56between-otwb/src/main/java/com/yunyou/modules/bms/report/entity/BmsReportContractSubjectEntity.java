package com.yunyou.modules.bms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 描述：合同科目实体
 *
 * @author liujianhua created on 2019-12-9
 */
public class BmsReportContractSubjectEntity extends DataEntity<BmsReportContractSubjectEntity> {

    private static final long serialVersionUID = 4655154772093184607L;
    @ExcelField(title = "系统合同编号", type = 1)
    private String sysContractNo;
    @ExcelField(title = "合同编号", type = 1)
    private String contractNo;
    @ExcelField(title = "结算对象编码", type = 1)
    private String settleCode;
    @ExcelField(title = "结算对象名称", type = 1)
    private String settleName;
    // 机构ID
    private String orgId;
    @ExcelField(title = "仓别", type = 1)
    private String orgName;
    @ExcelField(title = "费用模块", dictType = "BMS_BILL_MODULE",type = 1)
    private String billModule;
    @ExcelField(title = "费用科目编码", type = 1)
    private String billSubjectCode;
    @ExcelField(title = "费用科目", type = 1)
    private String billSubjectName;
    @ExcelField(title = "计费条款编码", type = 1)
    private String billTermsCode;
    @ExcelField(title = "计费条款", type = 1)
    private String billTermsDesc;
    @ExcelField(title = "公式名称", type = 1)
    private String methodName;
    @ExcelField(title = "操作人", type = 1)
    private String operator;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    @ExcelField(title = "操作时间", type = 1)
    private Date operateTime;
    @ExcelField(title = "阶梯范围从", type = 1)
    private Double fm;
    @ExcelField(title = "阶梯范围到", type = 1)
    private Double to;
    @ExcelField(title = "单价", type = 1)
    private Double price;
    @ExcelField(title = "物流点数", type = 1)
    private Double logisticsPoints;
    @ExcelField(title = "税点", type = 1)
    private Double taxRate;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "有效期从", format = DateFormatUtil.PATTERN_ISO_ON_DATE, type = 1)
    private Date effectiveDateFm;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "有效期到", format = DateFormatUtil.PATTERN_ISO_ON_DATE, type = 1)
    private Date effectiveDateTo;
    @ExcelField(title = "备注", type = 1)
    private String remarks;

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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Double getFm() {
        return fm;
    }

    public void setFm(Double fm) {
        this.fm = fm;
    }

    public Double getTo() {
        return to;
    }

    public void setTo(Double to) {
        this.to = to;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getLogisticsPoints() {
        return logisticsPoints;
    }

    public void setLogisticsPoints(Double logisticsPoints) {
        this.logisticsPoints = logisticsPoints;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
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

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
