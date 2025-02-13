package com.yunyou.modules.wms.outbound.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 销售单Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmSaleHeader extends DataEntity<BanQinWmSaleHeader> {
	
	private static final long serialVersionUID = 1L;
	private String saleNo;		// 销售单号
	private String logisticNo;		// 物流单号
	private String saleType;		// 销售单类型
	private String priority;		// 优先级别
	private String status;		// 状态
	private String auditStatus;		// 审核状态
	private String auditOp;		// 审核人
	private Date auditTime;		// 审核时间
	private Date orderTime;		// 订单创建时间
	private Date fmEtd;		// 预期发货时间从
	private Date toEtd;		// 预期发货时间到
	private String ownerCode;		// 货主编码
	private String carrierCode;		// 承运人编码
	private String transType;		// 运输类型
	private String line;		// 路线
	private String stop;		// 站点
	private String vehicleNo;		// 车牌号
	private String driver;		// 驾驶员
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
	private String isEdiSend;		// EDI是否已发送
	private String def1;		// 自定义1
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String def6;		// 自定义6
	private String def7;		// 自定义7
	private String def8;		// 自定义8
	private String def9;		// 自定义9
	private String def10;		// 自定义10
	private String orgId;		// 分公司
	
	public BanQinWmSaleHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinWmSaleHeader(String id){
		super(id);
	}

	@ExcelField(title="销售单号", align=2, sort=2)
	public String getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}
	
	@ExcelField(title="物流单号", align=2, sort=3)
	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}
	
	@ExcelField(title="销售单类型", dictType="", align=2, sort=4)
	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	
	@ExcelField(title="优先级别", dictType="", align=2, sort=5)
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	@ExcelField(title="状态", dictType="", align=2, sort=6)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="审核状态", dictType="", align=2, sort=7)
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@ExcelField(title="审核人", align=2, sort=8)
	public String getAuditOp() {
		return auditOp;
	}

	public void setAuditOp(String auditOp) {
		this.auditOp = auditOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=9)
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="订单创建时间", align=2, sort=10)
	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="预期发货时间从", align=2, sort=11)
	public Date getFmEtd() {
		return fmEtd;
	}

	public void setFmEtd(Date fmEtd) {
		this.fmEtd = fmEtd;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="预期发货时间到", align=2, sort=12)
	public Date getToEtd() {
		return toEtd;
	}

	public void setToEtd(Date toEtd) {
		this.toEtd = toEtd;
	}
	
	@ExcelField(title="货主编码", align=2, sort=13)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="承运人编码", align=2, sort=14)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	@ExcelField(title="运输类型", dictType="", align=2, sort=15)
	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	@ExcelField(title="路线", align=2, sort=16)
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	@ExcelField(title="站点", align=2, sort=17)
	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}
	
	@ExcelField(title="车牌号", align=2, sort=18)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="驾驶员", align=2, sort=19)
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	@ExcelField(title="收货人编码", align=2, sort=20)
	public String getConsigneeCode() {
		return consigneeCode;
	}

	public void setConsigneeCode(String consigneeCode) {
		this.consigneeCode = consigneeCode;
	}
	
	@ExcelField(title="收货人名称", align=2, sort=21)
	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	
	@ExcelField(title="收货人电话", align=2, sort=22)
	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}
	
	@ExcelField(title="收货人地址", align=2, sort=23)
	public String getConsigneeAddr() {
		return consigneeAddr;
	}

	public void setConsigneeAddr(String consigneeAddr) {
		this.consigneeAddr = consigneeAddr;
	}
	
	@ExcelField(title="收货人邮编", align=2, sort=24)
	public String getConsigneeZip() {
		return consigneeZip;
	}

	public void setConsigneeZip(String consigneeZip) {
		this.consigneeZip = consigneeZip;
	}
	
	@ExcelField(title="收货人Email", align=2, sort=25)
	public String getConsigneeEmail() {
		return consigneeEmail;
	}

	public void setConsigneeEmail(String consigneeEmail) {
		this.consigneeEmail = consigneeEmail;
	}
	
	@ExcelField(title="收货人传真", align=2, sort=26)
	public String getConsigneeFax() {
		return consigneeFax;
	}

	public void setConsigneeFax(String consigneeFax) {
		this.consigneeFax = consigneeFax;
	}
	
	@ExcelField(title="联系人编码", align=2, sort=27)
	public String getContactCode() {
		return contactCode;
	}

	public void setContactCode(String contactCode) {
		this.contactCode = contactCode;
	}
	
	@ExcelField(title="联系人名称", align=2, sort=28)
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@ExcelField(title="联系人电话", align=2, sort=29)
	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	
	@ExcelField(title="联系人地址", align=2, sort=30)
	public String getContactAddr() {
		return contactAddr;
	}

	public void setContactAddr(String contactAddr) {
		this.contactAddr = contactAddr;
	}
	
	@ExcelField(title="联系人邮编", align=2, sort=31)
	public String getContactZip() {
		return contactZip;
	}

	public void setContactZip(String contactZip) {
		this.contactZip = contactZip;
	}
	
	@ExcelField(title="联系人Email", align=2, sort=32)
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	
	@ExcelField(title="联系人传真", align=2, sort=33)
	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}
	
	@ExcelField(title="结算人编码", align=2, sort=34)
	public String getSettleCode() {
		return settleCode;
	}

	public void setSettleCode(String settleCode) {
		this.settleCode = settleCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="EDI发送时间", align=2, sort=35)
	public Date getEdiSendTime() {
		return ediSendTime;
	}

	public void setEdiSendTime(Date ediSendTime) {
		this.ediSendTime = ediSendTime;
	}
	
	@ExcelField(title="EDI是否已发送", dictType="", align=2, sort=36)
	public String getIsEdiSend() {
		return isEdiSend;
	}

	public void setIsEdiSend(String isEdiSend) {
		this.isEdiSend = isEdiSend;
	}
	
	@ExcelField(title="自定义1", align=2, sort=37)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=38)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=39)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=40)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=41)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}
	
	@ExcelField(title="自定义6", align=2, sort=42)
	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}
	
	@ExcelField(title="自定义7", align=2, sort=43)
	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}
	
	@ExcelField(title="自定义8", align=2, sort=44)
	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}
	
	@ExcelField(title="自定义9", align=2, sort=45)
	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}
	
	@ExcelField(title="自定义10", align=2, sort=46)
	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	@ExcelField(title="分公司", align=2, sort=55)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}