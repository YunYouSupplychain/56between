package com.yunyou.modules.wms.outbound.entity;

import java.util.Date;

/**
 * 出库单打包记录Entity
 * @author WMJ
 * @version 2019-12-16
 */
public class BanQinWmSoPackEntity extends BanQinWmSoPack {
	private String orgName;
	private Date packTimeFm;
	private Date packTimeTo;
	private Date orderTimeFm;
	private Date orderTimeTo;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Date getPackTimeFm() {
		return packTimeFm;
	}

	public void setPackTimeFm(Date packTimeFm) {
		this.packTimeFm = packTimeFm;
	}

	public Date getPackTimeTo() {
		return packTimeTo;
	}

	public void setPackTimeTo(Date packTimeTo) {
		this.packTimeTo = packTimeTo;
	}

	public Date getOrderTimeFm() {
		return orderTimeFm;
	}

	public void setOrderTimeFm(Date orderTimeFm) {
		this.orderTimeFm = orderTimeFm;
	}

	public Date getOrderTimeTo() {
		return orderTimeTo;
	}

	public void setOrderTimeTo(Date orderTimeTo) {
		this.orderTimeTo = orderTimeTo;
	}
}