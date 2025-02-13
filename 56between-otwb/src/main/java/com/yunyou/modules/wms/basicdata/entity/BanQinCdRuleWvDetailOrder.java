package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 波次规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRuleWvDetailOrder extends DataEntity<BanQinCdRuleWvDetailOrder> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String lineNo;		// 行号
	private String andOr;		// AND或OR运算符
	private String leftBracket;		// 左括号
	private String orderAttCode;		// 出库单属性编码
	private String orderAttName;		// 出库单属性名称
	private String operator;		// 运算符
	private String value;		// 值
	private String rightBracket;		// 右括号
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	
	public BanQinCdRuleWvDetailOrder() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRuleWvDetailOrder(String id){
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
	
	@ExcelField(title="AND或OR运算符", align=2, sort=4)
	public String getAndOr() {
		return andOr;
	}

	public void setAndOr(String andOr) {
		this.andOr = andOr;
	}
	
	@ExcelField(title="左括号", align=2, sort=5)
	public String getLeftBracket() {
		return leftBracket;
	}

	public void setLeftBracket(String leftBracket) {
		this.leftBracket = leftBracket;
	}
	
	@ExcelField(title="出库单属性编码", align=2, sort=6)
	public String getOrderAttCode() {
		return orderAttCode;
	}

	public void setOrderAttCode(String orderAttCode) {
		this.orderAttCode = orderAttCode;
	}
	
	@ExcelField(title="出库单属性名称", align=2, sort=7)
	public String getOrderAttName() {
		return orderAttName;
	}

	public void setOrderAttName(String orderAttName) {
		this.orderAttName = orderAttName;
	}
	
	@ExcelField(title="运算符", align=2, sort=8)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@ExcelField(title="值", align=2, sort=9)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@ExcelField(title="右括号", align=2, sort=10)
	public String getRightBracket() {
		return rightBracket;
	}

	public void setRightBracket(String rightBracket) {
		this.rightBracket = rightBracket;
	}

	@ExcelField(title="分公司", align=2, sort=18)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="头表Id", align=2, sort=19)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
	
}