package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmDriver;

public class TmDriverEntity extends TmDriver {
    private static final long serialVersionUID = 9030771991843648912L;

    private String carrierName; // 承运商名称
    private String accountName; // 账号名称
    private String allowDriverCarTypeName;// 准驾车型

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

    public String getAllowDriverCarTypeName() {
        return allowDriverCarTypeName;
    }

    public void setAllowDriverCarTypeName(String allowDriverCarTypeName) {
        this.allowDriverCarTypeName = allowDriverCarTypeName;
    }
}
