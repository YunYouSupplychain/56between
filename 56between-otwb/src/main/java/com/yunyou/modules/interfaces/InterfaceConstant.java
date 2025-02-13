package com.yunyou.modules.interfaces;

import com.yunyou.modules.sys.entity.User;

/**
 * 接口常量类
 * 
 * @author zyf
 * @version 2019-10-09
 */
public class InterfaceConstant {

	public static final String FORMAT_TYPE_XML = "XML";
	public static final String FORMAT_TYPE_JSON = "JSON";

	public static final String SUCCESS_LOG = "成功";

	public static final String STATUS_Y = "Y";
	public static final String STATUS_N = "N";

	// 处理方向
	public static final String HANDLE_DIR_PRE = "P";		// 预
	public static final String HANDLE_DIR_RECEIVE = "R";	// 接收
	public static final String HANDLE_DIR_SEND = "S";		// 发送

	// 百世快递默认用户
	public static final User BEST_USER = new User("800Best");
	// 顺丰快递默认用户
	public static final User SF_EXPRESS_USER = new User("SF-EXPRESS");
	// 中通快递默认用户
	public static final User ZTO_USER = new User("ZTO_USER");
	// 韵达快递默认用户
	public static final User YUNDA_USER = new User("YUNDA_USER");
	// 申通快递默认用户
	public static final User STO_USER = new User("STO_USER");
    // 圆通快递默认用户
    public static final User YTO_USER = new User("YTO_USER");
	// 快递100默认用户
	public static final User KD100_USER = new User("KD100_USER");

	// 百世接口日志类型
	public static final String KD_TRACE_QUERY = "KD_TRACE_QUERY";				// 物流节点查询
	public static final String KD_WAYBILL_QUERY = "KD_WAYBILL_QUERY";			// 电子面单获取
	public static final String KD_INSPECTION_SUBMIT = "KD_INSPECTION_SUBMIT";	// 抽检数据提交
	public static final String KD_TRACE_TIMER = "KD_TRACE_TIMER";				// 物流节点查询定时器

	// 顺丰接口日志类型
	public static final String SF_EXPRESS_CREATE_ORDER = "SF_EXPRESS_CREATE_ORDER";		// 下单接口
	public static final String SF_EXPRESS_ROUTE_QUERY = "SF_EXPRESS_ROUTE_QUERY";		// 路由查询接口

	// 中通接口日志类型
	public static final String ZTO_GET_ORDER = "ZTO_GET_ORDER";		// 获取面单
	public static final String ZTO_GET_DTB = "ZTO_GET_DTB";			// 获取大头笔
	public static final String ZTO_ROUTE_QUERY = "ZTO_ROUTE_QUERY";	// 获取路由

	// 韵达接口日志类型
	public static final String YUNDA_CREATE_ORDER = "YUNDA_CREATE_ORDER";	// 创建订单
	public static final String YUNDA_ROUTE_QUERY = "YUNDA_ROUTE_QUERY";		// 路由查询

	// 申通接口日志类型
	public static final String STO_ORDER_CREATE = "STO_ORDER_CREATE"; // 订单创建
	public static final String STO_TRACE_QUERY = "STO_TRACE_QUERY";	  // 物流轨迹查询

    // 圆通接口日志类型
    public static final String YTO_CREATE_ORDER = "YTO_CREATE_ORDER";	// 创建订单
    public static final String YTO_ROUTE_QUERY = "YTO_ROUTE_QUERY";		// 路由查询

	// 快递100接口日志类型
	public static final String KD100_CREATE_ORDER = "KD100_CREATE_ORDER";	// 创建订单并获取电子面单
	public static final String KD100_MAIL_REPLAY = "KD100_MAIL_REPLAY";		// 面单复打
	public static final String KD100_ROUTE_QUERY = "KD100_ROUTE_QUERY";		// 路由查询
}
