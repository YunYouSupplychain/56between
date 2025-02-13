package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 交接单标签Entity
 * @author zyf
 * @version 2020-03-30
 */
public class TmHandoverOrderLabel extends DataEntity<TmHandoverOrderLabel> {
	
	private static final long serialVersionUID = 1L;
	private String handoverNo;		// 交接单号
	private String labelNo;		// 标签号
	private String transportNo;		// 运输订单号
	private String customerNo;		// 客户订单号
	private String status;		// 状态
	private String receiveShip;		// 收/发
	private String orgId;		// 机构ID
	private String baseOrgId;   // 基础数据机构ID
	
	public TmHandoverOrderLabel() {
		super();
	}

	public TmHandoverOrderLabel(String handoverNo, String orgId) {
		super();
		this.handoverNo = handoverNo;
		this.orgId = orgId;
	}

	public TmHandoverOrderLabel(String id){
		super(id);
	}

	@ExcelField(title="交接单号", align=2, sort=7)
	public String getHandoverNo() {
		return handoverNo;
	}

	public void setHandoverNo(String handoverNo) {
		this.handoverNo = handoverNo;
	}
	
	@ExcelField(title="标签号", align=2, sort=8)
	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}
	
	@ExcelField(title="运输订单号", align=2, sort=9)
	public String getTransportNo() {
		return transportNo;
	}

	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}
	
	@ExcelField(title="客户订单号", align=2, sort=10)
	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	
	@ExcelField(title="状态", align=2, sort=11)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="收/发", align=2, sort=12)
	public String getReceiveShip() {
		return receiveShip;
	}

	public void setReceiveShip(String receiveShip) {
		this.receiveShip = receiveShip;
	}
	
	@ExcelField(title="机构ID", align=2, sort=13)
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