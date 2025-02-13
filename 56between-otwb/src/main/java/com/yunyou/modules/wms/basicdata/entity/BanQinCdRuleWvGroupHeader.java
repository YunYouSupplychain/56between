package com.yunyou.modules.wms.basicdata.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 波次规则组Entity
 * @author WMJ
 * @version 2020-02-09
 */
public class BanQinCdRuleWvGroupHeader extends DataEntity<BanQinCdRuleWvGroupHeader> {
	
	private static final long serialVersionUID = 1L;
	private String groupCode;		// 规则组编码
	private String groupName;		// 规则组名称
	private Date orderDateFm;		// 订单时间从
	private Date orderDateTo;		// 订单时间到
	private String orgId;			// 平台Id
	private String ownerCode;		// 货主编码
	private String skuCode;			// 商品编码
	private String ownerName;		// 货主名称
	private String skuName;			// 商品名称
	private String addrArea;		// 三级地址
	private String ruleCodeAndName; // 模糊查询字段

	public BanQinCdRuleWvGroupHeader() {
		super();
	}

	public BanQinCdRuleWvGroupHeader(String id){
		super(id);
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@JsonFormat(pattern = "HH:mm:ss")
	public Date getOrderDateFm() {
		return orderDateFm;
	}

	public void setOrderDateFm(Date orderDateFm) {
		this.orderDateFm = orderDateFm;
	}
	
	@JsonFormat(pattern = "HH:mm:ss")
	public Date getOrderDateTo() {
		return orderDateTo;
	}

	public void setOrderDateTo(Date orderDateTo) {
		this.orderDateTo = orderDateTo;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public String getRuleCodeAndName() {
		return ruleCodeAndName;
	}

	public void setRuleCodeAndName(String ruleCodeAndName) {
		this.ruleCodeAndName = ruleCodeAndName;
	}

	public String getAddrArea() {
		return addrArea;
	}

	public void setAddrArea(String addrArea) {
		this.addrArea = addrArea;
	}
}