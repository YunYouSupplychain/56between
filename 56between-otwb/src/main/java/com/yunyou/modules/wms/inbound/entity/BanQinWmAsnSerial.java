package com.yunyou.modules.wms.inbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 入库序列Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmAsnSerial extends DataEntity<BanQinWmAsnSerial> {
	
	private static final long serialVersionUID = 1L;
	private String asnNo;		// 入库单号
	private String skuCode;		// 商品编码
	private String serialNo;		// 序列号
	private String status;		// 状态:计划未扫描，计划已扫描，无计划已扫描
	private String rcvLineNo;		// 收货明细行号
	private String ownerCode;		// 货主编码
	private String lotNum;		// 批次号
	private String dataSource;		// 数据来源
	private String scanOp;		// 扫描人
	private Date scanTime;		// 扫描时间
	private String orgId;		// 分公司
	private String headId;		// 入库单ID
	
	public BanQinWmAsnSerial() {
		super();
		this.recVer = 0;
	}

	public BanQinWmAsnSerial(String id){
		super(id);
	}

	public String getAsnNo() {
		return asnNo;
	}

	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
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
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRcvLineNo() {
		return rcvLineNo;
	}

	public void setRcvLineNo(String rcvLineNo) {
		this.rcvLineNo = rcvLineNo;
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
	
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	public String getScanOp() {
		return scanOp;
	}

	public void setScanOp(String scanOp) {
		this.scanOp = scanOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getScanTime() {
		return scanTime;
	}

	public void setScanTime(Date scanTime) {
		this.scanTime = scanTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}
	
}