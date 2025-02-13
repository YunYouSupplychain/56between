package com.yunyou.modules.tms.applet.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TmDispatchOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String remarks;
    private String dispatchNo;        // 派车单号
    private String dispatchStatus;        // 派车单状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dispatchTime;        // 派车时间
    private String dispatchType;        // 派车单类型
    private String isException;        // 是否异常
    private String isAppInput;        // 是否APP录入
    private String transportType;        // 运输方式
    private Long totalQty;        // 发车总件数
    private Long receivedQty;        // 实收件数
    private Double totalWeight;        // 总重量
    private Double totalCubic;        // 总体积
    private Double totalAmount;        // 费用金额
    private String dispatchOutletCode;        // 派车网点
    private String dispatcher;        // 派车人
    private String carrierCode;        // 承运商编码
    private String carNo;           // 车牌号
    private String driver;           // 司机
    private String copilot;        // 副驾驶
    private String driverTel;        // 联系电话
    private String orgId;        // 机构ID
    private String account;        // 账号
    private String baseOrgId;   // 基础数据机构ID
    private String dispatchPlanNo;  // 调度计划单号
    private Double cashAmount;      // 现金金额
    private String trip;            // 车次
    private String trailerNo;       // 车挂号

    private Date dispatchTimeFm;
    private Date dispatchTimeTo;

    private String carrierName; // 承运商名称
    private String driverName; // 司机姓名
    private String copilotName; // 副驾驶名称
    private String dispatchOutletName;     // 派车网点名称
    private String orgName;
    private String receiveOutletName;       // 目标网点名称
    private String isSafetyCheck = "N";             // 是否已填写出车安全检查
    private String vehicleCheckItemStatus = "0";    // 车辆检查项状态
    private String repairStatus;            // 车辆维修状态
    private BigDecimal departureOdometerNumber = BigDecimal.ZERO; // 出车里程表数
    private BigDecimal mileage;                 // 行车里程

    private List<TmDispatchOrderSiteEntity> tmDispatchOrderSiteList = Lists.newArrayList(); // 配送点信息
    private List<TmDispatchOrderLabelEntity> tmDispatchOrderLabelList = Lists.newArrayList(); // 标签信息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(String dispatchType) {
        this.dispatchType = dispatchType;
    }

    public String getIsException() {
        return isException;
    }

    public void setIsException(String isException) {
        this.isException = isException;
    }

    public String getIsAppInput() {
        return isAppInput;
    }

    public void setIsAppInput(String isAppInput) {
        this.isAppInput = isAppInput;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public Long getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Long totalQty) {
        this.totalQty = totalQty;
    }

    public Long getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Long receivedQty) {
        this.receivedQty = receivedQty;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDispatchOutletCode() {
        return dispatchOutletCode;
    }

    public void setDispatchOutletCode(String dispatchOutletCode) {
        this.dispatchOutletCode = dispatchOutletCode;
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
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

    public String getCopilot() {
        return copilot;
    }

    public void setCopilot(String copilot) {
        this.copilot = copilot;
    }

    public String getDriverTel() {
        return driverTel;
    }

    public void setDriverTel(String driverTel) {
        this.driverTel = driverTel;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getDispatchPlanNo() {
        return dispatchPlanNo;
    }

    public void setDispatchPlanNo(String dispatchPlanNo) {
        this.dispatchPlanNo = dispatchPlanNo;
    }

    public Date getDispatchTimeFm() {
        return dispatchTimeFm;
    }

    public void setDispatchTimeFm(Date dispatchTimeFm) {
        this.dispatchTimeFm = dispatchTimeFm;
    }

    public Date getDispatchTimeTo() {
        return dispatchTimeTo;
    }

    public void setDispatchTimeTo(Date dispatchTimeTo) {
        this.dispatchTimeTo = dispatchTimeTo;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCopilotName() {
        return copilotName;
    }

    public void setCopilotName(String copilotName) {
        this.copilotName = copilotName;
    }

    public String getDispatchOutletName() {
        return dispatchOutletName;
    }

    public void setDispatchOutletName(String dispatchOutletName) {
        this.dispatchOutletName = dispatchOutletName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<TmDispatchOrderSiteEntity> getTmDispatchOrderSiteList() {
        return tmDispatchOrderSiteList;
    }

    public void setTmDispatchOrderSiteList(List<TmDispatchOrderSiteEntity> tmDispatchOrderSiteList) {
        this.tmDispatchOrderSiteList = tmDispatchOrderSiteList;
    }

    public List<TmDispatchOrderLabelEntity> getTmDispatchOrderLabelList() {
        return tmDispatchOrderLabelList;
    }

    public void setTmDispatchOrderLabelList(List<TmDispatchOrderLabelEntity> tmDispatchOrderLabelList) {
        this.tmDispatchOrderLabelList = tmDispatchOrderLabelList;
    }

    public String getReceiveOutletName() {
        return receiveOutletName;
    }

    public void setReceiveOutletName(String receiveOutletName) {
        this.receiveOutletName = receiveOutletName;
    }

    public String getIsSafetyCheck() {
        return isSafetyCheck;
    }

    public void setIsSafetyCheck(String isSafetyCheck) {
        this.isSafetyCheck = isSafetyCheck;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getVehicleCheckItemStatus() {
        return vehicleCheckItemStatus;
    }

    public void setVehicleCheckItemStatus(String vehicleCheckItemStatus) {
        this.vehicleCheckItemStatus = vehicleCheckItemStatus;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    public BigDecimal getDepartureOdometerNumber() {
        return departureOdometerNumber;
    }

    public void setDepartureOdometerNumber(BigDecimal departureOdometerNumber) {
        this.departureOdometerNumber = departureOdometerNumber;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public String getTrailerNo() {
        return trailerNo;
    }

    public void setTrailerNo(String trailerNo) {
        this.trailerNo = trailerNo;
    }
}
