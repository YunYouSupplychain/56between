package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 出库单打包记录Entity
 * @author WMJ
 * @version 2019-12-16
 */
public class BanQinWmSoPack extends DataEntity<BanQinWmSoPack> {
	
	private static final long serialVersionUID = 1L;
	private String soNo;		// 出库单号
	private String soType;		// 出库单类型
	private Date orderDate;		// 订单时间
	private String ownerCode;		// 货主编码
	private String ownerName;		// 货主名称
	private String deliveryName;		// 发货人
	private String deliveryTel;		// 发货人电话
	private String deliveryZip;		// 发货人邮编
	private String deliveryArea;		// 发货人区域
	private String deliveryAddress;		// 发货人地址
	private String consignee;		// 收货人
	private String consigneeTel;		// 收货人电话
	private String consigneeAddress;		// 收货人地址
	private String consigneeZip;		// 收货人邮编
	private String consigneeArea;		// 发货人区域
	private String businessNo;		// 商流订单号
	private String chainNo;		// 供应链订单号
	private String taskNo;		// 供应链任务号
	private String customerOrderNo;		// 客户订单号
	private String externalNo;		// 外部单号
	private String allocId;		// 分配明细Id
	private String waveNo;		// 波次单号
	private String skuCode;		// 商品编码
	private String skuName;		// 商品名称
	private String locCode;		// 库位编码
	private String traceId;		// 跟踪号
	private Double qty;		// 数量
	private String toLoc;		// 目标库位编码
	private String toId;		// 目标跟踪号
	private String pickOp;		// 拣货人
	private Date pickTime;		// 拣货时间
	private String checkOp;		// 复核人
	private Date checkTime;		// 复核时间
	private String packOp;		// 打包人
	private Date packTime;		// 打包时间
	private String trackingNo;		// 快递单号
	private String orgId;		// 机构Id
	private String caseNo;		// 打包箱号
	private String carrierCode;	// 承运商编码
	private String carrierName;	// 承运商名称
	private String scanCaseNo;	// 扫描箱号
	
	public BanQinWmSoPack() {
		super();
	}

	public BanQinWmSoPack(String id){
		super(id);
	}

	@ExcelField(title="出库单号", align=2, sort=7)
	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}
	
	@ExcelField(title="出库单类型", align=2, sort=8)
	public String getSoType() {
		return soType;
	}

	public void setSoType(String soType) {
		this.soType = soType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="订单时间", align=2, sort=9)
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	@ExcelField(title="货主编码", align=2, sort=10)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="货主名称", align=2, sort=11)
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	@ExcelField(title="发货人", align=2, sort=12)
	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	
	@ExcelField(title="发货人电话", align=2, sort=13)
	public String getDeliveryTel() {
		return deliveryTel;
	}

	public void setDeliveryTel(String deliveryTel) {
		this.deliveryTel = deliveryTel;
	}
	
	@ExcelField(title="发货人邮编", align=2, sort=14)
	public String getDeliveryZip() {
		return deliveryZip;
	}

	public void setDeliveryZip(String deliveryZip) {
		this.deliveryZip = deliveryZip;
	}
	
	@ExcelField(title="发货人区域", align=2, sort=15)
	public String getDeliveryArea() {
		return deliveryArea;
	}

	public void setDeliveryArea(String deliveryArea) {
		this.deliveryArea = deliveryArea;
	}

	@ExcelField(title="发货人地址", align=2, sort=15)
	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	
	@ExcelField(title="收货人", align=2, sort=16)
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	
	@ExcelField(title="收货人电话", align=2, sort=17)
	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}
	
	@ExcelField(title="收货人地址", align=2, sort=18)
	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	
	@ExcelField(title="收货人邮编", align=2, sort=19)
	public String getConsigneeZip() {
		return consigneeZip;
	}

	public void setConsigneeZip(String consigneeZip) {
		this.consigneeZip = consigneeZip;
	}
	
	@ExcelField(title="收货人区域", align=2, sort=20)
	public String getConsigneeArea() {
		return consigneeArea;
	}

	public void setConsigneeArea(String consigneeArea) {
		this.consigneeArea = consigneeArea;
	}
	
	@ExcelField(title="商流订单号", align=2, sort=21)
	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	
	@ExcelField(title="供应链订单号", align=2, sort=22)
	public String getChainNo() {
		return chainNo;
	}

	public void setChainNo(String chainNo) {
		this.chainNo = chainNo;
	}
	
	@ExcelField(title="供应链任务号", align=2, sort=23)
	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	
	@ExcelField(title="客户订单号", align=2, sort=24)
	public String getCustomerOrderNo() {
		return customerOrderNo;
	}

	public void setCustomerOrderNo(String customerOrderNo) {
		this.customerOrderNo = customerOrderNo;
	}
	
	@ExcelField(title="外部单号", align=2, sort=25)
	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}
	
	@ExcelField(title="分配明细Id", align=2, sort=26)
	public String getAllocId() {
		return allocId;
	}

	public void setAllocId(String allocId) {
		this.allocId = allocId;
	}
	
	@ExcelField(title="波次单号", align=2, sort=27)
	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}
	
	@ExcelField(title="商品编码", align=2, sort=28)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="商品名称", align=2, sort=29)
	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	@ExcelField(title="库位编码", align=2, sort=30)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=31)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=32)
	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}
	
	@ExcelField(title="目标库位编码", align=2, sort=33)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="目标跟踪号", align=2, sort=34)
	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	@ExcelField(title="拣货人", align=2, sort=35)
	public String getPickOp() {
		return pickOp;
	}

	public void setPickOp(String pickOp) {
		this.pickOp = pickOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="拣货时间", align=2, sort=36)
	public Date getPickTime() {
		return pickTime;
	}

	public void setPickTime(Date pickTime) {
		this.pickTime = pickTime;
	}
	
	@ExcelField(title="复核人", align=2, sort=37)
	public String getCheckOp() {
		return checkOp;
	}

	public void setCheckOp(String checkOp) {
		this.checkOp = checkOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="复核时间", align=2, sort=38)
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
	@ExcelField(title="打包人", align=2, sort=39)
	public String getPackOp() {
		return packOp;
	}

	public void setPackOp(String packOp) {
		this.packOp = packOp;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="打包时间", align=2, sort=40)
	public Date getPackTime() {
		return packTime;
	}

	public void setPackTime(Date packTime) {
		this.packTime = packTime;
	}
	
	@ExcelField(title="快递单号", align=2, sort=41)
	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@ExcelField(title="打包箱号", align=2, sort=42)
	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	@ExcelField(title="承运商编码", align=2, sort=43)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	@ExcelField(title="承运商名称", align=2, sort=44)
	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	@ExcelField(title="扫描箱号", align=2, sort=45)
	public String getScanCaseNo() {
		return scanCaseNo;
	}

	public void setScanCaseNo(String scanCaseNo) {
		this.scanCaseNo = scanCaseNo;
	}
}