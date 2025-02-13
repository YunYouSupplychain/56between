package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;

/**
 * 保价模型（保价服务必填）
 */
public class InsuredAnnexDo implements Serializable {
    private static final long serialVersionUID = 1718952508207337057L;
    // 保价金额，单位：元（保价服务时必填，精确到小数点后两位）
    private String insuredValue;
    // 物品价值，单位：元（保价服务时必填，精确到小数点后两位）
    private String goodsValue;

    public String getInsuredValue() {
        return insuredValue;
    }

    public void setInsuredValue(String insuredValue) {
        this.insuredValue = insuredValue;
    }

    public String getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(String goodsValue) {
        this.goodsValue = goodsValue;
    }
}
