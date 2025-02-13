package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_InOutbound_Confirm_Response implements Serializable {
	// 计划数量
	private Double orderQty;
	// 扫描数量
	private Double scanQty;

	public Double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}

	public Double getScanQty() {
		return scanQty;
	}

	public void setScanQty(Double scanQty) {
		this.scanQty = scanQty;
	}
}
