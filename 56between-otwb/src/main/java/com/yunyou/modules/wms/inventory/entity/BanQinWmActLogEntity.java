package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存交易日志Entity
 * @author WMJ
 * @version 2020-04-14
 */
public class BanQinWmActLogEntity extends BanQinWmActLog {
	private String toOwnerCode;
	private String toSkuCode;
	private String toLotNum;
	private Double toQtyEaOp;
	private String status;

	private Date lotAtt01;
	private Date lotAtt02;
	private Date lotAtt03;
	private String lotAtt04;
	private String lotAtt05;
	private String lotAtt06;
	private String lotAtt07;
	private String lotAtt08;
	private String lotAtt09;
	private String lotAtt10;
	private String lotAtt11;
	private String lotAtt12;
	private String firstOwner;
	private String toOwner;
	private Date tfDate;
	private Date adDate;
	private Date inboundDate;
	private Date outboundDate;
	private BigDecimal qtyIn;
	private BigDecimal qtyOut;
	private BigDecimal weightIn;
	private BigDecimal weightOut;
	private BigDecimal qtyInv;
	private BigDecimal weightInv;
	private Integer days;
	private BigDecimal qtySettle;
	private BigDecimal weightSettle;
	private BigDecimal weight2Settle;
	private String ownerName;
	private String zoneName;
	private String skuName;
	private Integer plQty;
	private Integer seq;

	private Date beginTime;
	private Date endTime;
	private Date orderTimeFm;
	private Date orderTimeTo;

	public BanQinWmActLogEntity() {
		super();
	}

	public String getToOwnerCode() {
		return toOwnerCode;
	}

	public void setToOwnerCode(String toOwnerCode) {
		this.toOwnerCode = toOwnerCode;
	}

	public String getToSkuCode() {
		return toSkuCode;
	}

	public void setToSkuCode(String toSkuCode) {
		this.toSkuCode = toSkuCode;
	}

	public String getToLotNum() {
		return toLotNum;
	}

	public void setToLotNum(String toLotNum) {
		this.toLotNum = toLotNum;
	}

	public Double getToQtyEaOp() {
		return toQtyEaOp;
	}

	public void setToQtyEaOp(Double toQtyEaOp) {
		this.toQtyEaOp = toQtyEaOp;
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
	public Date getTfDate() {
		return tfDate;
	}

	public void setTfDate(Date tfDate) {
		this.tfDate = tfDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getAdDate() {
		return adDate;
	}

	public void setAdDate(Date adDate) {
		this.adDate = adDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}

	public Date getLotAtt02() {
		return lotAtt02;
	}

	public void setLotAtt02(Date lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getLotAtt03() {
		return lotAtt03;
	}

	public void setLotAtt03(Date lotAtt03) {
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

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getInboundDate() {
		return inboundDate;
	}

	public void setInboundDate(Date inboundDate) {
		this.inboundDate = inboundDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getOutboundDate() {
		return outboundDate;
	}

	public void setOutboundDate(Date outboundDate) {
		this.outboundDate = outboundDate;
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

	public BigDecimal getQtyInv() {
		return qtyInv;
	}

	public void setQtyInv(BigDecimal qtyInv) {
		this.qtyInv = qtyInv;
	}

	public BigDecimal getWeightInv() {
		return weightInv;
	}

	public void setWeightInv(BigDecimal weightInv) {
		this.weightInv = weightInv;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public BigDecimal getQtySettle() {
		return qtySettle;
	}

	public void setQtySettle(BigDecimal qtySettle) {
		this.qtySettle = qtySettle;
	}

	public BigDecimal getWeightSettle() {
		return weightSettle;
	}

	public void setWeightSettle(BigDecimal weightSettle) {
		this.weightSettle = weightSettle;
	}

	public BigDecimal getWeight2Settle() {
		return weight2Settle;
	}

	public void setWeight2Settle(BigDecimal weight2Settle) {
		this.weight2Settle = weight2Settle;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Integer getPlQty() {
		return plQty;
	}

	public void setPlQty(Integer plQty) {
		this.plQty = plQty;
	}

	public Date getOrderTimeFm() {
		return orderTimeFm;
	}

	public void setOrderTimeFm(Date orderTimeFm) {
		this.orderTimeFm = orderTimeFm;
	}

	public Date getOrderTimeTo() {
		return orderTimeTo;
	}

	public void setOrderTimeTo(Date orderTimeTo) {
		this.orderTimeTo = orderTimeTo;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}