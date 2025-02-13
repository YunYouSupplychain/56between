package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 描述：交接单明细
 *
 * @author Jianhua on 2020-2-6
 */
public class BanQinWmOutHandoverDetail extends DataEntity<BanQinWmOutHandoverDetail> {
    private static final long serialVersionUID = -8180417197969675716L;

    private String handoverNo;  // 交接单号
    private String soNo;		// 出库单号
    private String soType;		// 出库单类型
    private Date orderDate;		// 订单时间
    private String ownerCode;		// 货主编码
    private String ownerName;		// 货主名称
    private String deliveryName;	// 发货人
    private String deliveryTel;		// 发货人电话
    private String deliveryZip;		// 发货人邮编
    private String deliveryArea;		// 发货人区域
    private String deliveryAddress;		// 发货人地址
    private String consignee;		    // 收货人
    private String consigneeTel;		// 收货人电话
    private String consigneeAddress;	// 收货人地址
    private String consigneeZip;		// 收货人邮编
    private String consigneeArea;		// 发货人区域
    private String businessNo;	// 商流订单号
    private String chainNo;		// 供应链订单号
    private String taskNo;		// 供应链任务号
    private String customerOrderNo;		// 客户订单号
    private String externalNo;	// 外部单号
    private String allocId;		// 分配明细Id
    private String waveNo;		// 波次单号
    private String skuCode;		// 商品编码
    private String skuName;		// 商品名称
    private String locCode;		// 库位编码
    private String traceId;		// 跟踪号
    private Double qty;		    // 数量
    private String toLoc;		// 目标库位编码
    private String toId;		// 目标跟踪号
    private String pickOp;		// 拣货人
    private Date pickTime;		// 拣货时间
    private String checkOp;		// 复核人
    private Date checkTime;		// 复核时间
    private String packOp;		// 打包人
    private Date packTime;		// 打包时间
    private String trackingNo;	// 快递单号
    private String orgId;		// 机构Id
    private String orgName;     // 机构名称
    private String caseNo;		// 打包箱号
    private String carrierCode;	// 承运商编码
    private String carrierName;	// 承运商名称
    private Date shipTime;      // 发运时间

    public BanQinWmOutHandoverDetail() {
        super();
        this.recVer = 0;
    }

    public BanQinWmOutHandoverDetail(String id) {
        super(id);
    }

    public BanQinWmOutHandoverDetail(String handoverNo, String orgId) {
        this.handoverNo = handoverNo;
        this.orgId = orgId;
    }

    public String getHandoverNo() {
        return handoverNo;
    }

    public void setHandoverNo(String handoverNo) {
        this.handoverNo = handoverNo;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getSoType() {
        return soType;
    }

    public void setSoType(String soType) {
        this.soType = soType;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryTel() {
        return deliveryTel;
    }

    public void setDeliveryTel(String deliveryTel) {
        this.deliveryTel = deliveryTel;
    }

    public String getDeliveryZip() {
        return deliveryZip;
    }

    public void setDeliveryZip(String deliveryZip) {
        this.deliveryZip = deliveryZip;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeZip() {
        return consigneeZip;
    }

    public void setConsigneeZip(String consigneeZip) {
        this.consigneeZip = consigneeZip;
    }

    public String getConsigneeArea() {
        return consigneeArea;
    }

    public void setConsigneeArea(String consigneeArea) {
        this.consigneeArea = consigneeArea;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getChainNo() {
        return chainNo;
    }

    public void setChainNo(String chainNo) {
        this.chainNo = chainNo;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    public String getExternalNo() {
        return externalNo;
    }

    public void setExternalNo(String externalNo) {
        this.externalNo = externalNo;
    }

    public String getAllocId() {
        return allocId;
    }

    public void setAllocId(String allocId) {
        this.allocId = allocId;
    }

    public String getWaveNo() {
        return waveNo;
    }

    public void setWaveNo(String waveNo) {
        this.waveNo = waveNo;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getPickOp() {
        return pickOp;
    }

    public void setPickOp(String pickOp) {
        this.pickOp = pickOp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    public String getCheckOp() {
        return checkOp;
    }

    public void setCheckOp(String checkOp) {
        this.checkOp = checkOp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getPackOp() {
        return packOp;
    }

    public void setPackOp(String packOp) {
        this.packOp = packOp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getPackTime() {
        return packTime;
    }

    public void setPackTime(Date packTime) {
        this.packTime = packTime;
    }

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

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getShipTime() {
        return shipTime;
    }

    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
