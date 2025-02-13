package com.yunyou.modules.bms.report.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 描述：应收账款结算单
 *
 * @author liujianhua created on 2019-12-10
 */
public class BmsReportAccountReceivableSub1 implements Serializable {

    private static final long serialVersionUID = 7741346620209785427L;
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
    private BigDecimal billStandard = BigDecimal.ZERO;
    // 发生量
    private BigDecimal occurrenceQty = BigDecimal.ZERO;
    // 计费量
    private BigDecimal billQty = BigDecimal.ZERO;
    // 费用
    private BigDecimal cost = BigDecimal.ZERO;
    // 创建人
    private String creator;
    // 确认人
    private String auditor;
    // 费用备注
    private String costRemarks;

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

    public String getBillSubjectName() {
        return billSubjectName;
    }

    public void setBillSubjectName(String billSubjectName) {
        this.billSubjectName = billSubjectName;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getBillStandard() {
        return billStandard;
    }

    public void setBillStandard(BigDecimal billStandard) {
        this.billStandard = billStandard;
    }

    public BigDecimal getOccurrenceQty() {
        return occurrenceQty;
    }

    public void setOccurrenceQty(BigDecimal occurrenceQty) {
        this.occurrenceQty = occurrenceQty;
    }

    public BigDecimal getBillQty() {
        return billQty;
    }

    public void setBillQty(BigDecimal billQty) {
        this.billQty = billQty;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getCostRemarks() {
        return costRemarks;
    }

    public void setCostRemarks(String costRemarks) {
        this.costRemarks = costRemarks;
    }
}
