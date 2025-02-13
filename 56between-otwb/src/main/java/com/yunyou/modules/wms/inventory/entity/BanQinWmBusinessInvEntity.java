package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务库存Entity
 * @author WMJ
 * @version 2020-04-26
 */
public class BanQinWmBusinessInvEntity extends BanQinWmBusinessInv {
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
	private Date tfDate;
	private Date adDate;
	private Date outboundDate;
	private String ownerName;
	private String zoneName;
	private String skuName;
	private String def1;
	private String def2;
	private String def3;
	private String def4;
	private String def5;
	private String def6;
	private String def7;
	private String def8;
	private String def9;
	private String def10;
	private String def11;
	private String def12;

	private Date orderTimeFm;
	private Date orderTimeTo;
	private Date opTimeFm;
	private Date opTimeTo;

	private BigDecimal totalBgQty;
	private BigDecimal totalInQty;
	private BigDecimal totalOutQty;
	private BigDecimal totalEdQty;

	public BanQinWmBusinessInvEntity() {
		super();
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
	public Date getOutboundDate() {
		return outboundDate;
	}

	public void setOutboundDate(Date outboundDate) {
		this.outboundDate = outboundDate;
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

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}

	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}

	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}

	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}

	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	public String getDef11() {
		return def11;
	}

	public void setDef11(String def11) {
		this.def11 = def11;
	}

	public String getDef12() {
		return def12;
	}

	public void setDef12(String def12) {
		this.def12 = def12;
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

	public Date getOpTimeFm() {
		return opTimeFm;
	}

	public void setOpTimeFm(Date opTimeFm) {
		this.opTimeFm = opTimeFm;
	}

	public Date getOpTimeTo() {
		return opTimeTo;
	}

	public void setOpTimeTo(Date opTimeTo) {
		this.opTimeTo = opTimeTo;
	}

	public BigDecimal getTotalBgQty() {
		return totalBgQty;
	}

	public void setTotalBgQty(BigDecimal totalBgQty) {
		this.totalBgQty = totalBgQty;
	}

	public BigDecimal getTotalInQty() {
		return totalInQty;
	}

	public void setTotalInQty(BigDecimal totalInQty) {
		this.totalInQty = totalInQty;
	}

	public BigDecimal getTotalOutQty() {
		return totalOutQty;
	}

	public void setTotalOutQty(BigDecimal totalOutQty) {
		this.totalOutQty = totalOutQty;
	}

	public BigDecimal getTotalEdQty() {
		return totalEdQty;
	}

	public void setTotalEdQty(BigDecimal totalEdQty) {
		this.totalEdQty = totalEdQty;
	}
}