package com.yunyou.common.enums;

import com.yunyou.common.exception.GlobalException;

import java.util.Arrays;

/**
 * 客户类型枚举
 *
 * @author WMJ
 * @version 2019-05-30
 */
public enum CustomerType {
    /**
     * 收货方 CONSIGNEE
     */
    CONSIGNEE("CONSIGNEE", "收货方"),
    /**
     * 发货方 SHIPPER
     */
    SHIPPER("SHIPPER", "发货方"),
    /**
     * 委托方 CUSTOMER
     */
    CUSTOMER("CUSTOMER", "委托方"),
    /**
     * 通知方 NOTIFY
     */
    NOTIFY("NOTIFY", "通知方"),
    /**
     * 结算方 SETTLEMENT
     */
    SETTLEMENT("SETTLEMENT", "结算方"),
    /**
     * 货主 OWNER
     */
    OWNER("OWNER", "货主"),
    /**
     * 供应商 SUPPLIER
     */
    SUPPLIER("SUPPLIER", "供应商"),
    /**
     * 承运商 CARRIER
     */
    CARRIER("CARRIER", "承运商"),
    /**
     * 网点 OUTLET
     */
    OUTLET("OUTLET", "网点"),
    /**
     * 垫资方 ADVANCE
     */
    ADVANCE("ADVANCE", "垫资方"),
    /**
     * 档口 STALLS
     */
    STALLS("STALLS", "档口");

    private final String code;
    private final String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return desc;
    }

    public static CustomerType value(String code) {
        return Arrays.stream(CustomerType.values()).filter(o -> o.getCode().equals(code)).findFirst().orElseThrow(() -> new GlobalException("无效的客户类型[" + code + "]"));
    }

    CustomerType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}