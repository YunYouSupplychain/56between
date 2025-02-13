package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 波次规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRuleWvDetailWv extends DataEntity<BanQinCdRuleWvDetailWv> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String lineNo;		// 行号
	private String condition;		// 限制条件
	private String operator;		// 运算符
	private String value;		// 值
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	
	public BanQinCdRuleWvDetailWv() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRuleWvDetailWv(String id){
		super(id);
	}

	@ExcelField(title="规则编码", align=2, sort=2)
	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	
	@ExcelField(title="行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="限制条件", align=2, sort=4)
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@ExcelField(title="运算符", align=2, sort=5)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@ExcelField(title="值", align=2, sort=6)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ExcelField(title="分公司", align=2, sort=14)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="头表Id", align=2, sort=15)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
	
}