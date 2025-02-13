package com.yunyou.modules.wms.basicdata.entity;

import java.util.List;

/**
 * 库位Entity
 * @author WMJ
 * @version 2019-08-16
 */
public class BanQinCdWhLocEntity extends BanQinCdWhLoc {
	private static final long serialVersionUID = 1L;
	private List<BanQinCdWhLoc> locList;
	private int sumLength;

	public List<BanQinCdWhLoc> getLocList() {
		return locList;
	}

	public void setLocList(List<BanQinCdWhLoc> locList) {
		this.locList = locList;
	}

	public int getSumLength() {
		return sumLength;
	}

	public void setSumLength(int sumLength) {
		this.sumLength = sumLength;
	}
}