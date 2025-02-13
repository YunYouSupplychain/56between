package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;

/**
 * 客户信息 测试环境示例（需调度才传月结编号）：{"siteCode":"666666","customerName":"666666000001","sitePwd":"abc123"} ；正式环境找网点或市场部申请
 */
public class CustomerDO implements Serializable {
    private static final long serialVersionUID = 7157082745034539042L;
    // 网点编码必传
    private String siteCode;
    // 客户编码（不传单号时必传）
    private String customerName;
    // 电子面单密码（不传单号时必传）例（需调度才传月结编号）：{"siteCode":"666666","customerName":"666666000001","sitePwd":"abc123"} ；正式环境找网点或市场部申请
    private String sitePwd;
    // 月结客户编码（不传单号需调度才传月结编号）：{"siteCode":"666666","customerName":"666666000001","sitePwd":"abc123"} ；正式环境找网点或市场部申请
    private String monthCustomerCode;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSitePwd() {
        return sitePwd;
    }

    public void setSitePwd(String sitePwd) {
        this.sitePwd = sitePwd;
    }

    public String getMonthCustomerCode() {
        return monthCustomerCode;
    }

    public void setMonthCustomerCode(String monthCustomerCode) {
        this.monthCustomerCode = monthCustomerCode;
    }
}
