package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WMSRF_PK_SavePick_Request implements Serializable {
	// 用户Id
	private String userId;
	// 主键
	private String id;
	// 分配ID
	private String allocId;
	// 出库单号
	private String soNo;
	// 拣货单号
	private String pickNo;
	// 行号
	private String lineNo;
	// 拣货数量
	private Double qtyPkEa;
	// 拣货包装数量
	private Double qtyPkUom;
	// 货主名称
	private String ownerName;
	// 商品名称
	private String skuName;
	// 包装规格
	private String packDesc;
	// 包装单位描述
	private String uomDesc;
	// 修改人中文名称
	private String modifierName;
	// 拣货人名称
	private String pickOpName;
	// 复核人名称
	private String checkOpName;
	// 版本号
	private Integer recVer;
	// 标签
	private String toId;
	// 确认库位
	private String toLoc;
	// 打包跟踪号
	private String traceId;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getId() {
		return id;
	}

	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}

	public String getPickNo() {
		return pickNo;
	}

	public void setPickNo(String pickNo) {
		this.pickNo = pickNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public Double getQtyPkEa() {
		return qtyPkEa;
	}

	public void setQtyPkEa(Double qtyPkEa) {
		this.qtyPkEa = qtyPkEa;
	}

	public Double getQtyPkUom() {
		return qtyPkUom;
	}

	public void setQtyPkUom(Double qtyPkUom) {
		this.qtyPkUom = qtyPkUom;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getPackDesc() {
		return packDesc;
	}

	public void setPackDesc(String packDesc) {
		this.packDesc = packDesc;
	}

	public String getUomDesc() {
		return uomDesc;
	}

	public void setUomDesc(String uomDesc) {
		this.uomDesc = uomDesc;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public String getPickOpName() {
		return pickOpName;
	}

	public void setPickOpName(String pickOpName) {
		this.pickOpName = pickOpName;
	}

	public String getCheckOpName() {
		return checkOpName;
	}

	public void setCheckOpName(String checkOpName) {
		this.checkOpName = checkOpName;
	}

	public Integer getRecVer() {
		return recVer;
	}

	public void setRecVer(Integer recVer) {
		this.recVer = recVer;
	}

	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setId(String id) {
		this.id = id;
	}
}
