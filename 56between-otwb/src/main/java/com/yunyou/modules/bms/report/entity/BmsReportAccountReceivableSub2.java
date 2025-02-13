package com.yunyou.modules.bms.report.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 描述：应收账款结算单
 *
 * @author liujianhua created on 2019-12-10
 */
public class BmsReportAccountReceivableSub2 implements Serializable {

    private static final long serialVersionUID = 761423424106705803L;
    // 序号
    private String lineNo;
    // 税率
    private Double taxRate;
    // 含税金额
    private BigDecimal amountIncludeTax;
    // 未税金额
    private BigDecimal amount;
    // 税额
    private BigDecimal taxAmount;

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getAmountIncludeTax() {
        return amountIncludeTax;
    }

    public void setAmountIncludeTax(BigDecimal amountIncludeTax) {
        this.amountIncludeTax = amountIncludeTax;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}
