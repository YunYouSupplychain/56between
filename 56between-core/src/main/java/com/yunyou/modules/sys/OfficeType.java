package com.yunyou.modules.sys;

public enum OfficeType {
    /**
     * 公司
     */
    COMPANY("1"),
    /**
     * 仓库
     */
    WAREHOUSE("4"),
    /**
     * 组织中心
     */
    ORG_CENTER("5"),
    /**
     * 调度中心
     */
    DISPATCH_CENTER("6"),
    /**
     * 网点
     */
    OUTLET("7"),
    /**
     * 承运商
     */
    CARRIER("8");

    private final String value;

    OfficeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
