package com.yunyou.modules.wms.basicdata.entity;

/**
 * 库存周转规则Entity
 * @author WMJ
 * @version 2019-04-12
 */
public class BanQinCdRuleRotationHeaderEntity extends BanQinCdRuleRotationHeader {
    // 批次属性名称
	private String lotName;
    // 模糊查询字段
    private String ruleCodeAndName;

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getRuleCodeAndName() {
        return ruleCodeAndName;
    }

    public void setRuleCodeAndName(String ruleCodeAndName) {
        this.ruleCodeAndName = ruleCodeAndName;
    }
}