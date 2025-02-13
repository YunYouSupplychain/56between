package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 预配规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRulePreallocHeader extends DataEntity<BanQinCdRulePreallocHeader> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String ruleName;		// 规则名称
	private String orgId;		// 分公司
	private String ruleCodeAndName; // 模糊查询字段
	
	public BanQinCdRulePreallocHeader() {
		super();
	}

	public BanQinCdRulePreallocHeader(String id){
		super(id);
	}

	@ExcelField(title="规则编码", align=2, sort=2)
	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	
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