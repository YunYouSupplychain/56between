package com.yunyou.modules.wms.kit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 加工单Entity
 * @author Jianhua Liu
 * @version 2019-08-20
 */
public class BanQinWmKitHeader extends DataEntity<BanQinWmKitHeader> {
	
	private static final long serialVersionUID = 1L;
	private String kitNo;		// 加工单号
	private String kitType;		// 加工类型
	private String status;		// 状态
	private String auditStatus;		// 审核状态
	private String logisticNo;		// 物流单号
	private String ownerCode;		// 货主编码
	private Date fmEtk;		// 预计加工时间从
	private Date toEtk;		// 预计加工时间到
	private String kitLoc;		// 计划加工台
	private String auditOp;		// 审核人
	private Date auditTime;		// 审核时间
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 机构ID
	
	public BanQinWmKitHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinWmKitHeader(String id){
		super(id);
	}

	@ExcelField(title="加工单号", align=2, sort=2)
	public String getKitNo() {
		return kitNo;
	}

	public void setKitNo(String kitNo) {
		this.kitNo = kitNo;
	}
	
	@ExcelField(title="加工类型", dictType="SYS_WM_KIT_TYPE", align=2, sort=3)
	public String getKitType() {
		return kitType;
	}

	public void setKitType(String kitType) {
		this.kitType = kitType;
	}
	
	@ExcelField(title="状态", dictType="SYS_WM_KIT_STATUS", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="审核状态", dictType="SYS_AUDIT_STATUS", align=2, sort=5)
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@ExcelField(title="物流单号", align=2, sort=6)
	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}
	
	@ExcelField(title="货主编码", align=2, sort=7)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="预计加工时间从", align=2, sort=8)
	public Date getFmEtk() {
		return fmEtk;
	}

	public void setFmEtk(Date fmEtk) {
		this.fmEtk = fmEtk;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="预计加工时间到", align=2, sort=9)
	public Date getToEtk() {
		return toEtk;
	}

	public void setToEtk(Date toEtk) {
		this.toEtk = toEtk;
	}
	
	@ExcelField(title="计划加工台", align=2, sort=10)
	public String getKitLoc() {
		return kitLoc;
	}

	public void setKitLoc(String kitLoc) {
		this.kitLoc = kitLoc;
	}
	
	@ExcelField(title="审核人", align=2, sort=11)
	public String getAuditOp() {
		return auditOp;
	}

	public void setAuditOp(String auditOp) {
		this.auditOp = auditOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=12)
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	@ExcelField(title="自定义1", align=2, sort=13)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=14)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=15)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=16)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=17)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="机构ID", align=2, sort=26)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}