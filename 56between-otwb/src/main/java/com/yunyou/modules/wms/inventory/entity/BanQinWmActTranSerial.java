package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 库存序列号交易Entity
 * @author WMJ
 * @version 2019-01-26
 */
public class BanQinWmActTranSerial extends DataEntity<BanQinWmActTranSerial> {
	
	private static final long serialVersionUID = 1L;
	private String serialTranId;		// 序列号交易ID
	private String serialTranType;		// 交易类型
	private String ownerCode;		// 货主
	private String skuCode;		// 商品
	private String serialNo;		// 序列号
	private String orderType;		// 单据类型
	private String orderNo;		// 单据号
	private String lineNo;		// 行号
	private String lotNum;		// 批次号
	private String tranId;		// 库存交易ID
	private String tranOp;		// 操作人
	private Date tranTime;		// 交易时间
	private String orgId;		// 分公司
	
	public BanQinWmActTranSerial() {
		super();
		this.recVer = 0;
	}

	public BanQinWmActTranSerial(String id){
		super(id);
	}

	@ExcelField(title="序列号交易ID", align=2, sort=2)
	public String getSerialTranId() {
		return serialTranId;
	}

	public void setSerialTranId(String serialTranId) {
		this.serialTranId = serialTranId;
	}
	
	@ExcelField(title="交易类型", align=2, sort=3)
	public String getSerialTranType() {
		return serialTranType;
	}

	public void setSerialTranType(String serialTranType) {
		this.serialTranType = serialTranType;
	}
	
	@ExcelField(title="货主", align=2, sort=4)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品", align=2, sort=5)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="序列号", align=2, sort=6)
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	@ExcelField(title="单据类型", align=2, sort=7)
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@ExcelField(title="单据号", align=2, sort=8)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="行号", align=2, sort=9)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="批次号", align=2, sort=10)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="库存交易ID", align=2, sort=11)
	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
	
	@ExcelField(title="操作人", align=2, sort=12)
	public String getTranOp() {
		return tranOp;
	}

	public void setTranOp(String tranOp) {
		this.tranOp = tranOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="交易时间", align=2, sort=13)
	public Date getTranTime() {
		return tranTime;
	}

	public void setTranTime(Date tranTime) {
		this.tranTime = tranTime;
	}

	@ExcelField(title="分公司", align=2, sort=21)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}