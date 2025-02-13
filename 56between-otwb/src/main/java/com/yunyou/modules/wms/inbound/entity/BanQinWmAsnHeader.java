package com.yunyou.modules.wms.inbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 入库单Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmAsnHeader extends DataEntity<BanQinWmAsnHeader> {
	
	private static final long serialVersionUID = 1L;
	private String asnNo;		// 入库单号
	private String logisticNo;		// 物流单号
	private String asnType;		// 入库单类型
	private Date orderTime;		// 订单时间
	private Date fmEta;		// 预计到货时间从
	private Date toEta;		// 预计到货时间到
	private String priority;		// 优先级别
	private String status;		// 订单状态
	private String auditStatus;		// 审核状态
	private String auditOp;		// 审核人
	private Date auditTime;		// 审核时间
	private String holdStatus;		// 冻结状态
	private String holdOp;		// 冻结人
	private Date holdTime;		// 冻结时间
	private String qcStatus;		// 质检状态（创建、部分质检、完全质检）
	private String ownerCode;		// 货主编码
	private String supplierCode;		// 供应商编码
	private String carrierCode;		// 承运商编码
	private String settleCode;		// 结算人编码
	private Long printNum;		// 打印次数
	private String isAppointDock;		// 是否已预约月台
	private Date ediSendTime;		// EDI发送时间
	private String isEdiSend;		// EDI是否已发送(放弃值Y/N,采用新的值WmsConstants.EDI_FLAG_??)
	private String def1;		// 自定义1（商流订单号）
	private String def2;		// 自定义2（供应链订单号）
	private String def3;		// 自定义3（作业任务号）
	private String def4;		// 自定义4（客户订单号）
	private String def5;		// 自定义5（订单来源）
	private String def6;		// 自定义6
	private String def7;		// 自定义7
	private String def8;		// 自定义8
	private String def9;		// 自定义9
	private String def10;		// 自定义10（业务订单类型）
	private String def11;		// 自定义11
	private String def12;		// 自定义12
	private String def13;		// 自定义13
	private String def14;		// 自定义14（客户机构）
	private String def15;		// 自定义15
	private String def16;		// 自定义16
	private String def17;		// 自定义17
	private String def18;		// 自定义18
	private String def19;		// 自定义19
	private String def20;		// 自定义20
	private String orgId;		// 分公司
	private Double freightAmount; // 运费金额
	private String driver;		// 司机
	private String vehicleNo;	// 车牌号
	private String tel;			// 电话
	private String dataSource;  // 数据来源

	public BanQinWmAsnHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinWmAsnHeader(String id){
		super(id);
	}

	public String getAsnNo() {
		return asnNo;
	}

	public void setAsnNo(String asnNo) {
		this.asnNo = asnNo;
	}
	
	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}
	
	public String getAsnType() {
		return asnType;
	}

	public void setAsnType(String asnType) {
		this.asnType = asnType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFmEta() {
		return fmEta;
	}

	public void setFmEta(Date fmEta) {
		this.fmEta = fmEta;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getToEta() {
		return toEta;
	}

	public void setToEta(Date toEta) {
		this.toEta = toEta;
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
	
	public String getQcStatus() {
		return qcStatus;
	}

	public void setQcStatus(String qcStatus) {
		this.qcStatus = qcStatus;
	}
	
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	public String getSettleCode() {
		return settleCode;
	}

	public void setSettleCode(String settleCode) {
		this.settleCode = settleCode;
	}
	
	public Long getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Long printNum) {
		this.printNum = printNum;
	}
	
	public String getIsAppointDock() {
		return isAppointDock;
	}

	public void setIsAppointDock(String isAppointDock) {
		this.isAppointDock = isAppointDock;
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

	public Double getFreightAmount() {
		return freightAmount;
	}

	public void setFreightAmount(Double freightAmount) {
		this.freightAmount = freightAmount;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
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
}