package com.yunyou.modules.wms.outbound.entity;

/**
 * 复核打包的商品数，单品数，总重量，总体积
 */
public class BanQinCheckPackingTotalInfoEntity {
	// 商品数
	private Double skuQty;
	// 单品数
	private Double skuItemQty;
	// 总重量
	private Double weight;
	// 总体积
	private Double cubic;

	public Double getSkuQty() {
		return skuQty;
	}

	public void setSkuQty(Double skuQty) {
		this.skuQty = skuQty;
	}

	public Double getSkuItemQty() {
		return skuItemQty;
	}

	public void setSkuItemQty(Double skuItemQty) {
		this.skuItemQty = skuItemQty;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getCubic() {
		return cubic;
	}

	public void setCubic(Double cubic) {
		this.cubic = cubic;
	}

}