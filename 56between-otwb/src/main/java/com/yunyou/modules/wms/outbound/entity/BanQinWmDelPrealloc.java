package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 取消预配记录Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmDelPrealloc extends DataEntity<BanQinWmDelPrealloc> {
	
	private static final long serialVersionUID = 1L;
	private String preallocId;		// 预配ID
	private String preallocSeq;		// 取消预配流水号
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
	private String op;		// 操作人
	private Date opTime;		// 操作时间
	private String orgId;		// 分公司
	
	public BanQinWmDelPrealloc() {
		super();
		this.recVer = 0;
	}

	public BanQinWmDelPrealloc(String id){
		super(id);
	}

	@ExcelField(title="预配ID", align=2, sort=2)
	public String getPreallocId() {
		return preallocId;
	}

	public void setPreallocId(String preallocId) {
		this.preallocId = preallocId;
	}
	
	@ExcelField(title="取消预配流水号", align=2, sort=3)
	public String getPreallocSeq() {
		return preallocSeq;
	}

	public void setPreallocSeq(String preallocSeq) {
		this.preallocSeq = preallocSeq;
	}
	
	@ExcelField(title="波次单号", align=2, sort=4)
	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}
	
	@ExcelField(title="出库单号", align=2, sort=5)
	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}
	
	@ExcelField(title="行号", align=2, sort=6)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="货主编码", align=2, sort=7)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=8)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=9)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="包装编码", align=2, sort=10)
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	
	@ExcelField(title="包装单位", align=2, sort=11)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="预配数", align=2, sort=12)
	public Double getQtyPreallocUom() {
		return qtyPreallocUom;
	}

	public void setQtyPreallocUom(Double qtyPreallocUom) {
		this.qtyPreallocUom = qtyPreallocUom;
	}
	
	@ExcelField(title="预配数EA", align=2, sort=13)
	public Double getQtyPreallocEa() {
		return qtyPreallocEa;
	}

	public void setQtyPreallocEa(Double qtyPreallocEa) {
		this.qtyPreallocEa = qtyPreallocEa;
	}
	
	@ExcelField(title="操作人", align=2, sort=14)
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="操作时间", align=2, sort=15)
	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	@ExcelField(title="分公司", align=2, sort=23)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}