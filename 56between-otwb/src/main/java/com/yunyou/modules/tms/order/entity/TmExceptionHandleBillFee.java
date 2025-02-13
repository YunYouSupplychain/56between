package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 异常处理单费用Entity
 * @author zyf
 * @version 2020-07-29
 */
public class TmExceptionHandleBillFee extends DataEntity<TmExceptionHandleBillFee> {

	private static final long serialVersionUID = 1L;
	private String billNo;						// 异常处理单号
	private String rpFlag;						// 应收应付
	private String chargeName;					// 费用名称
	private Double amount;						// 金额
	private String liabilitySysUserCode;		// 责任人编码
	private String liabilitySysUserName;		// 责任人名称
	// 机构ID
	private String orgId;
	// 基础数据机构ID
	private String baseOrgId;

	public TmExceptionHandleBillFee() {
		super();
	}

	public TmExceptionHandleBillFee(String id){
		super(id);
	}

	public TmExceptionHandleBillFee(String billNo, String orgId) {
		this.billNo = billNo;
		this.orgId = orgId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getLiabilitySysUserCode() {
		return liabilitySysUserCode;
	}

	public void setLiabilitySysUserCode(String liabilitySysUserCode) {
		this.liabilitySysUserCode = liabilitySysUserCode;
	}

	public String getLiabilitySysUserName() {
		return liabilitySysUserName;
	}

	public void setLiabilitySysUserName(String liabilitySysUserName) {
		this.liabilitySysUserName = liabilitySysUserName;
	}

	public String getRpFlag() {
		return rpFlag;
	}

	public void setRpFlag(String rpFlag) {
		this.rpFlag = rpFlag;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getBaseOrgId() {
		return baseOrgId;
	}

	public void setBaseOrgId(String baseOrgId) {
		this.baseOrgId = baseOrgId;
	}
}