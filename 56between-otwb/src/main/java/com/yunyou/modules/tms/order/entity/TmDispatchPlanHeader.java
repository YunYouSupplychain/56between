package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 调度计划单头Entity
 * @author WMJ
 * @version 2020-07-07
 */
public class TmDispatchPlanHeader extends DataEntity<TmDispatchPlanHeader> {

	private static final long serialVersionUID = 1L;
	// 调度计划号
	private String planNo;
	// 调度时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dispatchTime;
	// 承运商
	private String carrierCode;
	// 派车网点
	private String dispatchOutletCode;
	// 机构ID
	private String orgId;
	// 基础数据机构ID
	private String baseOrgId;
	// 状态
	private String status;

	public TmDispatchPlanHeader() {
		super();
	}

	public TmDispatchPlanHeader(String id){
		super(id);
	}

	public String getPlanNo() {
		return planNo;
	}

	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}

	public Date getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getDispatchOutletCode() {
		return dispatchOutletCode;
	}

	public void setDispatchOutletCode(String dispatchOutletCode) {
		this.dispatchOutletCode = dispatchOutletCode;
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