package com.yunyou.modules.wms.weigh.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 称重设备表Entity
 * @author zyf
 * @version 2019-09-21
 */
public class BanQinWeighMachineInfo extends DataEntity<BanQinWeighMachineInfo> {
	
	private static final long serialVersionUID = 1L;
	private String machineNo;		// 设备编码
	private String orgId;			// 机构id
	private String brand;			// 品牌
	private String businessType;	// 业务类型
	
	public BanQinWeighMachineInfo() {
		super();
	}

	public BanQinWeighMachineInfo(String id){
		super(id);
	}

	@ExcelField(title="设备编码", align=2, sort=7)
	public String getMachineNo() {
		return machineNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}
	
	@ExcelField(title="机构id", align=2, sort=8)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
}