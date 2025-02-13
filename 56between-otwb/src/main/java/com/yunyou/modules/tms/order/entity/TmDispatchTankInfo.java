package com.yunyou.modules.tms.order.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 派车装罐信息Entity
 * @author zyf
 * @version 2020-08-18
 */
public class TmDispatchTankInfo extends DataEntity<TmDispatchTankInfo> {

	private static final long serialVersionUID = 1L;
	private String dispatchNo;				// 派车单号
	private String outletCode;				// 网点
	private String transportNo;				// 运输订单号
	private BigDecimal offloadingQty;		// 卸油数
	private BigDecimal tankQty;				// 装罐数
	private String orgId;					// 机构ID
	private String baseOrgId;   			// 基础数据机构ID

	public TmDispatchTankInfo() {
		super();
	}

	public TmDispatchTankInfo(String id){
		super(id);
	}

	@ExcelField(title="派车单号", align=2, sort=8)
	public String getDispatchNo() {
		return dispatchNo;
	}

	public void setDispatchNo(String dispatchNo) {
		this.dispatchNo = dispatchNo;
	}

	public String getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}

	public String getTransportNo() {
		return transportNo;
	}

	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}

	public BigDecimal getOffloadingQty() {
		return offloadingQty;
	}

	public void setOffloadingQty(BigDecimal offloadingQty) {
		this.offloadingQty = offloadingQty;
	}

	public BigDecimal getTankQty() {
		return tankQty;
	}

	public void setTankQty(BigDecimal tankQty) {
		this.tankQty = tankQty;
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