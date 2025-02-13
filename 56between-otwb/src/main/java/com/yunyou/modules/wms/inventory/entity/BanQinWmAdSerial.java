package com.yunyou.modules.wms.inventory.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 序列号调整Entity
 * @author WMJ
 * @version 2019-01-28
 */
public class BanQinWmAdSerial extends DataEntity<BanQinWmAdSerial> {
	
	private static final long serialVersionUID = 1L;
	private String adNo;		// 调整单号
	private String skuCode;		// 商品编码
	private String serialNo;		// 序列号
	private String adMode;		// 调整方式（A 增加、D减少）
	private String ownerCode;		// 货主编码
	private String lotNum;		// 批次号
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司
	private String headerId;		// 表头Id
	
	public BanQinWmAdSerial() {
		super();
		this.recVer = 0;
	}

	public BanQinWmAdSerial(String id){
		super(id);
	}

	@ExcelField(title="调整单号", align=2, sort=2)
	public String getAdNo() {
		return adNo;
	}

	public void setAdNo(String adNo) {
		this.adNo = adNo;
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
	
	@ExcelField(title="调整方式（A 增加、D减少）", align=2, sort=5)
	public String getAdMode() {
		return adMode;
	}

	public void setAdMode(String adMode) {
		this.adMode = adMode;
	}
	
	@ExcelField(title="货主编码", align=2, sort=6)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=7)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="自定义1", align=2, sort=8)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=9)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=10)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=11)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=12)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="分公司", align=2, sort=21)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="表头Id", align=2, sort=22)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
	
}