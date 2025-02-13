package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Outbound_Confirm_Request implements Serializable {
	// 备件出库单号
	private String spareSoNo;
	// 备件编码
	private String fittingCode;
	// 备件条码
	private String barcode;

	public String getSpareSoNo() {
		return spareSoNo;
	}

	public void setSpareSoNo(String spareSoNo) {
		this.spareSoNo = spareSoNo;
	}

	public String getFittingCode() {
		return fittingCode;
	}

	public void setFittingCode(String fittingCode) {
		this.fittingCode = fittingCode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
