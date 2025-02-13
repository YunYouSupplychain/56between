package com.yunyou.modules.tms.order.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 交接单商品Entity
 * @author zyf
 * @version 2020-03-30
 */
public class TmHandoverOrderSku extends DataEntity<TmHandoverOrderSku> {

	private static final long serialVersionUID = 1L;
	private String handoverNo;		// 交接单号
	private String transportNo;		// 运输订单号
	private String lineNo;// 行号
	private String customerNo;		// 客户订单号
	private String ownerCode;		// 货主编码
	private String skuCode;			// 商品编码
	private BigDecimal orderQty;	// 计划数量
	private BigDecimal actualQty;	// 实际数量
	private BigDecimal unloadingTime;	// 卸货时长
	private String receiveShip;		// 收/发
	private String orgId;			// 机构ID
	private String baseOrgId;   	// 基础数据机构ID

	public TmHandoverOrderSku() {
		super();
	}

	public TmHandoverOrderSku(String handoverNo, String orgId) {
		super();
		this.handoverNo = handoverNo;
		this.orgId = orgId;
	}

	public TmHandoverOrderSku(String id){
		super(id);
	}

	@ExcelField(title="交接单号", align=2, sort=7)
	public String getHandoverNo() {
		return handoverNo;
	}

	public void setHandoverNo(String handoverNo) {
		this.handoverNo = handoverNo;
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

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}

	public BigDecimal getActualQty() {
		return actualQty;
	}

	public void setActualQty(BigDecimal actualQty) {
		this.actualQty = actualQty;
	}

	public BigDecimal getUnloadingTime() {
		return unloadingTime;
	}

	public void setUnloadingTime(BigDecimal unloadingTime) {
		this.unloadingTime = unloadingTime;
	}
}