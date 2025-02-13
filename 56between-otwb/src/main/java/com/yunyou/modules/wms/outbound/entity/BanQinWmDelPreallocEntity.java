package com.yunyou.modules.wms.outbound.entity;

/**
 * 取消预配明细Entity
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinWmDelPreallocEntity extends BanQinWmDelPrealloc {
	// 货主名称
	private String ownerName;
	// 商品名称
	private String skuName;
	// 操作人名称
	private String opName;
	// 创建人名称
	private String creatorName;
	// 修改人名称
	private String modifierName;
	// 包装规格
	private String packDesc;
	// 包装单位描述
	private String uomDesc;

	public String getPackDesc() {
		return packDesc;
	}

	public void setPackDesc(String packDesc) {
		this.packDesc = packDesc;
	}

	public String getUomDesc() {
		return uomDesc;
	}

	public void setUomDesc(String uomDesc) {
		this.uomDesc = uomDesc;
	}

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

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
}