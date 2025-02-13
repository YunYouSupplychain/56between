package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 商品条码Entity
 * @author WMJ
 * @version 2019-10-28
 */
public class BanQinCdWhSkuBarcode extends DataEntity<BanQinCdWhSkuBarcode> {

	private static final long serialVersionUID = 1L;
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String barcode;		// 条码
	private String orgId;		// 分公司
	private String isDefault;		// 是否默认
	private String headerId;		// 商品Id

	public BanQinCdWhSkuBarcode() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhSkuBarcode(String id){
		super(id);
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
}