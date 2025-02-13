package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Repair_ScanSpareBarcode_Request implements Serializable {
	// 备件条码
	private String spareBarCode;
	// 维修工单号
	private String repairNo;

	public String getSpareBarCode() {
		return spareBarCode;
	}

	public void setSpareBarCode(String spareBarCode) {
		this.spareBarCode = spareBarCode;
	}

	public String getRepairNo() {
		return repairNo;
	}

	public void setRepairNo(String repairNo) {
		this.repairNo = repairNo;
	}
}
