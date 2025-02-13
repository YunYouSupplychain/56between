package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 业务服务范围Entity
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmTransportScope extends DataEntity<TmTransportScope> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 服务范围编码
	private String name;		// 服务范围名称
	private String orgId;		// 机构ID

	public TmTransportScope() {
		super();
	}

	public TmTransportScope(String id){
		super(id);
	}

	public TmTransportScope(String code, String orgId) {
		this.code = code;
		this.orgId = orgId;
	}

	@ExcelField(title="服务范围编码", align=2, sort=7)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="服务范围名称", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="机构ID", align=2, sort=9)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}