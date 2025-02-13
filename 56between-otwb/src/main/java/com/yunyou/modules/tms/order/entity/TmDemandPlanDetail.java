package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 需求计划明细Entity
 * @author WMJ
 * @version 2020-07-07
 */
public class TmDemandPlanDetail extends DataEntity<TmDemandPlanDetail> {

	private static final long serialVersionUID = 1L;
	// 需求计划单号
	private String planOrderNo;
	// 客户编码
	private String ownerCode;
	// 商品编码
	private String skuCode;
	// 数量
	private BigDecimal qty;
	// 机构ID
	private String orgId;
	// 基础数据机构ID
	private String baseOrgId;

	public TmDemandPlanDetail() {
		super();
	}

	public TmDemandPlanDetail(String id){
		super(id);
	}

	public TmDemandPlanDetail(String planOrderNo, String orgId) {
		this.planOrderNo = planOrderNo;
		this.orgId = orgId;
	}

	public String getPlanOrderNo() {
		return planOrderNo;
	}

	public void setPlanOrderNo(String planOrderNo) {
		this.planOrderNo = planOrderNo;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getBaseOrgId() {
		return baseOrgId;
	}

	public void setBaseOrgId(String baseOrgId) {
		this.baseOrgId = baseOrgId;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
}