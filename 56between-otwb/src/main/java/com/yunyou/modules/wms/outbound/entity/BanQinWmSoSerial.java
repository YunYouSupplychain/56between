package com.yunyou.modules.wms.outbound.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 出库序列号Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmSoSerial extends DataEntity<BanQinWmSoSerial> {
	
	private static final long serialVersionUID = 1L;
	private String soNo;		// 出库单号
	private String skuCode;		// 商品编码
	private String serialNo;		// 序列号
	private String lineNo;		// 行号
	private String allocId;		// 分配ID
	private String ownerCode;		// 货主编码
	private String lotNum;		// 批次号
	private String orgId;		// 分公司
	
	public BanQinWmSoSerial() {
		super();
		this.recVer = 0;
	}

	public BanQinWmSoSerial(String id){
		super(id);
	}

	@ExcelField(title="出库单号", align=2, sort=2)
	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
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
	
	@ExcelField(title="行号", align=2, sort=5)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="分配ID", align=2, sort=6)
	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}
	
	@ExcelField(title="货主编码", align=2, sort=7)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=8)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	@ExcelField(title="分公司", align=2, sort=16)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}