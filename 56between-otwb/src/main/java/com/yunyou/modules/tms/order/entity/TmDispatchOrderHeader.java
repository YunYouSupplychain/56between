package com.yunyou.modules.tms.order.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 派车单Entity
 *
 * @author liujianhua
 * @version 2020-03-11
 */
public class TmDispatchOrderHeader extends DataEntity<TmDispatchOrderHeader> {

    private static final long serialVersionUID = 1L;
    private String dispatchNo;      // 派车单号
    private String dispatchStatus;  // 派车单状态
    private Date dispatchTime;      // 派车时间
    private String dispatchType;    // 派车单类型
    private Date departureTime;     // 发车时间
    private String isException;     // 是否异常
    private String isAppInput;      // 是否APP录入
    private String transportType;   // 运输方式
    private Integer totalQty;       // 发车总件数
    private Integer receivedQty;    // 实收件数
    private Double totalSkuQty;     // 总数量
    private Double totalWeight;     // 总重量
    private Double totalCubic;      // 总体积
    private Double totalAmount;     // 费用金额
    private String dispatchOutletCode;  // 派车网点
    private String dispatcher;      // 派车人
    private String carrierCode;     // 承运商编码
    private String carNo;           // 车牌号
    private String driver;          // 司机
    private String copilot;         // 副驾驶
    private String driverTel;       // 司机电话
    private String startAreaId;     // 始发地区域ID
    private String endAreaId;       // 目的地区域ID
    private String orgId;           // 机构ID
    private String baseOrgId;       // 基础数据机构ID
    private String account;         // 账号
    private String dispatchPlanNo;  // 调度计划单号
    private String dataSource;      // 数据来源
    private Double cashAmount;      // 现金金额
    private Double prepaidAmount;   // 预付金额
    private String trip;            // 车次
    private String shift;			// 班次
    private String platform;		// 月台
    private String sealNo;		    // 铅封号
    private String feedBackStatus;  // 反馈状态

    public TmDispatchOrderHeader() {
        super();
    }

    public TmDispatchOrderHeader(String id) {
        super(id);
    }

    @ExcelField(title = "派车单号", align = 2, sort = 7)
    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    @ExcelField(title = "派车单状态", dictType = "TMS_DISPATCH_STATUS", align = 2, sort = 8)
    public String getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "派车时间", align = 2, sort = 9)
    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    @ExcelField(title = "派车单类型", dictType = "TMS_DISPATCH_TYPE", align = 2, sort = 10)
    public String getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(String dispatchType) {
        this.dispatchType = dispatchType;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "发车时间")
    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    @ExcelField(title = "是否异常", dictType = "SYS_YES_NO", align = 2, sort = 11)
    public String getIsException() {
        return isException;
    }

    public void setIsException(String isException) {
        this.isException = isException;
    }

    @ExcelField(title = "是否APP录入", dictType = "SYS_YES_NO", align = 2, sort = 12)
    public String getIsAppInput() {
        return isAppInput;
    }

    public void setIsAppInput(String isAppInput) {
        this.isAppInput = isAppInput;
    }

    @ExcelField(title = "运输方式", dictType = "TMS_TRANSPORT_METHOD", align = 2, sort = 13)
    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    @ExcelField(title = "发车总件数", align = 2, sort = 14)
    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    @ExcelField(title = "实收件数", align = 2, sort = 15)
    public Integer getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Integer receivedQty) {
        this.receivedQty = receivedQty;
    }

    @ExcelField(title = "总重量", align = 2, sort = 16)
    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    @ExcelField(title = "总体积", align = 2, sort = 17)
    public Double getTotalCubic() {
        return totalCubic;
    }

    public void setTotalCubic(Double totalCubic) {
        this.totalCubic = totalCubic;
    }

    @ExcelField(title = "费用金额", align = 2, sort = 18)
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @ExcelField(title = "派车网点", align = 2, sort = 19)
    public String getDispatchOutletCode() {
        return dispatchOutletCode;
    }

    public void setDispatchOutletCode(String dispatchOutletCode) {
        this.dispatchOutletCode = dispatchOutletCode;
    }

    @ExcelField(title = "派车人", align = 2, sort = 20)
    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }

    @ExcelField(title = "承运商编码", align = 2, sort = 21)
    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    @ExcelField(title = "车牌号", align = 2, sort = 22)
    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    @ExcelField(title = "司机", align = 2, sort = 23)
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @ExcelField(title = "联系电话", align = 2, sort = 24)
    public String getDriverTel() {
        return driverTel;
    }

    public void setDriverTel(String driverTel) {
        this.driverTel = driverTel;
    }

    public String getStartAreaId() {
        return startAreaId;
    }

    public void setStartAreaId(String startAreaId) {
        this.startAreaId = startAreaId;
    }

    public String getEndAreaId() {
        return endAreaId;
    }

    public void setEndAreaId(String endAreaId) {
        this.endAreaId = endAreaId;
    }

    @ExcelField(title = "机构ID", align = 2, sort = 25)
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

    public String getCopilot() {
        return copilot;
    }

    public void setCopilot(String copilot) {
        this.copilot = copilot;
    }

    public String getDispatchPlanNo() {
        return dispatchPlanNo;
    }

    public void setDispatchPlanNo(String dispatchPlanNo) {
        this.dispatchPlanNo = dispatchPlanNo;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Double getTotalSkuQty() {
        return totalSkuQty;
    }

    public void setTotalSkuQty(Double totalSkuQty) {
        this.totalSkuQty = totalSkuQty;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Double getPrepaidAmount() {
        return prepaidAmount;
    }

    public void setPrepaidAmount(Double prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public String getFeedBackStatus() {
        return feedBackStatus;
    }

    public void setFeedBackStatus(String feedBackStatus) {
        this.feedBackStatus = feedBackStatus;
    }
}