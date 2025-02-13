package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Repair_ScanSpareBarcode_Response implements Serializable {
	// 备件编码
	private String fittingCode;
	// 备件名称
	private String fittingName;
	// 规格型号
	private String fittingModel;
	// 供应商编码
	private String supplierCode;
	// 供应商名称
	private String supplierName;

	public String getFittingCode() {
		return fittingCode;
	}

	public void setFittingCode(String fittingCode) {
		this.fittingCode = fittingCode;
	}

	public String getFittingName() {
		return fittingName;
	}

	public void setFittingName(String fittingName) {
		this.fittingName = fittingName;
	}

	public String getFittingModel() {
		return fittingModel;
	}

	public void setFittingModel(String fittingModel) {
		this.fittingModel = fittingModel;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
