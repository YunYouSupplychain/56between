package com.yunyou.modules.wms.outbound.entity;

import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;

import java.util.Date;

/**
 * 获取批次库位库存 入参entity(出库管理)
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinAllocInvLotLocEntity extends BanQinWmInvLotLoc {
	// 操作数量=可分配数量，根据单位换算的EA数量
	private Double qtyEaOp;
	// 操作单位数量
	private Double qtyUomOp;
	// 批次属性01
	private Date lotAtt01;
	// 批次属性02
	private Date lotAtt02;
	// 批次属性03
	private Date lotAtt03;
	// 批次属性04
	private String lotAtt04;
	// 批次属性05
	private String lotAtt05;
	// 批次属性06
	private String lotAtt06;
	// 批次属性07
	private String lotAtt07;
	// 批次属性08
	private String lotAtt08;
	// 批次属性09
	private String lotAtt09;
	// 批次属性10
	private String lotAtt10;
	// 批次属性11
	private String lotAtt11;
	// 批次属性12
	private String lotAtt12;

	public Double getQtyEaOp() {
		return qtyEaOp;
	}

	public void setQtyEaOp(Double qtyEaOp) {
		this.qtyEaOp = qtyEaOp;
	}

	public Double getQtyUomOp() {
		return qtyUomOp;
	}

	public void setQtyUomOp(Double qtyUomOp) {
		this.qtyUomOp = qtyUomOp;
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

}