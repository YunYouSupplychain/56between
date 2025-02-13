package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Repair_Accept_Response implements Serializable {
	// 主键
	private String id;
	// 备注
	private String remarks;
	// 维修单号
	private String repairNo;
	// 报修时间
	private String orderTime;
	// 状态
	private String status;
	// 车牌号
	private String carNo;
	// 司机（报修人）
	private String driver;
	// 需维修项
	private String needRepairItem;
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

	public String getRepairNo() {
		return repairNo;
	}

	public void setRepairNo(String repairNo) {
		this.repairNo = repairNo;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getNeedRepairItem() {
		return needRepairItem;
	}

	public void setNeedRepairItem(String needRepairItem) {
		this.needRepairItem = needRepairItem;
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
