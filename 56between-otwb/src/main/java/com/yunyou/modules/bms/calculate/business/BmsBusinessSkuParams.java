package com.yunyou.modules.bms.calculate.business;

import java.math.BigDecimal;

/**
 * 计算商品相关量的业务参数
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsBusinessSkuParams {

    /**
     * 货主编码
     */
    private final String ownerCode;
    /**
     * 商品编码
     */
    private final String skuCode;
    /**
     * 品类
     */
    private final String skuClass;
    /**
     * 数量
     */
    private final BigDecimal qty;
    /**
     * 箱数量
     */
    private final BigDecimal csQty;
    /**
     * 托数量
     */
    private final BigDecimal plQty;
    /**
     * 理论箱数量
     */
    private final BigDecimal theoryCsQty;
    /**
     * 理论托数量
     */
    private final BigDecimal theoryPlQty;
    /**
     * 重量
     */
    private final BigDecimal weight;
    /**
     * 体积
     */
    private final BigDecimal volume;

    public BmsBusinessSkuParams(String ownerCode, String skuCode, String skuClass,
                                BigDecimal qty, BigDecimal csQty, BigDecimal plQty,
                                BigDecimal theoryCsQty, BigDecimal theoryPlQty,
                                BigDecimal weight, BigDecimal volume) {
        this.ownerCode = ownerCode;
        this.skuCode = skuCode;
        this.skuClass = skuClass;
        this.qty = qty;
        this.csQty = csQty;
        this.plQty = plQty;
        this.theoryCsQty = theoryCsQty;
        this.theoryPlQty = theoryPlQty;
        this.weight = weight;
        this.volume = volume;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public String getSkuClass() {
        return skuClass;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public BigDecimal getCsQty() {
        return csQty;
    }

    public BigDecimal getPlQty() {
        return plQty;
    }

    public BigDecimal getTheoryCsQty() {
        return theoryCsQty;
    }

    public BigDecimal getTheoryPlQty() {
        return theoryPlQty;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public BigDecimal getVolume() {
        return volume;
    }
}
