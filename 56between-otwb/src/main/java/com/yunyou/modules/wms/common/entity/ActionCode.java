package com.yunyou.modules.wms.common.entity;

/**
 * 操作代码映射类，用于标识业务操作代码
 */
public enum ActionCode {
	/**
	 * 安排库位
	 */
	PLAN_LOCATION("PLAN_LOCATION"),
	/**
	 * 取消安排库位
	 */
	CANCEL_PLAN_LOCATION("CANCEL_PLAN_LOCATION"),
	/**
	 * 一步预约收货
	 */
	ONESTEP_PLAN_RECEIVING("ONESTEP_PLAN_RECEIVING"),
	/**
	 * 预约收货
	 */
	PLAN_RECEIVING("PLAN_RECEIVING"),
	/**
	 * 扫描序列号收货
	 */
	SCAN_RECEIVING("SCAN_RECEIVING"),
	/**
	 * 收货
	 */
	RECEIVING("RECEIVING"),
	/**
	 * 取消收货
	 */
	CANCEL_RECEIVING("CANCEL_RECEIVING"),
	/**
	 * 生成上架任务
	 */
	CREATE_PA_TASK("CREATE_PA_TASK"),
	/**
	 * 取消上架任务
	 */
	CANCEL_PA_TASK("CANCEL_PA_TASK"),
	/**
	 * 上架
	 */
	PUTAWAY("PUTAWAY"),
	/**
	 * 库存移动
	 */
	MOVING("MOVING"),
	/**
	 * 库存移动按托盘
	 */
	MOVING_BY_TRACEID("MOVING_BY_TRACEID"),
	/**
	 * 正调整
	 */
	ADJUSTMENT_PROFIT("ADJUSTMENT_PROFIT"),
	/**
	 * 负调整
	 */
	ADJUSTMENT_LOSS("ADJUSTMENT_LOSS"),
	/**
	 * 转移
	 */
	TRANSFER("TRANSFER"),
	/**
	 * 质检
	 */
	QC("QC"),
	/**
	 * 冻结
	 */
	HOLD("HOLD"),
	/**
	 * 取消冻结
	 */
	CANCEL_HOLD("CANCEL_HOLD"),
	/**
	 * 补货
	 */
	REPLENISHMENT("REPLENISHMENT"),
	/**
	 * 生成补货任务
	 */
	CREATE_RP_TASK("CREATE_RP_TASK"),
	/**
	 * 取消补货任务
	 */
	CANCEL_RP_TASK("CANCEL_RP_TASK"),
	/**
	 * 预配
	 */
	PREALLOCATION("PREALLOCATION"),
	/**
	 * 取消预配
	 */
	CANCEL_PREALLOCATION("CANCEL_PREALLOCATION"),
	/**
	 * 分配
	 */
	ALLOCATION("ALLOCATION"),
	/**
	 * 超量分配
	 */
	OVER_ALLOCATION("OVER_ALLOCATION"),
	/**
	 * 一步超量分配
	 */
	ONESTEP_OVER_ALLOCATION("ONESTEP_OVER_ALLOCATION"),
	/**
	 * 一步分配
	 */
	ONESTEP_ALLOCATION("ONESTEP_ALLOCATION"),
	/**
	 * 取消分配
	 */
	CANCEL_ALLOCATION("CANCEL_ALLOCATION"),
	/**
	 * 拣货
	 */
	PICKING("PICKING"),
	/**
	 * 取消拣货
	 */
	CANCEL_PICKING("CANCEL_PICKING"),
	/**
	 * 发货
	 */
	SHIPMENT("SHIPMENT"),
	/**
	 * 取消发货
	 */
	CANCEL_SHIPMENT("CANCEL_SHIPMENT"),
	/**
	 * 复核打包
	 */
	PACKING("PACKING"),
	/**
	 * 装载移动
	 */
	LOADING("LOADING"),
	/**
	 * 加工父件入库
	 */
	KIN("KIN"),
	/**
	 * 取消加工父件入库
	 */
	CANCEL_KIN("CANCEL_KIN"),
	/**
	 * 加工子件出库
	 */
	KOUT("KOUT"),
	/**
	 * 取消加工子件出库
	 */
	CANCEL_KOUT("CANCEL_KOUT"),
	/**
	 * 加工转移
	 */
	KTF("KTF"),
	/**
	 * 取消加工转移
	 */
	CANCEL_KTF("CANCEL_KTF"),

	/**
	 * 取消动作
	 */
	CANCEL("CANCEL");

	private String code;

	public String getCode() {
		return this.code;
	}

	ActionCode(String code) {
		this.code = code;
	}
}
