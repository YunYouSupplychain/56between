package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 质检规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRuleQcClass extends DataEntity<BanQinCdRuleQcClass> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 质检规则编码
	private String lineNo;		// 行号
	private Double fmClass;		// 级差区间从（大于）
	private Double toClass;		// 级差区间到（小于等于）
	private String qtyType;		// 数量类型（按数量/按比例）
	private Double qty;		// 数量/比例
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	
	public BanQinCdRuleQcClass() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRuleQcClass(String id){
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

	@NotNull(message="级差区间从（大于）不能为空")
	@ExcelField(title="级差区间从（大于）", align=2, sort=4)
	public Double getFmClass() {
		return fmClass;
	}

	public void setFmClass(Double fmClass) {
		this.fmClass = fmClass;
	}

	@NotNull(message="级差区间到（小于）不能为空")
	@ExcelField(title="级差区间到（小于等于）", align=2, sort=5)
	public Double getToClass() {
		return toClass;
	}

	public void setToClass(Double toClass) {
		this.toClass = toClass;
	}

	@NotNull(message="数量类型不能为空")
	@ExcelField(title="数量类型", align=2, sort=6)
	public String getQtyType() {
		return qtyType;
	}

	public void setQtyType(String qtyType) {
		this.qtyType = qtyType;
	}

	@NotNull(message="数量/比例不能为空")
	@ExcelField(title="数量/比例", align=2, sort=7)
	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	@ExcelField(title="分公司", align=2, sort=16)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="头表Id", align=2, sort=17)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
	
}