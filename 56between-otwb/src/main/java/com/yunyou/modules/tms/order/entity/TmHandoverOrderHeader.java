package com.yunyou.modules.tms.order.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 交接单Entity
 * @author zyf
 * @version 2020-03-30
 */
public class TmHandoverOrderHeader extends DataEntity<TmHandoverOrderHeader> {
	
	private static final long serialVersionUID = 1L;
	private String handoverNo;		// 交接单号
	private String dispatchNo;		// 派车单号
	private Date dispatchTime;		// 派车时间
	private String status;		// 交接单状态
	private String dispatchOutletCode;		// 派车网点
	private String deliveryOutletCode;		// 配送网点
	private String carrierCode;		// 承运商编码
	private String carNo;		// 车牌号
	private String driver;		// 司机
	private String driverTel;		// 联系电话
	private Long receivableQty;		// 应收件数
	private Long actualQty;		// 实收件数
	private String orgId;		// 机构ID
	private String baseOrgId;   // 基础数据机构ID
	private String handoverPerson;	// 交接人
	private String trip;            // 车次
	
	public TmHandoverOrderHeader() {
		super();
	}

	public TmHandoverOrderHeader(String id){
		super(id);
	}

	@ExcelField(title="交接单号", align=2, sort=7)
	public String getHandoverNo() {
		return handoverNo;
	}

	public void setHandoverNo(String handoverNo) {
		this.handoverNo = handoverNo;
	}
	
	@ExcelField(title="派车单号", align=2, sort=8)
	public String getDispatchNo() {
		return dispatchNo;
	}

	public void setDispatchNo(String dispatchNo) {
		this.dispatchNo = dispatchNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="派车时间", align=2, sort=9)
	public Date getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	
	@ExcelField(title="交接单状态", align=2, sort=10)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="派车网点", align=2, sort=11)
	public String getDispatchOutletCode() {
		return dispatchOutletCode;
	}

	public void setDispatchOutletCode(String dispatchOutletCode) {
		this.dispatchOutletCode = dispatchOutletCode;
	}
	
	@ExcelField(title="配送网点", align=2, sort=12)
	public String getDeliveryOutletCode() {
		return deliveryOutletCode;
	}

	public void setDeliveryOutletCode(String deliveryOutletCode) {
		this.deliveryOutletCode = deliveryOutletCode;
	}
	
	@ExcelField(title="承运商编码", align=2, sort=13)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	@ExcelField(title="车牌号", align=2, sort=14)
	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	
	@ExcelField(title="司机", align=2, sort=15)
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	@ExcelField(title="联系电话", align=2, sort=16)
	public String getDriverTel() {
		return driverTel;
	}

	public void setDriverTel(String driverTel) {
		this.driverTel = driverTel;
	}
	
	@ExcelField(title="应收件数", align=2, sort=17)
	public Long getReceivableQty() {
		return receivableQty;
	}

	public void setReceivableQty(Long receivableQty) {
		this.receivableQty = receivableQty;
	}
	
	@ExcelField(title="实收件数", align=2, sort=18)
	public Long getActualQty() {
		return actualQty;
	}

	public void setActualQty(Long actualQty) {
		this.actualQty = actualQty;
	}
	
	@ExcelField(title="机构ID", align=2, sort=19)
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

	public String getHandoverPerson() {
		return handoverPerson;
	}

	public void setHandoverPerson(String handoverPerson) {
		this.handoverPerson = handoverPerson;
	}

	public String getTrip() {
		return trip;
	}

	public void setTrip(String trip) {
		this.trip = trip;
	}
}