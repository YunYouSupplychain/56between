package com.yunyou.modules.bms.finance.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.util.Date;

public class BmsBillDetailImport implements Serializable {
    private static final long serialVersionUID = -7078048834540590810L;
    @ExcelField(title = "结算对象编码", align = 2)
    private String settleObjectCode;    // 结算对象代码
    @ExcelField(title = "仓库编码", align = 2)
    private String warehouseCode;       // 仓库编码
    @ExcelField(title = "业务日期", align = 2)
    private Date businessDate;          // 业务日期
    @ExcelField(title = "系统合同编号", align = 2)
    private String sysContractNo;       // 系统合同编号
    @ExcelField(title = "费用科目代码", align = 2)
    private String billSubjectCode;     // 费用科目编码
    @ExcelField(title = "应收应付", align = 2)
    private String receivablePayable;   // 应收应付
    @ExcelField(title = "发生量", align = 2)
    private Double occurrenceQty;       // 发生量
    @ExcelField(title = "计费量", align = 2)
    private Double billQty;             // 计费量
    @ExcelField(title = "计费标准", align = 2)
    private Double billStandard;        // 计费标准
    @ExcelField(title = "费用", align = 2)
    private Double cost;                // 费用
    @ExcelField(title = "系统订单号", align = 2)
    private String sysOrderNo;          // 系统订单号
    @ExcelField(title = "客户订单号", align = 2)
    private String customerOrderNo;     // 客户订单号
    @ExcelField(title = "备注", align = 2)
    private String remarks;             // 备注

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getSysContractNo() {
        return sysContractNo;
    }

    public void setSysContractNo(String sysContractNo) {
        this.sysContractNo = sysContractNo;
    }

    public String getBillSubjectCode() {
        return billSubjectCode;
    }

    public void setBillSubjectCode(String billSubjectCode) {
        this.billSubjectCode = billSubjectCode;
    }

    public String getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(String receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public Double getOccurrenceQty() {
        return occurrenceQty;
    }

    public void setOccurrenceQty(Double occurrenceQty) {
        this.occurrenceQty = occurrenceQty;
    }

    public Double getBillQty() {
        return billQty;
    }

    public void setBillQty(Double billQty) {
        this.billQty = billQty;
    }

    public Double getBillStandard() {
        return billStandard;
    }

    public void setBillStandard(Double billStandard) {
        this.billStandard = billStandard;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getSysOrderNo() {
        return sysOrderNo;
    }

    public void setSysOrderNo(String sysOrderNo) {
        this.sysOrderNo = sysOrderNo;
    }

    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
