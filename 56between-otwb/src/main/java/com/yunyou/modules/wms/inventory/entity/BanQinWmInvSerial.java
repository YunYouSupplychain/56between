package com.yunyou.modules.wms.inventory.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 序列号库存表Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmInvSerial extends DataEntity<BanQinWmInvSerial> {
	
	private static final long serialVersionUID = 1L;
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String serialNo;		// 序列号
	private String lotNum;		// 批次号
	private String orgId;		// 分公司
	
	public BanQinWmInvSerial() {
		super();
	}

	public BanQinWmInvSerial(String id){
		super(id);
	}

	@ExcelField(title="货主编码", align=2, sort=2)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=3)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="序列号", align=2, sort=4)
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	@ExcelField(title="批次号", align=2, sort=5)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	@ExcelField(title="分公司", align=2, sort=13)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}