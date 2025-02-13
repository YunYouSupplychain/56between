package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 运输订单费用信息Entity
 * @author liujianhua
 * @version 2020-03-04
 */
public class TmTransportOrderCost extends DataEntity<TmTransportOrderCost> {
	
	private static final long serialVersionUID = 1L;
	private String transportNo;		// 运输单号
	private String costType;		// 费用类型
	private String settlementMethod;		// 结算方式
	private String settlementCode;		// 结算对象编码
	private String settlementType;		// 结算对象类型
	private Double amount;		// 金额
	private String isBill;		// 是否结账
	private String billVoucher;		// 结账凭证
	private String orgId;		// 机构ID
	private String baseOrgId;   // 基础数据机构ID
	
	public TmTransportOrderCost() {
		super();
	}

	public TmTransportOrderCost(String id){
		super(id);
	}

	public TmTransportOrderCost(String transportNo, String orgId) {
		this.transportNo = transportNo;
		this.orgId = orgId;
	}

	@ExcelField(title="运输单号", align=2, sort=7)
	public String getTransportNo() {
		return transportNo;
	}

	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}
	
	@ExcelField(title="费用类型", dictType="TMS_COST_TYPE", align=2, sort=8)
	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}
	
	@ExcelField(title="结算方式", dictType="BMS_SETTLEMENT_TYPE", align=2, sort=9)
	public String getSettlementMethod() {
		return settlementMethod;
	}

	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}
	
	@ExcelField(title="结算对象编码", align=2, sort=10)
	public String getSettlementCode() {
		return settlementCode;
	}

	public void setSettlementCode(String settlementCode) {
		this.settlementCode = settlementCode;
	}
	
	@ExcelField(title="结算对象类型", dictType="TMS_SETTLEMENT_TYPE", align=2, sort=11)
	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}
	
	@ExcelField(title="金额", align=2, sort=12)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@ExcelField(title="是否结账", dictType="SYS_YES_NO", align=2, sort=13)
	public String getIsBill() {
		return isBill;
	}

	public void setIsBill(String isBill) {
		this.isBill = isBill;
	}
	
	@ExcelField(title="结账凭证", align=2, sort=14)
	public String getBillVoucher() {
		return billVoucher;
	}

	public void setBillVoucher(String billVoucher) {
		this.billVoucher = billVoucher;
	}
	
	@ExcelField(title="机构ID", align=2, sort=15)
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