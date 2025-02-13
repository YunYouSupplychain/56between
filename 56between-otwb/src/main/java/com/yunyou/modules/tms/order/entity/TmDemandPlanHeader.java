package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 需求计划单头Entity
 * @author WMJ
 * @version 2020-07-07
 */
public class TmDemandPlanHeader extends DataEntity<TmDemandPlanHeader> {

	private static final long serialVersionUID = 1L;
	// 需求计划单号
	private String planOrderNo;
	// 订单时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orderTime;
	// 客户编码
	private String ownerCode;
	// 到达时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date arrivalTime;
	// 机构ID
	private String orgId;
	// 基础数据机构ID
	private String baseOrgId;
	// 状态
	private String status;

	public TmDemandPlanHeader() {
		super();
	}

	public TmDemandPlanHeader(String id){
		super(id);
	}

	public String getPlanOrderNo() {
		return planOrderNo;
	}

	public void setPlanOrderNo(String planOrderNo) {
		this.planOrderNo = planOrderNo;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getBaseOrgId() {
		return baseOrgId;
	}

	public void setBaseOrgId(String baseOrgId) {
		this.baseOrgId = baseOrgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}