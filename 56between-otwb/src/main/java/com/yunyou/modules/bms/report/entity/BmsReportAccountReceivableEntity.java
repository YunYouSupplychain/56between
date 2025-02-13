package com.yunyou.modules.bms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：应收账款实体
 *
 * @author liujianhua created on 2019-12-9
 */
public class BmsReportAccountReceivableEntity extends DataEntity<BmsReportAccountReceivableEntity> {
    private static final long serialVersionUID = 2892801225997695052L;

    private String billNo;
    // 状态
    private String status;
    // 开票对象编码
    private String invoiceObjectCode;
    // 开票对象名称
    private String invoiceObjectName;
    // 开户行
    private String bank;
    // 银行账户
    private String bankAccount;
    // 仓库编码
    private String warehouseCode;
    // 仓库名称
    private String warehouseName;
    // 结算对象编码
    private String settleObjectCode;
    // 结算对象名称
    private String settleObjectName;
    // 计费科目
    private String billSubjectName;
    // 税率
    private Double taxRate;
    // 计费标准
    private BigDecimal billStandard;
    // 发生量
    private BigDecimal occurrenceQty;
    // 计费量
    private BigDecimal billQty;
    // 费用
    private BigDecimal cost;
    // 创建人
    private String creator;
    // 确认人
    private String auditor;
    // 结算日期从
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date settleDateFm;
    // 结算日期到
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date settleDateTo;
    // 快行天下,
    private String kxtxCode;
    private String kxtxName;
    private String kxtxBank;
    private String kxtxBankAccount;
    private String kxtxRemarks;
    // 机构ID
    private String orgId;
    // 费用备注
    private String costRemarks;

    @ExcelField(title = "费用单号", type = 1, sort = 0)
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @ExcelField(title = "结算周期", type = 1, sort = 1)
    public String getSettleCycle() {
        if (this.settleDateFm != null && this.settleDateTo != null) {
            return DateFormatUtil.formatDate("yyyy.MM.dd", this.settleDateFm)
                    + "-" + DateFormatUtil.formatDate("MM.dd", this.settleDateTo);
        }
        return "";
    }

    @ExcelField(title = "开票对象编码", type = 1, sort = 2)
    public String getInvoiceObjectCode() {
        return invoiceObjectCode;
    }

    public void setInvoiceObjectCode(String invoiceObjectCode) {
        this.invoiceObjectCode = invoiceObjectCode;
    }

    @ExcelField(title = "开票对象名称", type = 1, sort = 3)
    public String getInvoiceObjectName() {
        return invoiceObjectName;
    }

    public void setInvoiceObjectName(String invoiceObjectName) {
        this.invoiceObjectName = invoiceObjectName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @ExcelField(title = "仓别", type = 1, sort = 4)
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

    @ExcelField(title = "结算对象编码", type = 1, sort = 5)
    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    @ExcelField(title = "结算对象名称", type = 1, sort = 6)
    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    @ExcelField(title = "计费科目", type = 1, sort = 7)
    public String getBillSubjectName() {
        return billSubjectName;
    }

    public void setBillSubjectName(String billSubjectName) {
        this.billSubjectName = billSubjectName;
    }

    @ExcelField(title = "税率", type = 1, sort = 8)
    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    @ExcelField(title = "计费标准", type = 1, sort = 9)
    public BigDecimal getBillStandard() {
        return billStandard;
    }

    public void setBillStandard(BigDecimal billStandard) {
        this.billStandard = billStandard;
    }

    @ExcelField(title = "发生量", type = 1, sort = 10)
    public BigDecimal getOccurrenceQty() {
        return occurrenceQty;
    }

    public void setOccurrenceQty(BigDecimal occurrenceQty) {
        this.occurrenceQty = occurrenceQty;
    }

    @ExcelField(title = "计费量", type = 1, sort = 11)
    public BigDecimal getBillQty() {
        return billQty;
    }

    public void setBillQty(BigDecimal billQty) {
        this.billQty = billQty;
    }

    @ExcelField(title = "费用", type = 1, sort = 12)
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @ExcelField(title = "计算人", type = 1, sort = 14)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @ExcelField(title = "确认人", type = 1, sort = 15)
    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getKxtxCode() {
        return kxtxCode;
    }

    public void setKxtxCode(String kxtxCode) {
        this.kxtxCode = kxtxCode;
    }

    public String getKxtxName() {
        return kxtxName;
    }

    public void setKxtxName(String kxtxName) {
        this.kxtxName = kxtxName;
    }

    public String getKxtxBank() {
        return kxtxBank;
    }

    public void setKxtxBank(String kxtxBank) {
        this.kxtxBank = kxtxBank;
    }

    public String getKxtxBankAccount() {
        return kxtxBankAccount;
    }

    public void setKxtxBankAccount(String kxtxBankAccount) {
        this.kxtxBankAccount = kxtxBankAccount;
    }

    public String getKxtxRemarks() {
        return kxtxRemarks;
    }

    public void setKxtxRemarks(String kxtxRemarks) {
        this.kxtxRemarks = kxtxRemarks;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCostRemarks() {
        return costRemarks;
    }

    public void setCostRemarks(String costRemarks) {
        this.costRemarks = costRemarks;
    }
}
