package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 预配规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRulePreallocDetail extends DataEntity<BanQinCdRulePreallocDetail> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String lineNo;		// 行号
	private String uom;		// 包装单位
	private String uomName;		// 包装单位名称
	private String isEnable;		// 是否启用
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	
	public BanQinCdRulePreallocDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRulePreallocDetail(String id){
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
	
	@ExcelField(title="包装单位", align=2, sort=4)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="包装单位名称", align=2, sort=5)
	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}
	
	@ExcelField(title="是否启用", align=2, sort=6)
	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
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