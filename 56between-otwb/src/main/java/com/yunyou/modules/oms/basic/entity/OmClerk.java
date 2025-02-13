package com.yunyou.modules.oms.basic.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 业务员Entity
 * @author Jianhua Liu
 * @version 2019-07-29
 */
public class OmClerk extends DataEntity<OmClerk> {
	
	private static final long serialVersionUID = 1L;
	private String clerkCode;		// 业务员代码
	private String clerkName;		// 业务员名称
	private String phone;		// 联系电话
	private String orgId;		// 机构

	// 查询条件
	private String codeAndName;

	public OmClerk() {
		super();
	}

	public OmClerk(String id){
		super(id);
	}

	@ExcelField(title="业务员代码", align=2, sort=7)
	public String getClerkCode() {
		return clerkCode;
	}

	public void setClerkCode(String clerkCode) {
		this.clerkCode = clerkCode;
	}
	
	@ExcelField(title="业务员名称", align=2, sort=8)
	public String getClerkName() {
		return clerkName;
	}

	public void setClerkName(String clerkName) {
		this.clerkName = clerkName;
	}
	
	@ExcelField(title="联系电话", align=2, sort=9)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="机构", align=2, sort=10)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCodeAndName() {
		return codeAndName;
	}

	public void setCodeAndName(String codeAndName) {
		this.codeAndName = codeAndName;
	}
}