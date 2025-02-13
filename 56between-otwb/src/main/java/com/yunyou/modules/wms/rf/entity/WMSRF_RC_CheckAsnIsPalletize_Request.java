package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WMSRF_RC_CheckAsnIsPalletize_Request implements Serializable {
	// 用户Id
	private String userId;
	// ASN单号
	private String asnNo;
	// 功能类型
	private int funType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getFunType() {
		return funType;
	}

	public void setFunType(int funType) {
		this.funType = funType;
	}

	public String getAsnNo() {
		return asnNo;
	}

	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
	}
}
