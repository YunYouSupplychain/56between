package com.yunyou.modules.oms.common;

import java.math.BigDecimal;

public class OmsUtils {

    /**
     * 描述：计算辅助单位数量
     * <p>
     * create by Jianhua on 2019/10/9
     */
    public static BigDecimal calcAuxiliaryQty(BigDecimal ratio, BigDecimal taskQty) {
        ratio = ratio == null ? BigDecimal.valueOf(1) : ratio;
        return ratio.multiply(taskQty).setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    /*计算价含税金额*/
    public static BigDecimal calcTaxAmount(BigDecimal taxPrice, BigDecimal taskQty) {
        taxPrice = taxPrice == null ? BigDecimal.ZERO : taxPrice;
        return taxPrice.multiply(taskQty).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /*计算税额*/
    public static BigDecimal calcTaxMoney(BigDecimal taxPrice, BigDecimal taskQty, BigDecimal taxRate) {
        taxPrice = taxPrice == null ? BigDecimal.ZERO : taxPrice;
        return taxPrice.multiply(taskQty.multiply(taxRate)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /*计算金额*/
    public static BigDecimal calcAmount(BigDecimal taxPrice, BigDecimal taskQty, BigDecimal taxRate) {
        taxPrice = taxPrice == null ? BigDecimal.ZERO : taxPrice;
        return taxPrice.multiply(taskQty).divide(BigDecimal.ONE.add(taxRate), 2, BigDecimal.ROUND_HALF_UP);
    }

    /*计算价税合计*/
    public static BigDecimal calcSumTransactionPriceTax(BigDecimal taxPrice, BigDecimal taskQty, BigDecimal discount) {
        discount = discount == null ? BigDecimal.ZERO : discount;
        BigDecimal taxAmount = calcTaxAmount(taxPrice, taskQty);
        return taxAmount.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /*计算成交税金*/
    public static BigDecimal calcTransactionTax(BigDecimal taxPrice, BigDecimal taskQty, BigDecimal taxRate, BigDecimal discount) {
        discount = discount == null ? BigDecimal.ZERO : discount;
        BigDecimal taxMoney = calcTaxMoney(taxPrice, taskQty, taxRate);
        return taxMoney.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /*计算成交金额*/
    public static BigDecimal calcTurnover(BigDecimal taxPrice, BigDecimal taskQty, BigDecimal taxRate, BigDecimal discount) {
        discount = discount == null ? BigDecimal.ZERO : discount;
        BigDecimal amount = calcAmount(taxPrice, taskQty, taxRate);
        return amount.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
