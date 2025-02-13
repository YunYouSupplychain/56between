package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WMSRF_SHIP_SaveShipment_Request implements Serializable {
	// 用户Id
	private String userId;
	// 箱号
	private String toId;
	// 出库单号
	private String soNo;
	// 面单号
	private String trackingNo;

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
}
