package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 调度计划配置信息Entity
 * @author WMJ
 * @version 2020-07-07
 */
public class TmDispatchPlanConfig extends DataEntity<TmDispatchPlanConfig> {

	private static final long serialVersionUID = 1L;
	// 调度计划号
	private String planNo;
	// 车牌号
	private String vehicleNo;
	// 车次
	private String trip;
	// 提货点
	private String pickUpPoint;
	// 送货点
	private String deliveryPoint;
	// 商品编码
	private String skuCode;
	// 配载数量
	private BigDecimal qty;
	// 仓室
	private String wareRoom;
	// 驾驶员
	private String driver;
	// 押运员
	private String escort;
	// 机构ID
	private String orgId;
	// 基础数据机构ID
	private String baseOrgId;
	// 运输订单号
	private String transportNo;
	// 派车单号
	private String dispatchNo;

	public TmDispatchPlanConfig() {
		super();
	}

	public TmDispatchPlanConfig(String id){
		super(id);
	}

	public String getPlanNo() {
		return planNo;
	}

	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getTrip() {
		return trip;
	}

	public void setTrip(String trip) {
		this.trip = trip;
	}

	public String getPickUpPoint() {
		return pickUpPoint;
	}

	public void setPickUpPoint(String pickUpPoint) {
		this.pickUpPoint = pickUpPoint;
	}

	public String getDeliveryPoint() {
		return deliveryPoint;
	}

	public void setDeliveryPoint(String deliveryPoint) {
		this.deliveryPoint = deliveryPoint;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getWareRoom() {
		return wareRoom;
	}

	public void setWareRoom(String wareRoom) {
		this.wareRoom = wareRoom;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getEscort() {
		return escort;
	}

	public void setEscort(String escort) {
		this.escort = escort;
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

	public String getTransportNo() {
		return transportNo;
	}

	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}

	public String getDispatchNo() {
		return dispatchNo;
	}

	public void setDispatchNo(String dispatchNo) {
		this.dispatchNo = dispatchNo;
	}
}