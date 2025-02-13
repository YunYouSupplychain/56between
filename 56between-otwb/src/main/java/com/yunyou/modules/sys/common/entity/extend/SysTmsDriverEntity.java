package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysTmsDriver;

public class SysTmsDriverEntity extends SysTmsDriver {
    private static final long serialVersionUID = 9030771991843648912L;

    private String carrierName; // 承运商名称
    private String accountName; // 账号名称

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
