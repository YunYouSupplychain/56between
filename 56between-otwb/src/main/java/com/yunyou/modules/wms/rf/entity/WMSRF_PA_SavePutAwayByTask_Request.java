/**
 * 
 */
package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;
import java.util.Date;

public class WMSRF_PA_SavePutAwayByTask_Request implements Serializable {
	private static final long serialVersionUID = -5543511194804223115L;
	// 用户Id
	private String userId;
	// 主键
	private String id;
	// 上架任务ID
	private String paId;
	// 上架任务行号
	private String lineNo;
	// 货主编码
	private String ownerName;
	// 商品编码
	private String skuName;
	// 目标库位编码
	private String toLoc;
	// 目标跟踪号
	private String toId;
	// 包装描述
	private String packDesc;
	// 包装单位
	private String uomDesc;
	// 当前上架EA数
	private Double currentPaQtyEa;
	// 上架人员
	private String paName;
	// 上架时间
	private Date paTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaId() {
		return paId;
	}

	public void setPaId(String paId) {
		this.paId = paId;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
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

	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
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

	public Double getCurrentPaQtyEa() {
		return currentPaQtyEa;
	}

	public void setCurrentPaQtyEa(Double currentPaQtyEa) {
		this.currentPaQtyEa = currentPaQtyEa;
	}

	public String getPaName() {
		return paName;
	}

	public void setPaName(String paName) {
		this.paName = paName;
	}

	public Date getPaTime() {
		return paTime;
	}

	public void setPaTime(Date paTime) {
		this.paTime = paTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
