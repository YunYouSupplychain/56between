package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WMSRF_LD_QueryLdInfo_Request implements Serializable {
	// 用户Id
	private String userId;
	// 装车单号
	private String ldNo;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLdNo() {
		return ldNo;
	}

	public void setLdNo(String ldNo) {
		this.ldNo = ldNo;
	}
}
