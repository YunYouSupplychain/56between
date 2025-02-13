package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 波次规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRuleWvDetail extends DataEntity<BanQinCdRuleWvDetail> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String lineNo;		// 行号
	private String mainCode;		// 主规则编码
	private String descr;		// 描述
	private String isEnable;		// 是否启用
	private String sql;		// 订单限制SQL
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
    private List<BanQinCdRuleWvDetailWv> ruleWvDetailWvList; // 波次限制明细
    private List<BanQinCdRuleWvDetailOrder> ruleWvDetailOrderList; //订单限制明细
	
	public BanQinCdRuleWvDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRuleWvDetail(String id){
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

	@NotNull(message="主规则不能为空")
	@ExcelField(title="主规则编码", align=2, sort=4)
	public String getMainCode() {
		return mainCode;
	}

	public void setMainCode(String mainCode) {
		this.mainCode = mainCode;
	}

	@NotNull(message="描述不能为空")
	@ExcelField(title="描述", align=2, sort=5)
	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	@ExcelField(title="是否启用", align=2, sort=6)
	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	
	@ExcelField(title="订单限制SQL", align=2, sort=7)
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
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

    public List<BanQinCdRuleWvDetailWv> getRuleWvDetailWvList() {
        return ruleWvDetailWvList;
    }

    public void setRuleWvDetailWvList(List<BanQinCdRuleWvDetailWv> ruleWvDetailWvList) {
        this.ruleWvDetailWvList = ruleWvDetailWvList;
    }

    public List<BanQinCdRuleWvDetailOrder> getRuleWvDetailOrderList() {
        return ruleWvDetailOrderList;
    }

    public void setRuleWvDetailOrderList(List<BanQinCdRuleWvDetailOrder> ruleWvDetailOrderList) {
        this.ruleWvDetailOrderList = ruleWvDetailOrderList;
    }
}