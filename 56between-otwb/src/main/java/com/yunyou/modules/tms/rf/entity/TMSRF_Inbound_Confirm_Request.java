package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Inbound_Confirm_Request implements Serializable {
	// 备件入库单号
	private String sparePoNo;
	// 备件编码
	private String fittingCode;
	// 备件条码
	private String barcode;

	public String getSparePoNo() {
		return sparePoNo;
	}

	public void setSparePoNo(String sparePoNo) {
		this.sparePoNo = sparePoNo;
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
