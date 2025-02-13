package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 承运商类型关系Entity
 * @author zyf
 * @version 2020-01-07
 */
public class BanQinWmCarrierTypeRelation extends DataEntity<BanQinWmCarrierTypeRelation> {
	
	private static final long serialVersionUID = 1L;
	private String carrierCode;		// 承运商编码
	private String type;		// 类型
	private String orgId;		// 机构id
	private String trackingInterfaceId;	// 快递接口信息Id
	private String mailType;	// 面单类型

	public BanQinWmCarrierTypeRelation() {
		super();
		this.recVer = 0;
	}

	public BanQinWmCarrierTypeRelation(String id){
		super(id);
	}

	@ExcelField(title="承运商编码", align=2, sort=7)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	@ExcelField(title="类型", align=2, sort=8)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="机构id", align=2, sort=9)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTrackingInterfaceId() {
		return trackingInterfaceId;
	}

	public void setTrackingInterfaceId(String trackingInterfaceId) {
		this.trackingInterfaceId = trackingInterfaceId;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
}