package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 波次单Entity
 * @author WMJ
 * @version 2019-02-15
 */
public class BanQinWmWvHeaderEntity extends BanQinWmWvHeader {
	private String orgName;
	private Date createDateFm;
	private Date createDateTo;
	private String isPrint;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDateFm() {
		return createDateFm;
	}

	public void setCreateDateFm(Date createDateFm) {
		this.createDateFm = createDateFm;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDateTo() {
		return createDateTo;
	}

	public void setCreateDateTo(Date createDateTo) {
		this.createDateTo = createDateTo;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}
}