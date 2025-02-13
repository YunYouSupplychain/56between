package com.yunyou.modules.oms.order.entity;

import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 供应链作业任务明细表Entity
 *
 * @author WMJ
 * @version 2019-04-21
 */
public class OmTaskDetail extends DataEntity<OmTaskDetail> {
    private static final long serialVersionUID = 1L;

    private String headerId;// 单头Id 父类
    private String skuCode;// 商品编码
    private String skuName;// 商品名称
    private String spec;// 规格
    private String unit;// 单位
    private BigDecimal qty;// 数量
    private BigDecimal price;// 单价
    private BigDecimal amount;// 金额
    private String allocStatus;// 分配状态
    private String chainNo;// 供应链订单号
    private String lineNo;// 供应链订单行号
    private String customerNo;// 客户订单号
    private String dataSource;// 数据来源
    private String orgId;// 机构ID

    private String auxiliaryUnit;// 辅助单位
    private BigDecimal auxiliaryQty;// 辅助单位数量
    private Double taxRate;// 税率
    private BigDecimal taxPrice;// 含税单价
    private BigDecimal taxMoney;// 税金
    private BigDecimal taxAmount;// 含税金额
    private BigDecimal discount;// 折扣
    private BigDecimal turnover;// 成交金额
    private BigDecimal transactionTax;// 成交税金
    private BigDecimal sumTransactionPriceTax;// 成交价税合计
    private String itemPriceId;// 商品价格表ID
    private BigDecimal riceNum;// 米数

    private String def1;       // 温层
    private String def2;       // 课别
    private String def3;       // 品类
    private String def4;
    private String def5;
    private String def6;
    private String def7;
    private String def8;
    private String def9;
    private String def10;

    public OmTaskDetail() {
        super();
    }

    public OmTaskDetail(String id) {
        super(id);
    }

    public OmTaskDetail(String id, String headerId) {
        super(id);
        this.headerId = headerId;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAllocStatus() {
        return allocStatus;
    }

    public void setAllocStatus(String allocStatus) {
        this.allocStatus = allocStatus;
    }

    public String getChainNo() {
        return chainNo;
    }

    public void setChainNo(String chainNo) {
        this.chainNo = chainNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAuxiliaryUnit() {
        return auxiliaryUnit;
    }

    public void setAuxiliaryUnit(String auxiliaryUnit) {
        this.auxiliaryUnit = auxiliaryUnit;
    }

    public BigDecimal getAuxiliaryQty() {
        return auxiliaryQty;
    }

    public void setAuxiliaryQty(BigDecimal auxiliaryQty) {
        this.auxiliaryQty = auxiliaryQty;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public BigDecimal getTaxMoney() {
        return taxMoney;
    }

    public void setTaxMoney(BigDecimal taxMoney) {
        this.taxMoney = taxMoney;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public BigDecimal getTransactionTax() {
        return transactionTax;
    }

    public void setTransactionTax(BigDecimal transactionTax) {
        this.transactionTax = transactionTax;
    }

    public BigDecimal getSumTransactionPriceTax() {
        return sumTransactionPriceTax;
    }

    public void setSumTransactionPriceTax(BigDecimal sumTransactionPriceTax) {
        this.sumTransactionPriceTax = sumTransactionPriceTax;
    }

    public String getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(String itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public BigDecimal getRiceNum() {
        return riceNum;
    }

    public void setRiceNum(BigDecimal riceNum) {
        this.riceNum = riceNum;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef10() {
        return def10;
    }

    public void setDef10(String def10) {
        this.def10 = def10;
    }
}