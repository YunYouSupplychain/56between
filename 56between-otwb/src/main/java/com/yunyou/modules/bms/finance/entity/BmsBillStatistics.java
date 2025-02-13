package com.yunyou.modules.bms.finance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 费用统计Entity
 *
 * @author Jianhua Liu
 * @version 2019-05-29
 */
public class BmsBillStatistics extends DataEntity<BmsBillStatistics> {

    private static final long serialVersionUID = 1L;
    @Deprecated
    private String billNo;
    // 账单状态(新建/已生成账单/已核销)
    private String status;
    @ExcelField(title = "帐单号")
    private String confirmNo;
    @ExcelField(title = "模型编码")
    private String settleModelCode;
    @ExcelField(title = "结算对象编码")
    private String settleObjectCode;
    @ExcelField(title = "结算对象名称")
    private String settleObjectName;
    // 业务机构编码
    @ExcelField(title = "仓库编码")
    private String warehouseCode;
    // 业务机构名称
    @ExcelField(title = "仓库名称")
    private String warehouseName;
    // 结算方式
    private String settleMethod;
    // 结算类别
    private String settleCategory;
    @ExcelField(title = "系统合同编号")
    private String sysContractNo;
    @ExcelField(title = "客户合同编号")
    private String contractNo;
    // 子合同编号
    private String subcontractNo;
    @ExcelField(title = "费用模块", dictType = "BMS_BILL_MODULE")
    private String billModule;
    // 费用科目编码
    private String billSubjectCode;
    @ExcelField(title = "费用科目名称")
    private String billSubjectName;
    @ExcelField(title = "费用类别", dictType = "BMS_BILL_SUBJECT_CATEGORY")
    private String billCategory;
    // 计费条款编码
    private String billTermsCode;
    // 计费条款说明
    private String billTermsDesc;
    @ExcelField(title = "应收应付", dictType = "BMS_RECEIVABLE_PAYABLE")
    private String receivablePayable;
    @ExcelField(title = "计费标准值")
    private BigDecimal billStandard;
    @ExcelField(title = "计费量")
    private BigDecimal billQty;
    @ExcelField(title = "发生量")
    private BigDecimal occurrenceQty;
    @ExcelField(title = "费用")
    private BigDecimal cost;
    // 结算日期从
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date dateFm;
    // 结算日期到
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date dateTo;
    // 机构ID
    private String orgId;

    public BmsBillStatistics() {
        super();
    }

    public BmsBillStatistics(String id) {
        super(id);
    }

    @Deprecated
    public String getBillNo() {
        return billNo;
    }

    @Deprecated
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo;
    }

    public String getSettleModelCode() {
        return settleModelCode;
    }

    public void setSettleModelCode(String settleModelCode) {
        this.settleModelCode = settleModelCode;
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

    public String getSettleMethod() {
        return settleMethod;
    }

    public void setSettleMethod(String settleMethod) {
        this.settleMethod = settleMethod;
    }

    public String getSettleCategory() {
        return settleCategory;
    }

    public void setSettleCategory(String settleCategory) {
        this.settleCategory = settleCategory;
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

    public String getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(String receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public BigDecimal getBillStandard() {
        return billStandard;
    }

    public void setBillStandard(BigDecimal billStandard) {
        this.billStandard = billStandard;
    }

    public BigDecimal getBillQty() {
        return billQty;
    }

    public void setBillQty(BigDecimal billQty) {
        this.billQty = billQty;
    }

    public BigDecimal getOccurrenceQty() {
        return occurrenceQty;
    }

    public void setOccurrenceQty(BigDecimal occurrenceQty) {
        this.occurrenceQty = occurrenceQty;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getDateFm() {
        return dateFm;
    }

    public void setDateFm(Date dateFm) {
        this.dateFm = dateFm;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @ExcelField(title = "日期范围")
    public String getDateRange() {
        if (this.dateFm != null && this.dateTo != null) {
            return DateFormatUtil.formatDate("yyyyMMdd", this.dateFm) + " ~ " + DateFormatUtil.formatDate("yyyyMMdd", this.dateTo);
        }
        return "";
    }

    @Override
    @ExcelField(title = "备注")
    public String getRemarks() {
        return super.getRemarks();
    }
}