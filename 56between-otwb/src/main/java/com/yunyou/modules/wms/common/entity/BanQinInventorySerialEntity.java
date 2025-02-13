package com.yunyou.modules.wms.common.entity;

import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerial;

/**
 * 序列号库存entity
 * @author WMJ
 * @version 2019-01-28
 */
public class BanQinInventorySerialEntity extends BanQinWmInvSerial {
	// 交易类型
	private String serialTranType;
	// 单据类型
	private String orderType;
	// 单据号
	private String orderNo;
	// 行号
	private String lineNo;
	// 交易ID
	private String tranId;

	public String getSerialTranType() {
		return serialTranType;
	}

	public void setSerialTranType(String serialTranType) {
		this.serialTranType = serialTranType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

}
