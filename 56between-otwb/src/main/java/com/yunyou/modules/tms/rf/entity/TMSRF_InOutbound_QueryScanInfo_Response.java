package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_InOutbound_QueryScanInfo_Response implements Serializable {
	// 主键
	private String id;
	// 备注
	private String remarks;
	// 备件编码
	private String fittingCode;
	// 备件名称
	private String fittingName;
	// 备件条码
	private String barcode;
	// 供应商编码
	private String supplierCode;
	// 供应商名称
	private String supplierName;
	// 操作人
	private String operator;
	// 操作时间
	private String operateTime;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
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
