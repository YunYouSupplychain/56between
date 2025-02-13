package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * tms异常处理单Entity
 * @author zyf
 * @version 2020-07-29
 */
public class TmExceptionHandleBill extends DataEntity<TmExceptionHandleBill> {

	private static final long serialVersionUID = 1L;
	private String billNo;					// 异常单号
	private String dispatchNo;				// 派车单号
	private String transportNo;				// 运输订单号
	private String customerNo;				// 客户订单号
	private String labelNo;					// 标签号
	private String outletCode;				// 网点编码
	private String billStatus;				// 订单状态
	private String carNo;					// 车牌号
	private String driver;					// 司机
	private String consigneeCode;			// 收货人编码
	private String consigneeName;			// 收货人名称
	private String consigneePhone;			// 收货人手机
	private String exceptionType;			// 异常类型
	private String exceptionReason;			// 异常原因
	private String registerPerson;			// 登记人
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTime;				// 登记时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date happenTime;				// 发生时间
	private String happenSysAreaId;			// 发生地点ID
	private String happenSysAreaCode;		// 发生地点编码
	private String happenSysAreaName;		// 发生地点名称
	private String responsibility;			// 责任方
	private String liabilityPerson;			// 责任人
	private String liabilityPersonCode;		// 责任人编码

	private String rpFlag;					// 应收应付
	private String unCode;					// 统一码
	private Double amount;					// 金额
	private Double receiptAmount;			// 应收金额
	private Double payAmount;				// 应付金额
	private String processNo;				// 流程单号
	private String feeProcessNo;			// 费用确认流程单号
	private String customerCode;			// 客户编码
	// 机构ID
	private String orgId;
	// 基础数据机构ID
	private String baseOrgId;

	public TmExceptionHandleBill() {
		super();
	}

	public TmExceptionHandleBill(String id){
		super(id);
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getDispatchNo() {
		return dispatchNo;
	}

	public void setDispatchNo(String dispatchNo) {
		this.dispatchNo = dispatchNo;
	}

	public String getTransportNo() {
		return transportNo;
	}

	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	public String getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public String getRegisterPerson() {
		return registerPerson;
	}

	public void setRegisterPerson(String registerPerson) {
		this.registerPerson = registerPerson;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}

	public String getHappenSysAreaId() {
		return happenSysAreaId;
	}

	public void setHappenSysAreaId(String happenSysAreaId) {
		this.happenSysAreaId = happenSysAreaId;
	}

	public String getHappenSysAreaCode() {
		return happenSysAreaCode;
	}

	public void setHappenSysAreaCode(String happenSysAreaCode) {
		this.happenSysAreaCode = happenSysAreaCode;
	}

	public String getHappenSysAreaName() {
		return happenSysAreaName;
	}

	public void setHappenSysAreaName(String happenSysAreaName) {
		this.happenSysAreaName = happenSysAreaName;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public String getLiabilityPerson() {
		return liabilityPerson;
	}

	public void setLiabilityPerson(String liabilityPerson) {
		this.liabilityPerson = liabilityPerson;
	}

	public String getLiabilityPersonCode() {
		return liabilityPersonCode;
	}

	public void setLiabilityPersonCode(String liabilityPersonCode) {
		this.liabilityPersonCode = liabilityPersonCode;
	}

	public String getRpFlag() {
		return rpFlag;
	}

	public void setRpFlag(String rpFlag) {
		this.rpFlag = rpFlag;
	}

	public String getUnCode() {
		return unCode;
	}

	public void setUnCode(String unCode) {
		this.unCode = unCode;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public String getProcessNo() {
		return processNo;
	}

	public void setProcessNo(String processNo) {
		this.processNo = processNo;
	}

	public String getFeeProcessNo() {
		return feeProcessNo;
	}

	public void setFeeProcessNo(String feeProcessNo) {
		this.feeProcessNo = feeProcessNo;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
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

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
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

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}
}