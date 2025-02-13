package com.yunyou.modules.interfaces.gps.e6.constant;

public class ApiPathList {

    /**
     * 获取用户车辆的最新位置信息
     */
    public static final String GET_VEHICLE_INFO = "/public/v3/Inface/Call";
    /**
     * 获取用户车辆的轨迹信息
     */
    public static final String GET_TRACK_DETAIL = "/public/v3/StatisticsReport/Call";

    /**
     * 获取设备实时数据
     */
    public static final String GET_EQUIP_INFO_REAL = "/public/v4/COMMON-MODULE-TERMINAL-WEB/api/getEquipInfoReal";

    /**
     * 获取设备指定时间区间数据
     */
    public static final String GET_EQUIP_INFO_HISTORY = "/public/v4/COMMON-MODULE-TERMINAL-WEB/api/getEquipInfoHistory";
}
