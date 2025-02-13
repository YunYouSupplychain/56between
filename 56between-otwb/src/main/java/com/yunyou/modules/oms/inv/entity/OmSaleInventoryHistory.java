package com.yunyou.modules.oms.inv.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 销售库存履历Entity
 * @author zyf
 * @version 2020-01-06
 */
public class OmSaleInventoryHistory extends DataEntity<OmSaleInventoryHistory> {
	
	private static final long serialVersionUID = 1L;
	private String warehouse;		// 仓库
	private String owner;		// 货主
	private String skuCode;		// 商品
	private String orgId;		// 机构
	private Double beforeQty;		// 操作前数量
	private Double operQty;		// 操作数量
	private Double afterQty;		// 操作后数量
	private String operate;		// 操作
	private String orderNo;		// 操作单号
	
	public OmSaleInventoryHistory() {
		super();
	}

	public OmSaleInventoryHistory(String id){
		super(id);
	}

	@ExcelField(title="仓库", align=2, sort=7)
	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	
	@ExcelField(title="货主", align=2, sort=8)
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@ExcelField(title="商品", align=2, sort=9)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="机构", align=2, sort=10)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="操作前数量", align=2, sort=11)
	public Double getBeforeQty() {
		return beforeQty;
	}

	public void setBeforeQty(Double beforeQty) {
		this.beforeQty = beforeQty;
	}
	
	@ExcelField(title="操作数量", align=2, sort=12)
	public Double getOperQty() {
		return operQty;
	}

	public void setOperQty(Double operQty) {
		this.operQty = operQty;
	}
	
	@ExcelField(title="操作后数量", align=2, sort=13)
	public Double getAfterQty() {
		return afterQty;
	}

	public void setAfterQty(Double afterQty) {
		this.afterQty = afterQty;
	}
	
	@ExcelField(title="操作", align=2, sort=14)
	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}