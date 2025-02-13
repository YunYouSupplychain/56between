package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 装车单明细Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmLdDetail extends DataEntity<BanQinWmLdDetail> {
	
	private static final long serialVersionUID = 1L;
	private String ldNo;		// 装车单号
	private String lineNo;		// 装车单行号
	private String status;		// 状态
	private String allocId;		// 分配ID
	private String ldOp;		// 装车人
	private Date ldTime;		// 装车时间
	private String orgId;		// 分公司
	
	public BanQinWmLdDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinWmLdDetail(String id){
		super(id);
	}

	@ExcelField(title="装车单号", align=2, sort=2)
	public String getLdNo() {
		return ldNo;
	}

	public void setLdNo(String ldNo) {
		this.ldNo = ldNo;
	}
	
	@ExcelField(title="装车单行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="状态", dictType="", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="分配ID", align=2, sort=5)
	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}
	
	@ExcelField(title="装车人", align=2, sort=6)
	public String getLdOp() {
		return ldOp;
	}

	public void setLdOp(String ldOp) {
		this.ldOp = ldOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="装车时间", align=2, sort=7)
	public Date getLdTime() {
		return ldTime;
	}

	public void setLdTime(Date ldTime) {
		this.ldTime = ldTime;
	}

	@ExcelField(title="分公司", align=2, sort=15)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}