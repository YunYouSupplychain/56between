package com.yunyou.modules.wms.basicdata.entity;

/**
 * 库存周转规则Entity
 * @author WMJ
 * @version 2019-04-12
 */
public class BanQinCdRuleRotationDetailEntity extends BanQinCdRuleRotationDetail {
	private String lotAttName;

    public String getLotAttName() {
        return lotAttName;
    }

    public void setLotAttName(String lotAttName) {
        this.lotAttName = lotAttName;
    }
}