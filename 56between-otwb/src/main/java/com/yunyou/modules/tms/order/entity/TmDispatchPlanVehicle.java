package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 调度计划车辆Entity
 * @author WMJ
 * @version 2020-07-07
 */
public class TmDispatchPlanVehicle extends DataEntity<TmDispatchPlanVehicle> {

	private static final long serialVersionUID = 1L;
	// 调度计划号
	private String planNo;
	// 车辆编码
	private String vehicleNo;
	// 机构ID
	private String orgId;
	// 基础数据机构Id
	private String baseOrgId;

    public TmDispatchPlanVehicle() {
		super();
	}

	public TmDispatchPlanVehicle(String id){
		super(id);
	}

	public String getPlanNo() {
		return planNo;
	}

	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
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
}