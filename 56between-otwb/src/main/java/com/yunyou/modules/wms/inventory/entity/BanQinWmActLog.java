package com.yunyou.modules.wms.inventory.entity;

import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 库存交易日志Entity
 * @author WMJ
 * @version 2020-04-14
 */
public class BanQinWmActLog extends DataEntity<BanQinWmActLog> {

	private static final long serialVersionUID = 1L;
	private String tranType;				// 交易类型
	private String orderType;				// 单据类型
	private String orderNo;					// 单据号
	private String lineNo;					// 行号
	private Date opTime;					// 操作时间
	private String zoneCode;				// 库区编码
	private String ownerCode;				// 货主编码
    private String skuCode;					// 商品编码
	private String lotNum;					// 批次号
	private Double qtyEaOp;					// 操作数量EA（增量）
	private Double qtyEaBefore;				// 操作前 库存数
	private Double qtyEaAfter;				// 操作后 库存数
	private String orgId;					// 分公司

	public BanQinWmActLog() {
		super();
		this.recVer = 0;
	}

	public BanQinWmActLog(String id){
		super(id);
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
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

	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	public Double getQtyEaOp() {
		return qtyEaOp;
	}

	public void setQtyEaOp(Double qtyEaOp) {
		this.qtyEaOp = qtyEaOp;
	}

	public Double getQtyEaBefore() {
		return qtyEaBefore;
	}

	public void setQtyEaBefore(Double qtyEaBefore) {
		this.qtyEaBefore = qtyEaBefore;
	}

	public Double getQtyEaAfter() {
		return qtyEaAfter;
	}

	public void setQtyEaAfter(Double qtyEaAfter) {
		this.qtyEaAfter = qtyEaAfter;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}