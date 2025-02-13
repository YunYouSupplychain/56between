package com.yunyou.modules.wms.inventory.entity;

/**
 * 序列号转移entity
 * @author WMJ
 * @version 2019/03/07
 */
public class BanQinWmTfSerialEntity extends BanQinWmTfSerial {
	private String ownerName;
	private String skuName;

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

}
