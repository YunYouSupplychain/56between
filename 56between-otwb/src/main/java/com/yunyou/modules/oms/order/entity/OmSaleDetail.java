package com.yunyou.modules.oms.order.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import org.apache.commons.lang3.StringEscapeUtils;

import java.math.BigDecimal;

/**
 * 销售订单明细表Entity
 *
 * @author WMJ
 * @version 2019-04-17
 */
public class OmSaleDetail extends DataEntity<OmSaleDetail> {

    private static final long serialVersionUID = 1L;
    private String headerId;        // 单头Id 父类
    private String skuCode;        // 商品编码
    private String skuName;        // 商品名称
    private String spec;        // 规格
    private String auxiliaryUnit;        // 辅助单位
    private BigDecimal auxiliaryQty;        // 数量
    private String unit;        // 单位
    private BigDecimal qty;        // 数量
    private Double taxRate;        // 税率
    private BigDecimal price;        // 单价
    private BigDecimal taxPrice;        // 含税单价
    private BigDecimal amount;        // 金额
    private BigDecimal taxMoney;        // 税金
    private BigDecimal taxAmount;        // 含税金额
    private BigDecimal discount;        // 折扣
    private BigDecimal turnover;        // 成交金额
    private BigDecimal transactionTax;        // 成交税金
    private BigDecimal sumTransactionPriceTax;        // 成交价税合计
    private String orgId;           // 平台编码
    private BigDecimal saleMultiple;    // 销售倍数
    private BigDecimal ratio;       // 换算比例
    private String middleId;    // 中间表对应ID
    private String itemPriceId;     // 商品价格表ID
    private BigDecimal riceNum;     // 米数
    private String isAllowAdjustment;		// 是否允许调整价格（价格表带出）
    private String logisticsMuqType; // 物流管理单位数量种类(辅助单位数量/单位数量)
    private String logisticsUnit;   // 物流单位
    private BigDecimal logisticsUnitQty; // 物流单位数量

    public OmSaleDetail() {
        super();
    }

    public OmSaleDetail(String id) {
        super(id);
    }

    public OmSaleDetail(String id, String headerId) {
        super(id);
        this.headerId = headerId;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    @ExcelField(title = "商品编码", align = 2, sort = 8)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "商品名称", align = 2, sort = 9)
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @ExcelField(title = "规格", align = 2, sort = 10)
    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = StringEscapeUtils.unescapeHtml4(spec);// 解决HTML字符被转义问题
    }

    @ExcelField(title = "辅助单位", align = 2, sort = 11)
    public String getAuxiliaryUnit() {
        return auxiliaryUnit;
    }

    public void setAuxiliaryUnit(String auxiliaryUnit) {
        this.auxiliaryUnit = auxiliaryUnit;
    }

    @ExcelField(title = "数量", align = 2, sort = 12)
    public BigDecimal getAuxiliaryQty() {
        return auxiliaryQty;
    }

    public void setAuxiliaryQty(BigDecimal auxiliaryQty) {
        this.auxiliaryQty = auxiliaryQty;
    }

    @ExcelField(title = "单位", align = 2, sort = 13)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @ExcelField(title = "数量", align = 2, sort = 14)
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @ExcelField(title = "税率", align = 2, sort = 15)
    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    @ExcelField(title = "单价", align = 2, sort = 16)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ExcelField(title = "含税单价", align = 2, sort = 17)
    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    @ExcelField(title = "金额", align = 2, sort = 18)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @ExcelField(title = "税金", align = 2, sort = 19)
    public BigDecimal getTaxMoney() {
        return taxMoney;
    }

    public void setTaxMoney(BigDecimal taxMoney) {
        this.taxMoney = taxMoney;
    }

    @ExcelField(title = "含税金额", align = 2, sort = 20)
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    @ExcelField(title = "折扣", align = 2, sort = 21)
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @ExcelField(title = "成交金额", align = 2, sort = 22)
    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    @ExcelField(title = "成交税金", align = 2, sort = 23)
    public BigDecimal getTransactionTax() {
        return transactionTax;
    }

    public void setTransactionTax(BigDecimal transactionTax) {
        this.transactionTax = transactionTax;
    }

    @ExcelField(title = "成交价税合计", align = 2, sort = 24)
    public BigDecimal getSumTransactionPriceTax() {
        return sumTransactionPriceTax;
    }

    public void setSumTransactionPriceTax(BigDecimal sumTransactionPriceTax) {
        this.sumTransactionPriceTax = sumTransactionPriceTax;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public BigDecimal getSaleMultiple() {
        return saleMultiple;
    }

    public void setSaleMultiple(BigDecimal saleMultiple) {
        this.saleMultiple = saleMultiple;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public String getMiddleId() {
        return middleId;
    }

    public void setMiddleId(String middleId) {
        this.middleId = middleId;
    }

    public String getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(String itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public String getIsAllowAdjustment() {
        return isAllowAdjustment;
    }

    public void setIsAllowAdjustment(String isAllowAdjustment) {
        this.isAllowAdjustment = isAllowAdjustment;
    }

    public BigDecimal getRiceNum() {
        return riceNum;
    }

    public void setRiceNum(BigDecimal riceNum) {
        this.riceNum = riceNum;
    }

    public String getLogisticsMuqType() {
        return logisticsMuqType;
    }

    public void setLogisticsMuqType(String logisticsMuqType) {
        this.logisticsMuqType = logisticsMuqType;
    }

    public String getLogisticsUnit() {
        return logisticsUnit;
    }

    public void setLogisticsUnit(String logisticsUnit) {
        this.logisticsUnit = logisticsUnit;
    }

    public BigDecimal getLogisticsUnitQty() {
        return logisticsUnitQty;
    }

    public void setLogisticsUnitQty(BigDecimal logisticsUnitQty) {
        this.logisticsUnitQty = logisticsUnitQty;
    }

}