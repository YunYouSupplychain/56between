package com.yunyou.modules.wms.qc.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 质检单Entity
 * @author WMJ
 * @version 2019-01-26
 */
public class BanQinWmQcHeader extends DataEntity<BanQinWmQcHeader> {
	
	private static final long serialVersionUID = 1L;
	private String qcNo;		// 质检单号
	private String qcPhase;		// 质检阶段
	private String status;		// 状态(创建、部分质检、完全质检、关闭、取消)
	private String auditStatus;		// 审核状态
	private String ownerCode;		// 货主编码
	private String priority;		// 优先级别
	private String orderType;		// 单据类型
	private String orderNo;		// 单据号
	private Date orderTime;		// 订单时间
	private Date fmEtq;		// 预计质检时间从
	private Date toEtq;		// 预计质检时间到
	private String auditOp;		// 审核人
	private Date auditTime;		// 审核时间
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司
	
	public BanQinWmQcHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinWmQcHeader(String id){
		super(id);
	}

	@ExcelField(title="质检单号", align=2, sort=2)
	public String getQcNo() {
		return qcNo;
	}

	public void setQcNo(String qcNo) {
		this.qcNo = qcNo;
	}
	
	@ExcelField(title="质检阶段", align=2, sort=3)
	public String getQcPhase() {
		return qcPhase;
	}

	public void setQcPhase(String qcPhase) {
		this.qcPhase = qcPhase;
	}
	
	@ExcelField(title="状态(创建、部分质检、完全质检、关闭、取消)", dictType="", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="审核状态", align=2, sort=5)
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@ExcelField(title="货主编码", align=2, sort=6)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="优先级别", dictType="", align=2, sort=7)
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	@ExcelField(title="单据类型", dictType="", align=2, sort=8)
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@ExcelField(title="单据号", align=2, sort=9)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="订单时间", align=2, sort=10)
	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="预计质检时间从", align=2, sort=11)
	public Date getFmEtq() {
		return fmEtq;
	}

	public void setFmEtq(Date fmEtq) {
		this.fmEtq = fmEtq;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="预计质检时间到", align=2, sort=12)
	public Date getToEtq() {
		return toEtq;
	}

	public void setToEtq(Date toEtq) {
		this.toEtq = toEtq;
	}
	
	@ExcelField(title="审核人", align=2, sort=13)
	public String getAuditOp() {
		return auditOp;
	}

	public void setAuditOp(String auditOp) {
		this.auditOp = auditOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=14)
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	@ExcelField(title="自定义1", align=2, sort=15)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=16)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=17)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=18)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=19)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="分公司", align=2, sort=28)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}