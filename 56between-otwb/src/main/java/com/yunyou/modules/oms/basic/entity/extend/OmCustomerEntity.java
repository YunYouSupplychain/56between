package com.yunyou.modules.oms.basic.entity.extend;

import com.yunyou.modules.oms.basic.entity.OmCustomer;

/**
 * 描述：
 * <p>
 * create by Jianhua on 2019/7/29
 */
public class OmCustomerEntity extends OmCustomer {
    private static final long serialVersionUID = 2057800863926299621L;
    private String clerkName; // 业务员名称
    private String ebcuEbplCountryName;		// 国家
    private String ebcuEbplProvinceName;		// 省
    private String ebcuEbplCityName;		// 市

    private String codeAndName; // 查询条件-编码/名称

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

    public String getEbcuEbplCountryName() {
        return ebcuEbplCountryName;
    }

    public void setEbcuEbplCountryName(String ebcuEbplCountryName) {
        this.ebcuEbplCountryName = ebcuEbplCountryName;
    }

    public String getEbcuEbplProvinceName() {
        return ebcuEbplProvinceName;
    }

    public void setEbcuEbplProvinceName(String ebcuEbplProvinceName) {
        this.ebcuEbplProvinceName = ebcuEbplProvinceName;
    }

    public String getEbcuEbplCityName() {
        return ebcuEbplCityName;
    }

    public void setEbcuEbplCityName(String ebcuEbplCityName) {
        this.ebcuEbplCityName = ebcuEbplCityName;
    }
}
