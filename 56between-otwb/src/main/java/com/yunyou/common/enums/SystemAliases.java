package com.yunyou.common.enums;

import com.yunyou.common.exception.GlobalException;

import java.util.Arrays;

/**
 * 系统类型
 *
 * @author WMJ
 * @version 2019-05-31
 */
public enum SystemAliases {
    /**
     * 接口管理平台 IMS
     */
    IMS("IMS", "接口管理平台"),
    /**
     * 接口管理平台 OMS
     */
    OMS("OMS", "订单管理系统"),
    /**
     * 仓储管理系统 WMS
     */
    WMS("WMS", "仓储管理系统"),
    /**
     * 运输管理系统 TMS
     */
    TMS("TMS", "运输管理系统"),
    /**
     * 计费管理系统 BMS
     */
    BMS("BMS", "计费管理系统");

    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SystemAliases value(String code) {
        return Arrays.stream(SystemAliases.values()).filter(o -> o.getCode().equals(code)).findFirst().orElseThrow(() -> new GlobalException("无效的系统类型[" + code + "]"));
    }

    SystemAliases(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
