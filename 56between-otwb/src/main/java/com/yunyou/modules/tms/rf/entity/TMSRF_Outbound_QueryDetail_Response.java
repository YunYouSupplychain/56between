package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Outbound_QueryDetail_Response implements Serializable {
	// 主键
	private String id;
	// 备件出库单号
	private String spareSoNo;
	// 行号
	private String lineNo;
	// 备件编码
	private String fittingCode;
	// 备件名称
	private String fittingName;
	// 规格型号
	private String fittingModel;
	// 出库数量
	private Double soQty;
	// 扫描数量
	private Double scanQty;
	// 供应商编码
	private String supplierCode;
	// 供应商名称
	private String supplierName;
	// 单价
	private Double price;
	// 机构
	private String orgId;
	// 版本号
	private Integer recVer;
	// 组织中心
	private String baseOrgId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpareSoNo() {
		return spareSoNo;
	}

	public void setSpareSoNo(String spareSoNo) {
		this.spareSoNo = spareSoNo;
	}

	public Double getSoQty() {
		return soQty;
	}

	public void setSoQty(Double soQty) {
		this.soQty = soQty;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
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

	public String getFittingModel() {
		return fittingModel;
	}

	public void setFittingModel(String fittingModel) {
		this.fittingModel = fittingModel;
	}

	public Double getScanQty() {
		return scanQty;
	}

	public void setScanQty(Double scanQty) {
		this.scanQty = scanQty;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getRecVer() {
		return recVer;
	}

	public void setRecVer(Integer recVer) {
		this.recVer = recVer;
	}

	public String getBaseOrgId() {
		return baseOrgId;
	}

	public void setBaseOrgId(String baseOrgId) {
		this.baseOrgId = baseOrgId;
	}
}
