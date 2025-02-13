package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WMSRF_RC_QueryAsnDetailByTraceIDOrSku_Request implements Serializable {
	// 用户Id
	private String userId;
	// ASN单号
	private String asnNo;
	// 托盘ID
	private String planId;
	// 商品编码
	private String skuCode;
	// 功能类型：1扫描托盘收货，2扫描商品收货
	private String funType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getAsnNo() {
		return asnNo;
	}

	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getFunType() {
		return funType;
	}

	public void setFunType(String funType) {
		this.funType = funType;
	}
}
