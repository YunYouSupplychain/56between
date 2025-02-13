package com.yunyou.modules.wms.weigh.entity;

/**
 * 称重设备表Entity
 * @author zyf
 * @version 2019-09-21
 */
public class BanQinWeighMachineInfoEntity extends BanQinWeighMachineInfo {

	private String orgName;		// 机构名称

	public BanQinWeighMachineInfoEntity() {
		super();
	}

	public BanQinWeighMachineInfoEntity(String id){
		super(id);
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}