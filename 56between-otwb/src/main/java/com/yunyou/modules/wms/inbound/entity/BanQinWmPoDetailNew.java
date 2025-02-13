package com.yunyou.modules.wms.inbound.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 采购单明细Entity
 * @author WMJ
 * @version 2019-01-26
 */
public class BanQinWmPoDetailNew extends DataEntity<BanQinWmPoDetailNew> {
	
	private static final long serialVersionUID = 1L;
	private String poNo;		// PO单号
	private String logisticNo;		// 物流单号
	private String ownerCode;		// 货主代码
	private String skuCode;		// 商品编码
	private Double qtyPoEa;		// 订货数量EA
	private String skuColor;		// 商品颜色
	private String skuUuc128;		// UUC128码
	private String carton;		// 箱号
	private String orgId;		// 平台ID
	private String skuSize;		// 商品码
	private String booking;		// 舱号
	private String headId;		// PO主键
	
	public BanQinWmPoDetailNew() {
		super();
		this.recVer = 0;
	}

	public BanQinWmPoDetailNew(String id){
		super(id);
	}

	@ExcelField(title="PO单号", align=2, sort=2)
	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	
	@ExcelField(title="物流单号", align=2, sort=3)
	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}
	
	@ExcelField(title="货主代码", align=2, sort=4)
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
	
	@ExcelField(title="订货数量EA", align=2, sort=6)
	public Double getQtyPoEa() {
		return qtyPoEa;
	}

	public void setQtyPoEa(Double qtyPoEa) {
		this.qtyPoEa = qtyPoEa;
	}
	
	@ExcelField(title="商品颜色", align=2, sort=7)
	public String getSkuColor() {
		return skuColor;
	}

	public void setSkuColor(String skuColor) {
		this.skuColor = skuColor;
	}
	
	@ExcelField(title="UUC128码", align=2, sort=8)
	public String getSkuUuc128() {
		return skuUuc128;
	}

	public void setSkuUuc128(String skuUuc128) {
		this.skuUuc128 = skuUuc128;
	}
	
	@ExcelField(title="箱号", align=2, sort=9)
	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	@ExcelField(title="平台ID", align=2, sort=17)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="商品码", align=2, sort=18)
	public String getSkuSize() {
		return skuSize;
	}

	public void setSkuSize(String skuSize) {
		this.skuSize = skuSize;
	}
	
	@ExcelField(title="舱号", align=2, sort=19)
	public String getBooking() {
		return booking;
	}

	public void setBooking(String booking) {
		this.booking = booking;
	}
	
	@ExcelField(title="PO主键", align=2, sort=20)
	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}
	
}