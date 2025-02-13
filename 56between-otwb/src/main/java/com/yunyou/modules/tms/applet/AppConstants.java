package com.yunyou.modules.tms.applet;

public class AppConstants {

    public static final String LOGIN_TYPE_NAME = "NAME";    // 用户名密码
    public static final String LOGIN_TYPE_WX = "WX";        // 微信

    // 出车安全检查-确认结论
    public static final String VEHICLE_SAFETY_CHECK_STATUS_0 = "0";     // 良好 可以行驶
    public static final String VEHICLE_SAFETY_CHECK_STATUS_1 = "1";     // 须修复 检查合格后行驶
    public static final String VEHICLE_SAFETY_CHECK_STATUS_2 = "2";     // 不符合 禁止出车

    // 车辆检查项状态
    public static final String VEHICLE_CHECK_ITEM_STATUS_NORMAL = "0";// 合格
    public static final String VEHICLE_CHECK_ITEM_STATUS_WARN = "1";// 警告
    public static final String VEHICLE_CHECK_ITEM_STATUS_ERROR = "2";// 异常

    // 车辆维修状态
    public static final String VEHICLE_REPAIR_STATUS_NORMAL = "0";// 正常
    public static final String VEHICLE_REPAIR_STATUS_REPAIR = "1";// 维修中
}
