package com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.request;

public class Cargo {

    private String name;                    // *货物名称
    private Double count;                   // 货物数量
    private String unit;                    // 货物单位
    private String weight;                  // 订单货物单位重量
    private String amount;                  // 货物单价
    private String currency;                // 货物单价的币别
    private String sourceArea;              // 原产地国别
    private String productRecordNumber;     // 货物产品国检备案编号
    private String goodPrepardNumber;       // 商品海关备案号
    private String taxNumber;               // 商品行邮税号
    private String hsCode;                  // 海关编码

    public Cargo() {
    }

    public Cargo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSourceArea() {
        return sourceArea;
    }

    public void setSourceArea(String sourceArea) {
        this.sourceArea = sourceArea;
    }

    public String getProductRecordNumber() {
        return productRecordNumber;
    }

    public void setProductRecordNumber(String productRecordNumber) {
        this.productRecordNumber = productRecordNumber;
    }

    public String getGoodPrepardNumber() {
        return goodPrepardNumber;
    }

    public void setGoodPrepardNumber(String goodPrepardNumber) {
        this.goodPrepardNumber = goodPrepardNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }
}
