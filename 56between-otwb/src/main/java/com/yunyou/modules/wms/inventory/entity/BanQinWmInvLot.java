package com.yunyou.modules.wms.inventory.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 批次库存表Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmInvLot extends DataEntity<BanQinWmInvLot> {
	
	private static final long serialVersionUID = 1L;
	private String lotNum;		// 批次号
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private Double qty;		// 库存数
	private Double qtyHold;		// 冻结数
	private Double qtyPrealloc;		// 预配数
	private Double qtyAlloc;		// 分配数
	private Double qtyPk;		// 已拣货数
	private String orgId;		// 分公司
    
    private String isQc; // 只是用作查询
	
	public BanQinWmInvLot() {
		super();
        qty = 0d;
        qtyHold = 0d;
        qtyPrealloc = 0d;
        qtyAlloc = 0d;
        qtyPk = 0d;
		recVer = 0;
	}

	public BanQinWmInvLot(String id){
		super(id);
	}

	@ExcelField(title="批次号", align=2, sort=2)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="货主编码", align=2, sort=3)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=4)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="库存数", align=2, sort=5)
	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}
	
	@ExcelField(title="冻结数", align=2, sort=6)
	public Double getQtyHold() {
		return qtyHold;
	}

	public void setQtyHold(Double qtyHold) {
		this.qtyHold = qtyHold;
	}
	
	@ExcelField(title="预配数", align=2, sort=7)
	public Double getQtyPrealloc() {
		return qtyPrealloc;
	}

	public void setQtyPrealloc(Double qtyPrealloc) {
		this.qtyPrealloc = qtyPrealloc;
	}
	
	@ExcelField(title="分配数", align=2, sort=8)
	public Double getQtyAlloc() {
		return qtyAlloc;
	}

	public void setQtyAlloc(Double qtyAlloc) {
		this.qtyAlloc = qtyAlloc;
	}
	
	@ExcelField(title="已拣货数", align=2, sort=9)
	public Double getQtyPk() {
		return qtyPk;
	}

	public void setQtyPk(Double qtyPk) {
		this.qtyPk = qtyPk;
	}

	@ExcelField(title="分公司", align=2, sort=17)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    public String getIsQc() {
        return isQc;
    }

    public void setIsQc(String isQc) {
        this.isQc = isQc;
    }
}