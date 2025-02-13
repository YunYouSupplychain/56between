package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 库存周转规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRuleRotationDetail extends DataEntity<BanQinCdRuleRotationDetail> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String lineNo;		// 行号
	private String lotAtt;		// 批次属性
	private String orderBy;		// 排序方式（A升序，D降序，E精确匹配）
	private String orgId;		// 分公司
    private String headerId;    // 表头Id
	
	public BanQinCdRuleRotationDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRuleRotationDetail(String id){
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

	@NotNull(message="行号不能为空")
	@ExcelField(title="行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	@NotNull(message="批次属性不能为空")
	@ExcelField(title="批次属性", align=2, sort=4)
	public String getLotAtt() {
		return lotAtt;
	}

	public void setLotAtt(String lotAtt) {
		this.lotAtt = lotAtt;
	}

	@NotNull(message="排序方式不能为空")
	@ExcelField(title="排序方式", align=2, sort=5)
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@ExcelField(title="分公司", align=2, sort=13)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }
}