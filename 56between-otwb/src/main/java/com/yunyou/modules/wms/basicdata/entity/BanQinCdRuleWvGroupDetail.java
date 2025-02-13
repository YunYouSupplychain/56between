package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 波次规则组明细Entity
 * @author WMJ
 * @version 2020-02-09
 */
public class BanQinCdRuleWvGroupDetail extends DataEntity<BanQinCdRuleWvGroupDetail> {
	
	private static final long serialVersionUID = 1L;
	private String groupCode;		// 规则组编码
	private String lineNo;			 // 行号
	private String ruleCode;		// 波次规则编码
	private String orgId;		// 平台Id
	private String headerId;		// 表头Id
	
	public BanQinCdRuleWvGroupDetail() {
		super();
	}

	public BanQinCdRuleWvGroupDetail(String id){
		super(id);
	}

	@ExcelField(title="波次规则编码", align=2, sort=7)
	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	
	@ExcelField(title="平台Id", align=2, sort=8)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="表头Id", align=2, sort=9)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
}