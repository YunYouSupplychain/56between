package com.yunyou.modules.wms.inventory.entity;

/**
 * 批次号库存表Entity
 * @author WMJ
 * @version 2019-11-08
 */
public class BanQinWmInvLotAttEntity extends BanQinWmInvLotAtt {
	private String skuName;
	private String ownerName;

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}