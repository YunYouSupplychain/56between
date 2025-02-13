package com.yunyou.modules.tms.applet.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmDriver;

public class TmDriverEntity extends TmDriver {
    private static final long serialVersionUID = 9030771991843648912L;

    /*多字段匹配查询*/
    private String codeAndName;

    private String carrierName; // 承运商名称

    private String accountName; // 账号名称

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

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
