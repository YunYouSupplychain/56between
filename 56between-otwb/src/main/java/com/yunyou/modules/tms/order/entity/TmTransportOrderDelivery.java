package com.yunyou.modules.tms.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;

/**
 * 运输订单配送信息Entity
 *
 * @author liujianhua
 * @version 2020-03-04
 */
public class TmTransportOrderDelivery extends DataEntity<TmTransportOrderDelivery> {

    private static final long serialVersionUID = 1L;
    private String transportNo;// 运输单号
    private String carrierCode;// 承运商编码
    private String carType;// 车型
    private Double totalEaQty;// 件数
    private Double totalPlQty;// 托盘数
    private Double totalWeight;// 重量
    private Double totalCubic;// 体积
    private BigDecimal paidAmount;// 到付金额
    private Double actualShipWeight;// 实际发货重量
    private String virtualBillingStatus;// 虚拟开单状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planArriveTime;// 预计到达时间
    private String isException;// 是否异常
    private String signStatus;// 签收状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;// 签收时间
    private String signBy;// 签收人
    private String signRemarks;// 签收备注
    private String receiptStatus;// 回单状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiptTime;// 回单时间
    private String receiptBy;// 回单人
    private String receiptRemarks;// 回单备注
    private Integer receiptCount;// 回单份数
    private String orgId;// 机构ID
    private String baseOrgId;// 基础数据机构ID

    public TmTransportOrderDelivery() {
        super();
    }

    public TmTransportOrderDelivery(String id) {
        super(id);
    }

    public TmTransportOrderDelivery(String transportNo, String orgId) {
        this.transportNo = transportNo;
        this.orgId = orgId;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Double getTotalEaQty() {
        return totalEaQty;
    }

    public void setTotalEaQty(Double totalEaQty) {
        this.totalEaQty = totalEaQty;
    }

    public Double getTotalPlQty() {
        return totalPlQty;
    }

    public void setTotalPlQty(Double totalPlQty) {
        this.totalPlQty = totalPlQty;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Double getTotalCubic() {
        return totalCubic;
    }

    public void setTotalCubic(Double totalCubic) {
        this.totalCubic = totalCubic;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getActualShipWeight() {
        return actualShipWeight;
    }

    public void setActualShipWeight(Double actualShipWeight) {
        this.actualShipWeight = actualShipWeight;
    }

    public String getVirtualBillingStatus() {
        return virtualBillingStatus;
    }

    public void setVirtualBillingStatus(String virtualBillingStatus) {
        this.virtualBillingStatus = virtualBillingStatus;
    }

    public Date getPlanArriveTime() {
        return planArriveTime;
    }

    public void setPlanArriveTime(Date planArriveTime) {
        this.planArriveTime = planArriveTime;
    }

    public String getIsException() {
        return isException;
    }

    public void setIsException(String isException) {
        this.isException = isException;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getSignBy() {
        return signBy;
    }

    public void setSignBy(String signBy) {
        this.signBy = signBy;
    }

    public String getSignRemarks() {
        return signRemarks;
    }

    public void setSignRemarks(String signRemarks) {
        this.signRemarks = signRemarks;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getReceiptBy() {
        return receiptBy;
    }

    public void setReceiptBy(String receiptBy) {
        this.receiptBy = receiptBy;
    }

    public String getReceiptRemarks() {
        return receiptRemarks;
    }

    public void setReceiptRemarks(String receiptRemarks) {
        this.receiptRemarks = receiptRemarks;
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

    public Integer getReceiptCount() {
        return receiptCount;
    }

    public void setReceiptCount(Integer receiptCount) {
        this.receiptCount = receiptCount;
    }
}