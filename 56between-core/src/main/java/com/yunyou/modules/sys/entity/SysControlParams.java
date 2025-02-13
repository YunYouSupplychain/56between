package com.yunyou.modules.sys.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 系统控制参数Entity
 * @author Jianhua Liu
 * @version 2019-08-12
 */
public class SysControlParams extends DataEntity<SysControlParams> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 参数代码
	private String value;		// 参数值
	private String orgId;
	private String orgName;		// 机构名称
	private String type;		// 类别
	
	public SysControlParams() {
		super();
	}

	public SysControlParams(String id){
		super(id);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}