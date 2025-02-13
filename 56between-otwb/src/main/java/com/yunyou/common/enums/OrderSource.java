package com.yunyou.common.enums;

/**
 * 订单来源枚举
 *
 * @author zyf
 * @version 2021-06-07
 */
public enum OrderSource {
    /**
     * 接口订单 INT
     */
    INT("INT", "接口订单"),
    /**
     * 手工订单 IMP
     */
    IMP("IMP", "手工订单");

    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    OrderSource(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
