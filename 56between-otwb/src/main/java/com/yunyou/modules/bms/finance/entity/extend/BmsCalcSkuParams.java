package com.yunyou.modules.bms.finance.entity.extend;

import java.math.BigDecimal;

/**
 * 计算商品相关量的业务参数
 */
public class BmsCalcSkuParams {

    // 货主编码
    private final String ownerCode;
    // 商品编码
    private final String skuCode;
    // 品类
    private final String skuClass;
    // 结算机构ID
    private final String settleOrgId;
    // 商品数量
    private final BigDecimal qty;
    // 商品箱数量
    private final BigDecimal csQty;
    // 商品托数量
    private final BigDecimal plQty;
    // 商品重量
    private final BigDecimal weight;
    // 商品体积
    private final BigDecimal volume;

    public BmsCalcSkuParams(String ownerCode, String skuCode, String skuClass, String settleOrgId,
                            BigDecimal qty, BigDecimal csQty, BigDecimal plQty, BigDecimal weight, BigDecimal volume) {
        this.ownerCode = ownerCode;
        this.skuCode = skuCode;
        this.skuClass = skuClass;
        this.settleOrgId = settleOrgId;
        this.qty = qty;
        this.csQty = csQty;
        this.plQty = plQty;
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

    public String getSettleOrgId() {
        return settleOrgId;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public BigDecimal getVolume() {
        return volume;
    }
}
