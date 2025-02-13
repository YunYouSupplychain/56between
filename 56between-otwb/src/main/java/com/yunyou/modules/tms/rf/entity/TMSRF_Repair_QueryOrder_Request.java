package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Repair_QueryOrder_Request implements Serializable {
	// 车牌号
	private String vehicleNo;
	// 维修工单号
	private String repairNo;

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getRepairNo() {
		return repairNo;
	}

	public void setRepairNo(String repairNo) {
		this.repairNo = repairNo;
	}
}
