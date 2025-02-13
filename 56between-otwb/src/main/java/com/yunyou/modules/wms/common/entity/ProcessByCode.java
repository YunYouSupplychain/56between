package com.yunyou.modules.wms.common.entity;
/**
 * 操作粒度代码映射类，用于标识业务操作粒度代码
 * @author WMJ
 * @version 2019/03/04
 */
public enum ProcessByCode {

	/***************************************************************************
	 * SO出货单 * ***************************************
	 */

	/**
	 * 按目标跟踪号
	 */
	BY_TOID("BY_TOID"),
	/**
	 * 按预配明细
	 */
	BY_PREALLOC("BY_PREALLOC"),
	/**
	 * 按分配明细
	 */
	BY_ALLOC("BY_ALLOC"),
	/**
	 * 按SO单商品行
	 */
	BY_SO_LINE("BY_SO_LINE"),
	/**
	 * 按SO单
	 */
	BY_SO("BY_SO"),
	/**
	 * 按波次
	 */
	BY_WAVE("BY_WAVE"),

	/***************************************************************************
	 * KIT加工单 * ***************************************
	 */

	/**
	 * 按加工子件
	 */
	BY_KIT_SUB("BY_KIT_SUB"),
	/**
	 * 按加工任务
	 */
	BY_TASK_KIT("BY_TASK_KIT");

	private String code;

	public String getCode() {
		return this.code;
	}

	private ProcessByCode(String code) {
		this.code = code;
	}
}
