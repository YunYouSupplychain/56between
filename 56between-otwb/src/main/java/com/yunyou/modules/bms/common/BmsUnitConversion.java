package com.yunyou.modules.bms.common;

import com.yunyou.modules.bms.basic.entity.SettlementSku;

import java.math.BigDecimal;

/**
 * 描述：包装数量换算
 *
 * @author Jianhua
 * @version 2019/6/19
 */
public class BmsUnitConversion {

    public final static String EA = "EA";// 件
    public final static String IP = "IP";// 内包装
    public final static String CS = "CS";// 箱
    public final static String PL = "PL";// 托
    public final static String OT = "OT";// 大包装

    public static double convert(String originalUnit, double originalQty, String targetUnit, SettlementSku settlementSku) {
        if (settlementSku == null) {
            return originalQty;
        }
        Double originalQuantity = 1D, targetQuantity = 1D;
        if (EA.equals(originalUnit)) {
            originalQuantity = settlementSku.getEaQuantity();
        } else if (IP.equals(originalUnit)) {
            originalQuantity = settlementSku.getIpQuantity();
        } else if (CS.equals(originalUnit)) {
            originalQuantity = settlementSku.getCsQuantity();
        } else if (PL.equals(originalUnit)) {
            originalQuantity = settlementSku.getPlQuantity();
        } else if (OT.equals(originalUnit)) {
            originalQuantity = settlementSku.getOtQuantity();
        }
        if (EA.equals(targetUnit)) {
            targetQuantity = settlementSku.getEaQuantity();
        } else if (IP.equals(targetUnit)) {
            targetQuantity = settlementSku.getIpQuantity();
        } else if (CS.equals(targetUnit)) {
            targetQuantity = settlementSku.getCsQuantity();
        } else if (PL.equals(targetUnit)) {
            targetQuantity = settlementSku.getPlQuantity();
        } else if (OT.equals(targetUnit)) {
            targetQuantity = settlementSku.getOtQuantity();
        }
        originalQuantity = (originalQuantity == null || originalQuantity == 0D) ? 1D : originalQuantity;
        targetQuantity = (targetQuantity == null || targetQuantity == 0D) ? 1D : targetQuantity;
        return BigDecimal.valueOf(originalQty).multiply(BigDecimal.valueOf(originalQuantity)).divide(BigDecimal.valueOf(targetQuantity), 0, BigDecimal.ROUND_UP).doubleValue();
    }
}
