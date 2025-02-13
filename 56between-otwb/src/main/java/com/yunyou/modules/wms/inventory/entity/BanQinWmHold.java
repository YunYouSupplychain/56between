package com.yunyou.modules.wms.inventory.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 库存冻结Entity
 * @author WMJ
 * @version 2019-01-26
 */
public class BanQinWmHold extends DataEntity<BanQinWmHold> {
	
	private static final long serialVersionUID = 1L;
	private String holdId;		// 冻结ID
	private String holdType;		// 冻结方式
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 批次号
	private String locCode;		// 库位编码
	private String traceId;		// 跟踪号
	private String reasonCode;		// 原因编码
	private String reason;		// 原因描述
	private String isAllowMv;		// 是否可移动
	private String isAllowAd;		// 是否可调整
	private String isAllowTf;		// 是否可转移
	private String holdOp;		// 冻结人
	private Date holdTime;		// 冻结时间
	private String orgId;		// 分公司
	
	public BanQinWmHold() {
		super();
		this.recVer = 0;
	}

	public BanQinWmHold(String id){
		super(id);
	}

	@ExcelField(title="冻结ID", align=2, sort=2)
	public String getHoldId() {
		return holdId;
	}

	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}
	
	@ExcelField(title="冻结方式", align=2, sort=3)
	public String getHoldType() {
		return holdType;
	}

	public void setHoldType(String holdType) {
		this.holdType = holdType;
	}
	
	@ExcelField(title="货主编码", align=2, sort=4)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=5)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=6)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="库位编码", align=2, sort=7)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=8)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@ExcelField(title="原因编码", align=2, sort=9)
	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	
	@ExcelField(title="原因描述", align=2, sort=10)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@ExcelField(title="是否可移动", align=2, sort=11)
	public String getIsAllowMv() {
		return isAllowMv;
	}

	public void setIsAllowMv(String isAllowMv) {
		this.isAllowMv = isAllowMv;
	}
	
	@ExcelField(title="是否可调整", align=2, sort=12)
	public String getIsAllowAd() {
		return isAllowAd;
	}

	public void setIsAllowAd(String isAllowAd) {
		this.isAllowAd = isAllowAd;
	}
	
	@ExcelField(title="是否可转移", align=2, sort=13)
	public String getIsAllowTf() {
		return isAllowTf;
	}

	public void setIsAllowTf(String isAllowTf) {
		this.isAllowTf = isAllowTf;
	}
	
	@ExcelField(title="冻结人", align=2, sort=14)
	public String getHoldOp() {
		return holdOp;
	}

	public void setHoldOp(String holdOp) {
		this.holdOp = holdOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="冻结时间", align=2, sort=15)
	public Date getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(Date holdTime) {
		this.holdTime = holdTime;
	}

	@ExcelField(title="分公司", align=2, sort=23)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}