package com.yunyou.modules.tms.common.map;

import com.yunyou.common.config.Global;
import com.yunyou.common.utils.StringUtils;

/**
 * 地图常量类
 */
public class MapConstants {
    /*地图种类*/
    public static final String G_MAP = "GMap";// 高德地图
    public static final String B_MAP = "BMap";// 百度地图
    /*当前使用地图*/
    public static final String USE_MAP = StringUtils.isBlank(Global.getConfig("use.map")) ? G_MAP : Global.getConfig("use.map");
}
