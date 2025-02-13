package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 快递接口信息Entity
 * @author WMJ
 * @version 2020-05-06
 */
public class BanQinCdTrackingInfo extends DataEntity<BanQinCdTrackingInfo> {

	private static final long serialVersionUID = 1L;
	private String url;			// 接口地址
	private String type;		// 接口类型
	private String params;		// 接口参数
	private String description; // 接口描述
	private String orgId;		// 平台Id

	public BanQinCdTrackingInfo() {
		super();
		this.recVer = 0;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}