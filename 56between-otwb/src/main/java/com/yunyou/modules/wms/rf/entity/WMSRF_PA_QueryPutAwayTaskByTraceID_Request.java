/**
 * 
 */
package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WMSRF_PA_QueryPutAwayTaskByTraceID_Request implements Serializable {
	// 用户Id
	private String userId;
	// 托盘号
	private String toId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
}
