package com.yunyou.modules.wms.outbound.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 预配明细Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmSoPrealloc extends DataEntity<BanQinWmSoPrealloc> {
	
	private static final long serialVersionUID = 1L;
	private String preallocId;		// 预配ID
	private String waveNo;		// 波次单号
	private String soNo;		// 出库单号
	private String lineNo;		// 行号
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 批次号
	private String packCode;		// 包装编码
	private String uom;		// 包装单位
	private Double qtyPreallocUom;		// 预配数
	private Double qtyPreallocEa;		// 预配数EA
	private String orgId;		// 分公司
	
	public BanQinWmSoPrealloc() {
		super();
		this.recVer = 0;
	}

	public BanQinWmSoPrealloc(String id){
		super(id);
	}

	@ExcelField(title="预配ID", align=2, sort=2)
	public String getPreallocId() {
		return preallocId;
	}

	public void setPreallocId(String preallocId) {
		this.preallocId = preallocId;
	}
	
	@ExcelField(title="波次单号", align=2, sort=3)
	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}
	
	@ExcelField(title="出库单号", align=2, sort=4)
	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}
	
	@ExcelField(title="行号", align=2, sort=5)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="货主编码", align=2, sort=6)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=7)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=8)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="包装编码", align=2, sort=9)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=10)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="预配数", align=2, sort=11)
	public Double getQtyPreallocUom() {
		return qtyPreallocUom;
	}

	public void setQtyPreallocUom(Double qtyPreallocUom) {
		this.qtyPreallocUom = qtyPreallocUom;
	}
	
	@ExcelField(title="预配数EA", align=2, sort=12)
	public Double getQtyPreallocEa() {
		return qtyPreallocEa;
	}

	public void setQtyPreallocEa(Double qtyPreallocEa) {
		this.qtyPreallocEa = qtyPreallocEa;
	}

	@ExcelField(title="分公司", align=2, sort=20)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}