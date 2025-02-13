package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 质检规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRulePaHeader extends DataEntity<BanQinCdRulePaHeader> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String ruleName;		// 规则名称
	private String orgId;		// 分公司
	private String ruleCodeAndName; // 模糊查询字段

	public BanQinCdRulePaHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRulePaHeader(String id){
		super(id);
	}

	@NotNull(message="规则编码不能为空")
	@ExcelField(title="规则编码", align=2, sort=2)
	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	@NotNull(message="规则名称不能为空")
	@ExcelField(title="规则名称", align=2, sort=3)
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	@ExcelField(title="分公司", align=2, sort=11)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getRuleCodeAndName() {
		return ruleCodeAndName;
	}

	public void setRuleCodeAndName(String ruleCodeAndName) {
		this.ruleCodeAndName = ruleCodeAndName;
	}
}