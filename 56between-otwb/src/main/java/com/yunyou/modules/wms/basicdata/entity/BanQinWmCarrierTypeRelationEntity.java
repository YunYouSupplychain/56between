package com.yunyou.modules.wms.basicdata.entity;

/**
 * 承运商类型关系Entity
 * @author zyf
 * @version 2020-01-07
 */
public class BanQinWmCarrierTypeRelationEntity extends BanQinWmCarrierTypeRelation {

	private String carrierName;		// 承运商名称
	private String description;		// 接口描述
	private String url;				// 接口地址
	private String params;			// 接口参数

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}