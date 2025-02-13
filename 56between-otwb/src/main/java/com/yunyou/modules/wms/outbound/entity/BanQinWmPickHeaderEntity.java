package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 拣货单Entity
 * @author ZYF
 * @version 2020-05-13
 */
public class BanQinWmPickHeaderEntity extends BanQinWmPickHeader {
	private String orgName;
	private Date createDateFm;
	private Date createDateTo;

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
}