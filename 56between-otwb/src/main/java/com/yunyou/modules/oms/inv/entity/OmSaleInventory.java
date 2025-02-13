package com.yunyou.modules.oms.inv.entity;

import javax.validation.constraints.NotNull;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 销售库存Entity
 * @author Jianhua Liu
 * @version 2019-05-09
 */
public class OmSaleInventory extends DataEntity<OmSaleInventory> {
	
	private static final long serialVersionUID = 1L;
	private String warehouse;	// 仓库
	private String owner;		// 货主
	private String skuCode;		// 商品
	private Double allocQty;	// 分配数量
	private Double shipmentQty;	// 发运数量
	private String orgId;		// 平台ID
	
	public OmSaleInventory() {
		super();
	}

	public OmSaleInventory(String id){
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
	
	@NotNull(message="分配数量不能为空")
	@ExcelField(title="分配数量", align=2, sort=10)
	public Double getAllocQty() {
		return allocQty;
	}

	public void setAllocQty(Double allocQty) {
		this.allocQty = allocQty;
	}
	
	@ExcelField(title="发运数量", align=2, sort=11)
	public Double getShipmentQty() {
		return shipmentQty;
	}

	public void setShipmentQty(Double shipmentQty) {
		this.shipmentQty = shipmentQty;
	}

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}