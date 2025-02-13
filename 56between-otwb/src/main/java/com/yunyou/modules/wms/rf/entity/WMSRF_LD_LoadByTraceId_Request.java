package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WMSRF_LD_LoadByTraceId_Request implements Serializable {
	// 用户Id
	private String userId;
	// 装车单号
	private String ldNo;
	// 目标跟踪号
	private String traceId;

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

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
}
