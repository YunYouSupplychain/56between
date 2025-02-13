package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 质检项Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhQcItemHeader extends DataEntity<BanQinCdWhQcItemHeader> {
	
	private static final long serialVersionUID = 1L;
	private String itemGroupCode;		// 质检项组编码
	private String itemGroupName;		// 质检项组名称
	private String orgId;		// 分公司
	private String itemGroupCodeAndName; // 模糊查询字段
	
	public BanQinCdWhQcItemHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhQcItemHeader(String id){
		super(id);
	}

	@NotNull(message="质检项组编码不能为空")
	@ExcelField(title="质检项组编码", align=2, sort=2)
	public String getItemGroupCode() {
		return itemGroupCode;
	}

	public void setItemGroupCode(String itemGroupCode) {
		this.itemGroupCode = itemGroupCode;
	}

	@NotNull(message="质检项组名称不能为空")
	@ExcelField(title="质检项组名称", align=2, sort=3)
	public String getItemGroupName() {
		return itemGroupName;
	}

	public void setItemGroupName(String itemGroupName) {
		this.itemGroupName = itemGroupName;
	}

	@ExcelField(title="分公司", align=2, sort=12)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getItemGroupCodeAndName() {
		return itemGroupCodeAndName;
	}

	public void setItemGroupCodeAndName(String itemGroupCodeAndName) {
		this.itemGroupCodeAndName = itemGroupCodeAndName;
	}
}