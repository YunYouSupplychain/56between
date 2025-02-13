package com.yunyou.modules.wms.outbound.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 打包序列号Entity
 * @author WMJ
 * @version 2020-05-13
 */
public class BanQinWmPackSerial extends DataEntity<BanQinWmPackSerial> {

	private static final long serialVersionUID = 1L;
	private String soNo;		// 出库单号
	private String skuCode;		// 商品编码
	private String serialNo;	// 序列号
	private String lineNo;		// 行号
	private String allocId;		// 分配ID
	private String ownerCode;	// 货主编码
	private String lotNum;		// 批次号
	private String orgId;		// 分公司

	public BanQinWmPackSerial() {
		super();
		this.recVer = 0;
	}

	public BanQinWmPackSerial(String id){
		super(id);
	}

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}
	
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}
	
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}