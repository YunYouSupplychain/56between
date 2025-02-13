package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WMSRF_RC_SaveAsnDetailByTraceID_Request implements Serializable {
	// 用户Id
	private String userId;
	// 明细主键
	private String id;
	// 货主
	private String ownerCode;
	// 商品名称
	private String skuCode;
	// 库位
	private String toLoc;
	// ASN单号
	private String asnNo;
	// 收货明细行号
	private String lineNo;
	// 上架规则
	private String paRule;
	// 当前收货数
	private Double currentQtyRcvEa;
	// 包装
	private String packCode;
	// 单位
	private String uom;
	// 版本号
	private Integer recVer;
	// 目标托盘
	private String toId;
	// 批次属性1(生产日期)
	private String lotAtt01;
	// 批次属性2(失效日期)
	private String lotAtt02;
	// 批次属性3(入库日期)
	private String lotAtt03;
	// 批次属性4
	private String lotAtt04;
	// 批次属性5
	private String lotAtt05;
	// 批次属性6
	private String lotAtt06;
	// 批次属性7
	private String lotAtt07;
	// 批次属性8
	private String lotAtt08;
	// 批次属性9
	private String lotAtt09;
	// 批次属性10
	private String lotAtt10;
	// 批次属性11
	private String lotAtt11;
	// 批次属性12
	private String lotAtt12;
	// 跟踪号
	private String planId;
	
	public String getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(String lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}

	public String getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(String lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}

	public String getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(String lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}

	public String getLotAtt04() {
		return lotAtt04;
	}

	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}

	public String getLotAtt05() {
		return lotAtt05;
	}

	public void setLotAtt05(String lotAtt05) {
		this.lotAtt05 = lotAtt05;
	}

	public String getLotAtt06() {
		return lotAtt06;
	}

	public void setLotAtt06(String lotAtt06) {
		this.lotAtt06 = lotAtt06;
	}

	public String getLotAtt07() {
		return lotAtt07;
	}

	public void setLotAtt07(String lotAtt07) {
		this.lotAtt07 = lotAtt07;
	}

	public String getLotAtt08() {
		return lotAtt08;
	}

	public void setLotAtt08(String lotAtt08) {
		this.lotAtt08 = lotAtt08;
	}

	public String getLotAtt09() {
		return lotAtt09;
	}

	public void setLotAtt09(String lotAtt09) {
		this.lotAtt09 = lotAtt09;
	}

	public String getLotAtt10() {
		return lotAtt10;
	}

	public void setLotAtt10(String lotAtt10) {
		this.lotAtt10 = lotAtt10;
	}

	public String getLotAtt11() {
		return lotAtt11;
	}

	public void setLotAtt11(String lotAtt11) {
		this.lotAtt11 = lotAtt11;
	}

	public String getLotAtt12() {
		return lotAtt12;
	}

	public void setLotAtt12(String lotAtt12) {
		this.lotAtt12 = lotAtt12;
	}

	public Integer getRecVer() {
		return recVer;
	}

	public void setRecVer(Integer recVer) {
		this.recVer = recVer;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}

	public String getAsnNo() {
		return asnNo;
	}

	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getPaRule() {
		return paRule;
	}

	public void setPaRule(String paRule) {
		this.paRule = paRule;
	}

	public Double getCurrentQtyRcvEa() {
		return currentQtyRcvEa;
	}

	public void setCurrentQtyRcvEa(Double currentQtyRcvEa) {
		this.currentQtyRcvEa = currentQtyRcvEa;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
