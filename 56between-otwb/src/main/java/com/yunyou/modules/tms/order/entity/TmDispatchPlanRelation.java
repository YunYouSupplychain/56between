package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 调度计划需求计划关系Entity
 * @author WMJ
 * @version 2020-07-07
 */
public class TmDispatchPlanRelation extends DataEntity<TmDispatchPlanRelation> {

	private static final long serialVersionUID = 1L;
	// 调度计划Id
	private String dispatchPlanId;
	// 需求计划Id
	private String demandPlanId;

	public TmDispatchPlanRelation(String dispatchPlanId, String demandPlanId) {
		this.dispatchPlanId = dispatchPlanId;
		this.demandPlanId = demandPlanId;
	}

	public String getDispatchPlanId() {
		return dispatchPlanId;
	}

	public void setDispatchPlanId(String dispatchPlanId) {
		this.dispatchPlanId = dispatchPlanId;
	}

	public String getDemandPlanId() {
		return demandPlanId;
	}

	public void setDemandPlanId(String demandPlanId) {
		this.demandPlanId = demandPlanId;
	}
}