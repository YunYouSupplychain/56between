package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 异常处理单明细Entity
 * @author zyf
 * @version 2020-07-29
 */
public class TmExceptionHandleBillDetail extends DataEntity<TmExceptionHandleBillDetail> {

	private static final long serialVersionUID = 1L;
	private String billNo;						// 异常处理单号
	private String handleUser;					// 异常处理人
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date handleTime;					// 异常处理时间
	private String handleDescription;			// 异常处理描述
	// 机构ID
	private String orgId;
	// 基础数据机构ID
	private String baseOrgId;
	// 版本号
	private Long recVer = 0L;

	public TmExceptionHandleBillDetail() {
		super();
	}

	public TmExceptionHandleBillDetail(String id){
		super(id);
	}

	public TmExceptionHandleBillDetail(String billNo, String orgId) {
		this.billNo = billNo;
		this.orgId = orgId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getHandleUser() {
		return handleUser;
	}

	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandleDescription() {
		return handleDescription;
	}

	public void setHandleDescription(String handleDescription) {
		this.handleDescription = handleDescription;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getBaseOrgId() {
		return baseOrgId;
	}

	public void setBaseOrgId(String baseOrgId) {
		this.baseOrgId = baseOrgId;
	}
}