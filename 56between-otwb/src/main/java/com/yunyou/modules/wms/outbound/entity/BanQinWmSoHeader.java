package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 出库单Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmSoHeader extends DataEntity<BanQinWmSoHeader> {
	
	private static final long serialVersionUID = 1L;
	private String soNo;		// 出库单号
	private String logisticNo;		// 物流单号
	private String soType;		// 出库单类型
	private String priority;		// 优先级别
	private String status;		// 状态
	private String auditStatus;		// 审核状态
	private String auditOp;		// 审核人
	private Date auditTime;		// 审核时间
	private String holdStatus;		// 冻结状态
	private String holdOp;		// 冻结人
	private Date holdTime;		// 冻结时间
	private String interceptStatus;		// 拦截状态
	private Date interceptTime;		// 拦截时间
	private Date orderTime;		// 订单时间
	private Date fmEtd;		// 预期发货时间从
	private Date toEtd;		// 预期发货时间到
	private String trackingNo;		// 快递单号
	private String ldStatus;		// 装车状态
	private String ownerCode;		// 货主编码
	private String carrierCode;		// 承运人编码
	private String transType;		// 运输类型
	private String line;		// 路线
	private String stop;		// 站点
	private String vehicleNo;		// 车牌号
	private String driver;		// 驾驶员
	private String driverTel;		// 驾驶员电话
	private String consigneeCode;		// 收货人编码
	private String consigneeName;		// 收货人名称
	private String consigneeTel;		// 收货人电话
	private String consigneeAddr;		// 收货人地址
	private String consigneeZip;		// 收货人邮编
	private String consigneeEmail;		// 收货人Email
	private String consigneeFax;		// 收货人传真
	private String contactCode;		// 联系人编码
	private String contactName;		// 联系人名称
	private String contactTel;		// 联系人电话
	private String contactAddr;		// 联系人地址
	private String contactZip;		// 联系人邮编
	private String contactEmail;		// 联系人Email
	private String contactFax;		// 联系人传真
	private String settleCode;		// 结算人编码
	private Date ediSendTime;		// EDI发送时间
	private String isEdiSend;		// EDI是否已发送(放弃值Y/N,采用新的值WmsConstants.EDI_FLAG_??)
	private String def1;		// 自定义1(商流订单号)
	private String def2;		// 自定义2(供应链订单号)
	private String def3;		// 自定义3(作业任务号)
	private String def4;		// 自定义4(订单来源)
	private String def5;		// 自定义5(客户订单号)
	private String def6;		// 自定义6（渠道）
	private String def7;		// 自定义7（服务模式）
	private String def8;		// 自定义8（客户大类）
	private String def9;		// 自定义9（区域）
	private String def10;		// 自定义10（城市）
	private String def11;		// 自定义11（客户行业类型）
	private String def12;		// 自定义12（客户范围）
	private String def13;		// 自定义13（业务订单类型）
	private String def14;		// 自定义14（客户机构）
	private String def15;		// 自定义15
	private String def16;		// 自定义16
	private String def17;		// 自定义17（收货人区域地址）
	private String def18;		// 自定义18
	private String def19;		// 自定义19
	private String def20;		// 自定义20
	private String orgId;		// 分公司
	private String dataSource;	// 数据来源
	private String packStatus;  // 打包状态
	private String isPushed;    // 是否已下发
	
	public BanQinWmSoHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinWmSoHeader(String id){
		super(id);
	}

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}
	
	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}
	
	public String getSoType() {
		return soType;
	}

	public void setSoType(String soType) {
		this.soType = soType;
	}
	
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	public String getAuditOp() {
		return auditOp;
	}

	public void setAuditOp(String auditOp) {
		this.auditOp = auditOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	public String getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(String holdStatus) {
		this.holdStatus = holdStatus;
	}
	
	public String getHoldOp() {
		return holdOp;
	}

	public void setHoldOp(String holdOp) {
		this.holdOp = holdOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(Date holdTime) {
		this.holdTime = holdTime;
	}
	
	public String getInterceptStatus() {
		return interceptStatus;
	}

	public void setInterceptStatus(String interceptStatus) {
		this.interceptStatus = interceptStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInterceptTime() {
		return interceptTime;
	}

	public void setInterceptTime(Date interceptTime) {
		this.interceptTime = interceptTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFmEtd() {
		return fmEtd;
	}

	public void setFmEtd(Date fmEtd) {
		this.fmEtd = fmEtd;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getToEtd() {
		return toEtd;
	}

	public void setToEtd(Date toEtd) {
		this.toEtd = toEtd;
	}
	
	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
	
	public String getLdStatus() {
		return ldStatus;
	}

	public void setLdStatus(String ldStatus) {
		this.ldStatus = ldStatus;
	}
	
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}
	
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriverTel() {
		return driverTel;
	}

	public void setDriverTel(String driverTel) {
		this.driverTel = driverTel;
	}

	public String getConsigneeCode() {
		return consigneeCode;
	}

	public void setConsigneeCode(String consigneeCode) {
		this.consigneeCode = consigneeCode;
	}
	
	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	
	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}
	
	public String getConsigneeAddr() {
		return consigneeAddr;
	}

	public void setConsigneeAddr(String consigneeAddr) {
		this.consigneeAddr = consigneeAddr;
	}
	
	public String getConsigneeZip() {
		return consigneeZip;
	}

	public void setConsigneeZip(String consigneeZip) {
		this.consigneeZip = consigneeZip;
	}
	
	public String getConsigneeEmail() {
		return consigneeEmail;
	}

	public void setConsigneeEmail(String consigneeEmail) {
		this.consigneeEmail = consigneeEmail;
	}
	
	public String getConsigneeFax() {
		return consigneeFax;
	}

	public void setConsigneeFax(String consigneeFax) {
		this.consigneeFax = consigneeFax;
	}
	
	public String getContactCode() {
		return contactCode;
	}

	public void setContactCode(String contactCode) {
		this.contactCode = contactCode;
	}
	
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	
	public String getContactAddr() {
		return contactAddr;
	}

	public void setContactAddr(String contactAddr) {
		this.contactAddr = contactAddr;
	}
	
	public String getContactZip() {
		return contactZip;
	}

	public void setContactZip(String contactZip) {
		this.contactZip = contactZip;
	}
	
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	
	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}
	
	public String getSettleCode() {
		return settleCode;
	}

	public void setSettleCode(String settleCode) {
		this.settleCode = settleCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEdiSendTime() {
		return ediSendTime;
	}

	public void setEdiSendTime(Date ediSendTime) {
		this.ediSendTime = ediSendTime;
	}
	
	public String getIsEdiSend() {
		return isEdiSend;
	}

	public void setIsEdiSend(String isEdiSend) {
		this.isEdiSend = isEdiSend;
	}
	
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}
	
	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}
	
	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}
	
	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}
	
	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}
	
	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDef11() {
		return def11;
	}

	public void setDef11(String def11) {
		this.def11 = def11;
	}

	public String getDef12() {
		return def12;
	}

	public void setDef12(String def12) {
		this.def12 = def12;
	}

	public String getDef13() {
		return def13;
	}

	public void setDef13(String def13) {
		this.def13 = def13;
	}

	public String getDef14() {
		return def14;
	}

	public void setDef14(String def14) {
		this.def14 = def14;
	}

	public String getDef15() {
		return def15;
	}

	public void setDef15(String def15) {
		this.def15 = def15;
	}

	public String getDef16() {
		return def16;
	}

	public void setDef16(String def16) {
		this.def16 = def16;
	}

	public String getDef17() {
		return def17;
	}

	public void setDef17(String def17) {
		this.def17 = def17;
	}

	public String getDef18() {
		return def18;
	}

	public void setDef18(String def18) {
		this.def18 = def18;
	}

	public String getDef19() {
		return def19;
	}

	public void setDef19(String def19) {
		this.def19 = def19;
	}

	public String getDef20() {
		return def20;
	}

	public void setDef20(String def20) {
		this.def20 = def20;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getPackStatus() {
		return packStatus;
	}

	public void setPackStatus(String packStatus) {
		this.packStatus = packStatus;
	}

	public String getIsPushed() {
		return isPushed;
	}

	public void setIsPushed(String isPushed) {
		this.isPushed = isPushed;
	}
}