package com.yunyou.modules.tms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMSRF_Inbound_QueryDetail_Response implements Serializable {
	// 主键
	private String id;
	// 备件入库单号
	private String sparePoNo;
	// 行号
	private String lineNo;
	// 备件编码
	private String fittingCode;
	// 备件名称
	private String fittingName;
	// 规格型号
	private String fittingModel;
	// 采购数量
	private Double poQty;
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

	public String getSparePoNo() {
		return sparePoNo;
	}

	public void setSparePoNo(String sparePoNo) {
		this.sparePoNo = sparePoNo;
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

	public Double getPoQty() {
		return poQty;
	}

	public void setPoQty(Double poQty) {
		this.poQty = poQty;
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
