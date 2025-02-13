package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysOmsCustomer;

/**
 * 描述：
 */
public class SysOmsCustomerEntity extends SysOmsCustomer {
    private static final long serialVersionUID = 2057800863926299621L;
    private String clerkName; // 业务员名称
    private String ebcuEbplCountryName;        // 国家
    private String ebcuEbplProvinceName;        // 省
    private String ebcuEbplCityName;        // 市

    public SysOmsCustomerEntity() {
    }

    public SysOmsCustomerEntity(String id) {
        super(id);
    }

    public SysOmsCustomerEntity(String id, String dataSet) {
        super(id, dataSet);
    }

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
