package com.yunyou.modules.wms.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 补货任务Entity
 * @author WMJ
 * @version 2019-06-25
 */
public class BanQinWmTaskRpEntity extends BanQinWmTaskRp {
	private String ownerName;
	private String skuName;
	private String packDesc;
	private String cdprUnitLevel;
	private Double cdprQuantity;
	private String cdprDesc;
	private Double qty;
	private Integer qtyUse;
	private Double qtyHold;
	private Double qtyAlloc;
	private Double qtyPk;
	private Double qtyPaOut;
	private Double qtyRpOut;
	private Double qtyMvOut;
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
	private Date rpTimeFm;
	private Date rpTimeTo;
	private String orgName;

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

	public String getCdprUnitLevel() {
		return cdprUnitLevel;
	}

	public void setCdprUnitLevel(String cdprUnitLevel) {
		this.cdprUnitLevel = cdprUnitLevel;
	}

	public Double getCdprQuantity() {
		return cdprQuantity;
	}

	public void setCdprQuantity(Double cdprQuantity) {
		this.cdprQuantity = cdprQuantity;
	}

	public String getCdprDesc() {
		return cdprDesc;
	}

	public void setCdprDesc(String cdprDesc) {
		this.cdprDesc = cdprDesc;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Integer getQtyUse() {
		return qtyUse;
	}

	public void setQtyUse(Integer qtyUse) {
		this.qtyUse = qtyUse;
	}

	public Double getQtyHold() {
		return qtyHold;
	}

	public void setQtyHold(Double qtyHold) {
		this.qtyHold = qtyHold;
	}

	public Double getQtyAlloc() {
		return qtyAlloc;
	}

	public void setQtyAlloc(Double qtyAlloc) {
		this.qtyAlloc = qtyAlloc;
	}

	public Double getQtyPk() {
		return qtyPk;
	}

	public void setQtyPk(Double qtyPk) {
		this.qtyPk = qtyPk;
	}

	public Double getQtyPaOut() {
		return qtyPaOut;
	}

	public void setQtyPaOut(Double qtyPaOut) {
		this.qtyPaOut = qtyPaOut;
	}

	public Double getQtyRpOut() {
		return qtyRpOut;
	}

	public void setQtyRpOut(Double qtyRpOut) {
		this.qtyRpOut = qtyRpOut;
	}

	public Double getQtyMvOut() {
		return qtyMvOut;
	}

	public void setQtyMvOut(Double qtyMvOut) {
		this.qtyMvOut = qtyMvOut;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getLotAtt01() {
		return lotAtt01;
	}

	public void setLotAtt01(Date lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
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

	public Date getRpTimeFm() {
		return rpTimeFm;
	}

	public void setRpTimeFm(Date rpTimeFm) {
		this.rpTimeFm = rpTimeFm;
	}

	public Date getRpTimeTo() {
		return rpTimeTo;
	}

	public void setRpTimeTo(Date rpTimeTo) {
		this.rpTimeTo = rpTimeTo;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}