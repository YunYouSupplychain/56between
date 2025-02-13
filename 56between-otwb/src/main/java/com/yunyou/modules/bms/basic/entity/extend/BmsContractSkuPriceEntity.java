package com.yunyou.modules.bms.basic.entity.extend;

import com.yunyou.modules.bms.basic.entity.BmsContractSkuPrice;

public class BmsContractSkuPriceEntity extends BmsContractSkuPrice {
    private static final long serialVersionUID = 4776161386777070760L;

    // 结算对象编码
    private String settleObjectCode;
    // 结算对象名称
    private String settleObjectName;
    private String skuClassName;
    private String orgName;

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    public String getSkuClassName() {
        return skuClassName;
    }

    public void setSkuClassName(String skuClassName) {
        this.skuClassName = skuClassName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
