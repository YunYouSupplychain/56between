package com.yunyou.modules.bms.finance.entity.extend;

import java.math.BigDecimal;

/**
 * 计算实体
 */
public class BmsCalculateEntity {

    /**
     * 计费量
     */
    private BigDecimal qty;
    /**
     * 合同单价
     */
    private BigDecimal contractPrice;
    /**
     * 商品含税单价
     */
    private BigDecimal skuTaxPrice;
    /**
     * 商品未税单价
     */
    private BigDecimal skuExcludeTaxPrice;
    /**
     * 物流点数
     */
    private BigDecimal logisticsPoints;
    /**
     * 计算结果值
     */
    private BigDecimal result;
    /**
     * 计算过程数据描述
     */
    private StringBuilder calcProcessDataDesc = new StringBuilder();
    /**
     * 计算缺失数据描述
     */
    private StringBuilder missingDataDesc = new StringBuilder();

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    public BigDecimal getSkuTaxPrice() {
        return skuTaxPrice;
    }

    public void setSkuTaxPrice(BigDecimal skuTaxPrice) {
        this.skuTaxPrice = skuTaxPrice;
    }

    public BigDecimal getSkuExcludeTaxPrice() {
        return skuExcludeTaxPrice;
    }

    public void setSkuExcludeTaxPrice(BigDecimal skuExcludeTaxPrice) {
        this.skuExcludeTaxPrice = skuExcludeTaxPrice;
    }

    public BigDecimal getLogisticsPoints() {
        return logisticsPoints;
    }

    public void setLogisticsPoints(BigDecimal logisticsPoints) {
        this.logisticsPoints = logisticsPoints;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public BigDecimal getPrice() {
        if (contractPrice != null) {
            return contractPrice;
        }
        if (skuTaxPrice != null) {
            return skuTaxPrice;
        }
        if (skuExcludeTaxPrice != null) {
            return skuExcludeTaxPrice;
        }
        if (logisticsPoints != null) {
            return logisticsPoints;
        }
        return null;
    }

    public BmsCalculateEntity appendProcessDataDesc(String dataDesc) {
        calcProcessDataDesc.append(dataDesc);
        return this;
    }

    public String getCalcProcessDataDesc() {
        return calcProcessDataDesc.toString();
    }

    public BmsCalculateEntity appendMissingDataDesc(String dataDesc) {
        missingDataDesc.append(dataDesc);
        return this;
    }

    public String getMissingDataDesc() {
        return missingDataDesc.toString();
    }
}
