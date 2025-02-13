package com.yunyou.modules.oms.basic.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 业务服务范围Entity
 * @author Jianhua Liu
 * @version 2019-10-23
 */
public class OmBusinessServiceScope extends DataEntity<OmBusinessServiceScope> {
	
	private static final long serialVersionUID = 1L;
	private String groupCode;		// 组编码
	private String groupName;		// 组名称
	private String orgId;		// 机构ID
	
	public OmBusinessServiceScope() {
		super();
	}

	public OmBusinessServiceScope(String id){
		super(id);
	}

	@ExcelField(title="组编码", align=2, sort=7)
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	@ExcelField(title="组名称", align=2, sort=8)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@ExcelField(title="机构ID", align=2, sort=9)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}