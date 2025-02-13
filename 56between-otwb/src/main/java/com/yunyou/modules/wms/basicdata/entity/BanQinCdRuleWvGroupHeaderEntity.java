package com.yunyou.modules.wms.basicdata.entity;

import java.util.List;

/**
 * 波次规则组Entity
 * @author WMJ
 * @version 2020-02-09
 */
public class BanQinCdRuleWvGroupHeaderEntity extends BanQinCdRuleWvGroupHeader {
	private static final long serialVersionUID = 1L;
	private List<BanQinCdRuleWvGroupDetail> detailList;
	private String[] ownerCodes;
	private String[] skuCodes;

	public List<BanQinCdRuleWvGroupDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<BanQinCdRuleWvGroupDetail> detailList) {
		this.detailList = detailList;
	}

	public String[] getOwnerCodes() {
		return ownerCodes;
	}

	public void setOwnerCodes(String[] ownerCodes) {
		this.ownerCodes = ownerCodes;
	}

	public String[] getSkuCodes() {
		return skuCodes;
	}

	public void setSkuCodes(String[] skuCodes) {
		this.skuCodes = skuCodes;
	}
}