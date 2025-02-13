package com.yunyou.modules.wms.inventory.entity;

/**
 * 库存冻结entity
 * @author WMJ
 * @version 2019/03/06
 */
public class BanQinWmHoldEntity extends BanQinWmHold {
	private String ownerName;
	private String skuName;
	private String orgName;

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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
