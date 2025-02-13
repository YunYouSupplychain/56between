package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 调度计划明细Entity
 * @author WMJ
 * @version 2020-07-07
 */
public class TmDispatchPlanDetail extends DataEntity<TmDispatchPlanDetail> {

	private static final long serialVersionUID = 1L;
	// 调度计划号
	private String planNo;
	// 客户编码
	private String ownerCode;
	// 送达时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date arrivalTime;
	// 商品编码
	private String skuCode;
	// 计划数量
	private BigDecimal planQty;
	// 机构ID
	private String orgId;
	// 基础数据机构ID
	private String baseOrgId;
	// 是否配载
	private String isDispatch;

	public TmDispatchPlanDetail() {
		super();
	}

	public TmDispatchPlanDetail(String id){
		super(id);
	}

	public String getPlanNo() {
		return planNo;
	}

	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public BigDecimal getPlanQty() {
		return planQty;
	}

	public void setPlanQty(BigDecimal planQty) {
		this.planQty = planQty;
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

	public String getIsDispatch() {
		return isDispatch;
	}

	public void setIsDispatch(String isDispatch) {
		this.isDispatch = isDispatch;
	}
}