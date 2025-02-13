package com.yunyou.modules.oms.basic.entity.extend;

import com.yunyou.modules.oms.basic.entity.OmItem;

import java.math.BigDecimal;
import java.util.Date;

public class OmItemEntity extends OmItem {
    private String packDesc;// 包装规格
    private Date effectiveTime;// 生效日期
    private Date expirationTime;// 失效日期
    private String customerNo;// 客户编码
    private String channel;// 渠道
    private String priceType;// 价格类型
    private String isAllowAdjustment;// 是否允许调整
    private BigDecimal discount;// 折扣(比例)
    private BigDecimal taxPrice;// 含税单价
    private BigDecimal price;// 单价
    private Double taxRate;// 税率
    private BigDecimal convertRatio;// 换算比例
    private String itemPriceId; // 商品价格ID
    private BigDecimal purchaseMultiple;        // 采购倍数
    private BigDecimal saleMultiple;        // 销售倍数
    private String skuClassName;// 品类名称

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getIsAllowAdjustment() {
        return isAllowAdjustment;
    }

    public void setIsAllowAdjustment(String isAllowAdjustment) {
        this.isAllowAdjustment = isAllowAdjustment;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getConvertRatio() {
        return convertRatio;
    }

    public void setConvertRatio(BigDecimal convertRatio) {
        this.convertRatio = convertRatio;
    }

    public String getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(String itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public BigDecimal getPurchaseMultiple() {
        return purchaseMultiple;
    }

    public void setPurchaseMultiple(BigDecimal purchaseMultiple) {
        this.purchaseMultiple = purchaseMultiple;
    }

    public BigDecimal getSaleMultiple() {
        return saleMultiple;
    }

    public void setSaleMultiple(BigDecimal saleMultiple) {
        this.saleMultiple = saleMultiple;
    }

    public String getSkuClassName() {
        return skuClassName;
    }

    public void setSkuClassName(String skuClassName) {
        this.skuClassName = skuClassName;
    }
}
