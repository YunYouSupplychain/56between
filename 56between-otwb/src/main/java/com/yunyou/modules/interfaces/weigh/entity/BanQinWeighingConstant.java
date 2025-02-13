package com.yunyou.modules.interfaces.weigh.entity;


import com.yunyou.modules.sys.entity.User;

/**
 * 称重接口常量类
 * 
 * @author zyf
 * @version 2019-10-09
 */
public class BanQinWeighingConstant {

	// 称重接口默认用户
	public static final User WEIGH_USER = new User("WEIGH_IF");
	// 称重更新定时器默认用户
	public static final User WEIGH_TIMER_USER = new User("WEIGH_TIMER");

	public static final String STATUS_Y = "Y";
	public static final String STATUS_N = "N";

	public static final String BQ_WMS_WEIGHING = "BQ_WMS_WEIGHING";

	public static final String HANDLE_DIR_PRE = "P";
	public static final String HANDLE_DIR_RECEIVE = "R";
	public static final String HANDLE_DIR_SEND = "S";

	public static final String ACTION_WEIGH_SAVE = "称重保存";
	public static final String ACTION_AUTO_SHIP = "自动发货";

}
