package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Outbound_QueryOrder_Response implements Serializable {
	// 主键
	private String id;
	// 备注
	private String remarks;
	// 备件出库单号
	private String spareSoNo;
	// 订单时间
	private String orderTime;
	// 订单状态
	private String orderStatus;
	// 订单类型
	private String orderType;
	// 经办人
	private String operator;
	// 机构
	private String orgId;
	// 版本号
	private String recVer;
	// 组织中心
	private String baseOrgId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSpareSoNo() {
		return spareSoNo;
	}

	public void setSpareSoNo(String spareSoNo) {
		this.spareSoNo = spareSoNo;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getRecVer() {
		return recVer;
	}

	public void setRecVer(String recVer) {
		this.recVer = recVer;
	}

	public String getBaseOrgId() {
		return baseOrgId;
	}

	public void setBaseOrgId(String baseOrgId) {
		this.baseOrgId = baseOrgId;
	}
}
