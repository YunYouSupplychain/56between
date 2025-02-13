package com.yunyou.modules.wms.outbound.entity;

/**
 * 销售明细
 * @author WMJ
 * @version 2019-02-20
 */
public class BanQinWmSaleDetailEntity extends BanQinWmSaleDetail {
	// 商品名称
	private String skuName;
	// 单位描述
	private String uomDesc;
	// 包装描述
	private String packDesc;
	// 单位数量
	private Double uomQty;
	// 快速录入码
	private String quickCode;
	// 销售单位数
	private Double qtySaleUom;
	// 订货单位数
	private Double qtySoUom;
	// 预配单位数
	private Double qtyPreallocUom;
	// 分配单位数
	private Double qtyAllocUom;
	// 拣货单位数
	private Double qtyPkUom;
	// 发货单位数
	private Double qtyShipUom;
	// 当前可订货数EA
	private Double currentQtySoEa;
	// 当前可订货单位数
	private Double currentQtySoUom;

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getUomDesc() {
		return uomDesc;
	}

	public void setUomDesc(String uomDesc) {
		this.uomDesc = uomDesc;
	}

	public String getPackDesc() {
		return packDesc;
	}

	public void setPackDesc(String packDesc) {
		this.packDesc = packDesc;
	}

	public Double getUomQty() {
		return uomQty;
	}

	public void setUomQty(Double uomQty) {
		this.uomQty = uomQty;
	}

	public String getQuickCode() {
		return quickCode;
	}

	public void setQuickCode(String quickCode) {
		this.quickCode = quickCode;
	}

	public Double getQtySaleUom() {
		return qtySaleUom;
	}

	public void setQtySaleUom(Double qtySaleUom) {
		this.qtySaleUom = qtySaleUom;
	}

	public Double getQtySoUom() {
		return qtySoUom;
	}

	public void setQtySoUom(Double qtySoUom) {
		this.qtySoUom = qtySoUom;
	}

	public Double getQtyPreallocUom() {
		return qtyPreallocUom;
	}

	public void setQtyPreallocUom(Double qtyPreallocUom) {
		this.qtyPreallocUom = qtyPreallocUom;
	}

	public Double getQtyAllocUom() {
		return qtyAllocUom;
	}

	public void setQtyAllocUom(Double qtyAllocUom) {
		this.qtyAllocUom = qtyAllocUom;
	}

	public Double getQtyPkUom() {
		return qtyPkUom;
	}

	public void setQtyPkUom(Double qtyPkUom) {
		this.qtyPkUom = qtyPkUom;
	}

	public Double getQtyShipUom() {
		return qtyShipUom;
	}

	public void setQtyShipUom(Double qtyShipUom) {
		this.qtyShipUom = qtyShipUom;
	}

	public Double getCurrentQtySoEa() {
		return currentQtySoEa;
	}

	public void setCurrentQtySoEa(Double currentQtySoEa) {
		this.currentQtySoEa = currentQtySoEa;
	}

	public Double getCurrentQtySoUom() {
		return currentQtySoUom;
	}

	public void setCurrentQtySoUom(Double currentQtySoUom) {
		this.currentQtySoUom = currentQtySoUom;
	}

}
