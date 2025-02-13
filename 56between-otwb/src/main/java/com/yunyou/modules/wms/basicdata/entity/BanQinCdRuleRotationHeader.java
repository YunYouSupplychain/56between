package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 库存周转规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRuleRotationHeader extends DataEntity<BanQinCdRuleRotationHeader> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String ruleName;		// 规则名称
	private String rotationType;		// 周转类型（库存周转优先、包装优先）
	private String lotCode;		// 批次属性编码
	private String orgId;		// 分公司

	public BanQinCdRuleRotationHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRuleRotationHeader(String id){
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
	
	@ExcelField(title="周转类型（库存周转优先、包装优先）", align=2, sort=4)
	public String getRotationType() {
		return rotationType;
	}

	public void setRotationType(String rotationType) {
		this.rotationType = rotationType;
	}
	
	@ExcelField(title="批次属性编码", align=2, sort=5)
	public String getLotCode() {
		return lotCode;
	}

	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}

	@ExcelField(title="分公司", align=2, sort=13)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}