package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 装车单Entity
 * @author WMJ
 * @version 2019-02-14
 */
public class BanQinWmLdHeader extends DataEntity<BanQinWmLdHeader> {
	
	private static final long serialVersionUID = 1L;
	private String ldNo;		// 装车单号
	private String status;		// 装车单状态
	private String ldType;		// 装车单类型
	private String soStatus;		// 出库单状态
	private String vehicleNo;		// 车牌号
	private String vehicleType;		// 车型
	private String driver;		// 驾驶员
	private Date fmLdTime;		// 装车时间从
	private Date toLdTime;		// 装车时间到
	private String deliverOp;		// 装车交接人
	private Date deliverTime;		// 装车交接时间
	private String carrierCode;		// 承运商编码
	private String line;		// 路线
	private String consigneeCode;		// 收货人编码
	private String consigneeName;		// 收货人名称
	private String consigneeTel;		// 收货人电话
	private String consigneeAddr;		// 收货人地址
	private String contactName;		// 联系人名称
	private String contactTel;		// 联系人电话
	private String contactAddr;		// 联系人地址
	private String isAppointDock;		// 是否已预约月台
	private String def1;		// 自定义1	(是否已生成审单数据)
	private String def2;		// 自定义2
	private String def3;		// 自定义3
	private String def4;		// 自定义4
	private String def5;		// 自定义5
	private String orgId;		// 分公司
	
	public BanQinWmLdHeader() {
		super();
		this.recVer = 0;
	}

	public BanQinWmLdHeader(String id){
		super(id);
	}

	@ExcelField(title="装车单号", align=2, sort=2)
	public String getLdNo() {
		return ldNo;
	}

	public void setLdNo(String ldNo) {
		this.ldNo = ldNo;
	}
	
	@ExcelField(title="装车单状态", dictType="", align=2, sort=3)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="装车单类型", dictType="", align=2, sort=4)
	public String getLdType() {
		return ldType;
	}

	public void setLdType(String ldType) {
		this.ldType = ldType;
	}
	
	@ExcelField(title="出库单状态", dictType="", align=2, sort=5)
	public String getSoStatus() {
		return soStatus;
	}

	public void setSoStatus(String soStatus) {
		this.soStatus = soStatus;
	}
	
	@ExcelField(title="车牌号", align=2, sort=6)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="车型", align=2, sort=7)
	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	@ExcelField(title="驾驶员", align=2, sort=8)
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="装车时间从", align=2, sort=9)
	public Date getFmLdTime() {
		return fmLdTime;
	}

	public void setFmLdTime(Date fmLdTime) {
		this.fmLdTime = fmLdTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="装车时间到", align=2, sort=10)
	public Date getToLdTime() {
		return toLdTime;
	}

	public void setToLdTime(Date toLdTime) {
		this.toLdTime = toLdTime;
	}
	
	@ExcelField(title="装车交接人", align=2, sort=11)
	public String getDeliverOp() {
		return deliverOp;
	}

	public void setDeliverOp(String deliverOp) {
		this.deliverOp = deliverOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="装车交接时间", align=2, sort=12)
	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}
	
	@ExcelField(title="承运商编码", align=2, sort=13)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	@ExcelField(title="路线", align=2, sort=14)
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	@ExcelField(title="收货人编码", align=2, sort=15)
	public String getConsigneeCode() {
		return consigneeCode;
	}

	public void setConsigneeCode(String consigneeCode) {
		this.consigneeCode = consigneeCode;
	}
	
	@ExcelField(title="收货人名称", align=2, sort=16)
	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	
	@ExcelField(title="收货人电话", align=2, sort=17)
	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}
	
	@ExcelField(title="收货人地址", align=2, sort=18)
	public String getConsigneeAddr() {
		return consigneeAddr;
	}

	public void setConsigneeAddr(String consigneeAddr) {
		this.consigneeAddr = consigneeAddr;
	}
	
	@ExcelField(title="联系人名称", align=2, sort=19)
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@ExcelField(title="联系人电话", align=2, sort=20)
	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	
	@ExcelField(title="联系人地址", align=2, sort=21)
	public String getContactAddr() {
		return contactAddr;
	}

	public void setContactAddr(String contactAddr) {
		this.contactAddr = contactAddr;
	}
	
	@ExcelField(title="是否已预约月台", dictType="", align=2, sort=22)
	public String getIsAppointDock() {
		return isAppointDock;
	}

	public void setIsAppointDock(String isAppointDock) {
		this.isAppointDock = isAppointDock;
	}
	
	@ExcelField(title="自定义1", align=2, sort=23)
	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@ExcelField(title="自定义2", align=2, sort=24)
	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	@ExcelField(title="自定义3", align=2, sort=25)
	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}
	
	@ExcelField(title="自定义4", align=2, sort=26)
	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}
	
	@ExcelField(title="自定义5", align=2, sort=27)
	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	@ExcelField(title="分公司", align=2, sort=35)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}