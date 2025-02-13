package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 质检规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRuleQcDetail extends DataEntity<BanQinCdRuleQcDetail> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 质检规则编码
	private String lineNo;		// 行号
	private Double fmRate;		// 合格率区间从
	private Double toRate;		// 合格率区间到
	private String qcSuggest;		// 质检处理建议
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	
	public BanQinCdRuleQcDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRuleQcDetail(String id){
		super(id);
	}

	@NotNull(message="质检规则编码不能为空")
	@ExcelField(title="质检规则编码", align=2, sort=2)
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

	@NotNull(message="合格率区间从不能为空")
	@ExcelField(title="合格率区间从", align=2, sort=4)
	public Double getFmRate() {
		return fmRate;
	}

	public void setFmRate(Double fmRate) {
		this.fmRate = fmRate;
	}

	@NotNull(message="合格率区间到不能为空")
	@ExcelField(title="合格率区间到", align=2, sort=5)
	public Double getToRate() {
		return toRate;
	}

	public void setToRate(Double toRate) {
		this.toRate = toRate;
	}

	@NotNull(message="质检处理建议不能为空")
	@ExcelField(title="质检处理建议", align=2, sort=6)
	public String getQcSuggest() {
		return qcSuggest;
	}

	public void setQcSuggest(String qcSuggest) {
		this.qcSuggest = qcSuggest;
	}

	@ExcelField(title="分公司", align=2, sort=15)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="头表Id", align=2, sort=16)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
	
}