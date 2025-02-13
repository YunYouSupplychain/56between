package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务库存Entity
 * @author WMJ
 * @version 2020-04-26
 */
public class BanQinWmBusinessInv extends DataEntity<BanQinWmBusinessInv> {

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
	private String firstOwner;				// 第一货主
	private String toOwner;					// 货转后货主
	private Date bgDate;					// 期初日期
	private Integer storeDays;				// 存储天数
	private String lot;						// 结算批次
	private BigDecimal qtyIn;				// 入库数量
	private BigDecimal qtyOut;				// 出库数量
	private BigDecimal weightIn;			// 入库重量
	private BigDecimal weightOut;			// 出库重量
	private BigDecimal weightInv;			// 库存重量
	private Integer seq;					// 序号

	public BanQinWmBusinessInv() {
		super();
		this.recVer = 0;
	}

	public BanQinWmBusinessInv(String id){
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getFirstOwner() {
		return firstOwner;
	}

	public void setFirstOwner(String firstOwner) {
		this.firstOwner = firstOwner;
	}

	public String getToOwner() {
		return toOwner;
	}

	public void setToOwner(String toOwner) {
		this.toOwner = toOwner;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBgDate() {
		return bgDate;
	}

	public void setBgDate(Date bgDate) {
		this.bgDate = bgDate;
	}

	public Integer getStoreDays() {
		return storeDays;
	}

	public void setStoreDays(Integer storeDays) {
		this.storeDays = storeDays;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public BigDecimal getQtyIn() {
		return qtyIn;
	}

	public void setQtyIn(BigDecimal qtyIn) {
		this.qtyIn = qtyIn;
	}

	public BigDecimal getQtyOut() {
		return qtyOut;
	}

	public void setQtyOut(BigDecimal qtyOut) {
		this.qtyOut = qtyOut;
	}

	public BigDecimal getWeightIn() {
		return weightIn;
	}

	public void setWeightIn(BigDecimal weightIn) {
		this.weightIn = weightIn;
	}

	public BigDecimal getWeightOut() {
		return weightOut;
	}

	public void setWeightOut(BigDecimal weightOut) {
		this.weightOut = weightOut;
	}

	public BigDecimal getWeightInv() {
		return weightInv;
	}

	public void setWeightInv(BigDecimal weightInv) {
		this.weightInv = weightInv;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}