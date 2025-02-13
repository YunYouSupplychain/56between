package com.yunyou.modules.oms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.modules.sys.entity.User;

import java.math.BigDecimal;
import java.util.Date;

public class OmRequisitionHeader extends DataEntity<OmRequisitionHeader> {
    private static final long serialVersionUID = -3850196957159380977L;

    private String reqNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date orderTime;
    private String orderType;
    private String status;
    private User auditBy;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date auditTime;
    private String ownerCode;
    private String customerNo;
    private String reqReason;
    private String fmOrgId;
    private String toOrgId;
    private String logisticsNo;
    private String carrierCode;
    private String transportMode;
    private String vehicleNo;
    private String driver;
    private String driverTel;
    private BigDecimal freightAmount;
    private String shipper;
    private String shipperTel;
    private String shipAreaId;
    private String shipArea;
    private String shipAddress;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date planShipTime;
    private String consignee;
    private String consigneeTel;
    private String consigneeAreaId;
    private String consigneeArea;
    private String consigneeAddress;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date planArrivalTime;
    private String def1;
    private String def2;
    private String def3;
    private String def4;
    private String def5;
    private String orgId;

    public OmRequisitionHeader() {
    }

    public OmRequisitionHeader(String id) {
        super(id);
    }

    public OmRequisitionHeader(String reqNo, String orgId) {
        this.reqNo = reqNo;
        this.orgId = orgId;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(User auditBy) {
        this.auditBy = auditBy;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getReqReason() {
        return reqReason;
    }

    public void setReqReason(String reqReason) {
        this.reqReason = reqReason;
    }

    public String getFmOrgId() {
        return fmOrgId;
    }

    public void setFmOrgId(String fmOrgId) {
        this.fmOrgId = fmOrgId;
    }

    public String getToOrgId() {
        return toOrgId;
    }

    public void setToOrgId(String toOrgId) {
        this.toOrgId = toOrgId;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
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

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperTel() {
        return shipperTel;
    }

    public void setShipperTel(String shipperTel) {
        this.shipperTel = shipperTel;
    }

    public String getShipAreaId() {
        return shipAreaId;
    }

    public void setShipAreaId(String shipAreaId) {
        this.shipAreaId = shipAreaId;
    }

    public String getShipArea() {
        return shipArea;
    }

    public void setShipArea(String shipArea) {
        this.shipArea = shipArea;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public Date getPlanShipTime() {
        return planShipTime;
    }

    public void setPlanShipTime(Date planShipTime) {
        this.planShipTime = planShipTime;
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

    public String getConsigneeAreaId() {
        return consigneeAreaId;
    }

    public void setConsigneeAreaId(String consigneeAreaId) {
        this.consigneeAreaId = consigneeAreaId;
    }

    public String getConsigneeArea() {
        return consigneeArea;
    }

    public void setConsigneeArea(String consigneeArea) {
        this.consigneeArea = consigneeArea;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public Date getPlanArrivalTime() {
        return planArrivalTime;
    }

    public void setPlanArrivalTime(Date planArrivalTime) {
        this.planArrivalTime = planArrivalTime;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
