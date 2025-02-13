package com.yunyou.modules.bms.business.entity;

import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 描述：派车单数据
 */
public class BmsDispatchOrderData extends DataEntity<BmsDispatchOrderData> {

    private static final long serialVersionUID = 4060867913529569357L;
    /**
     * 派车单号
     */
    private String orderNo;
    /**
     * 派车单类型
     */
    private String orderType;
    /**
     * 订单日期
     */
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date orderDate;
    /**
     * 派车时间
     */
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date dispatchTime;
    /**
     * 运输类型
     */
    private String tranType;
    /**
     * 承运商编码
     */
    private String carrierCode;
    /**
     * 承运商名称
     */
    private String carrierName;
    /**
     * 车型
     */
    private String carType;
    /**
     * 车牌号
     */
    private String vehicleNo;
    /**
     * 驾驶员编码
     */
    private String driverCode;
    /**
     * 驾驶员名称
     */
    private String driverName;
    /**
     * 副驾驶员编码
     */
    private String copilotCode;
    /**
     * 副驾驶员名称
     */
    private String copilotName;
    /**
     * 总EA数
     */
    private Double totalQtyEa;
    /**
     * 总重量
     */
    private Double totalWeight;
    /**
     * 总体积
     */
    private Double totalVolume;
    /**
     * 数据来源
     */
    private String dataSources;
    /**
     * 是否参与计费
     */
    private String isFee;
    /**
     * 机构ID
     */
    private String orgId;
    /**
     * 配送点数据
     */
    private List<BmsDispatchOrderSiteData> siteList;

    /**
     * 调度员
     */
    private String dispatcher;
    /**
     * 签收人
     */
    private String signBy;
    /**
     * 签收时间
     */
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date signTime;
    /**
     * 总框数
     */
    private Double totalQtyFrame;
    /**
     * 月台
     */
    private String platform;
    /**
     * 铅封号
     */
    private String sealNo;
    /**
     * 班次
     */
    private String shift;

    public BmsDispatchOrderData() {
    }

    public BmsDispatchOrderData(String id) {
        super(id);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCopilotCode() {
        return copilotCode;
    }

    public void setCopilotCode(String copilotCode) {
        this.copilotCode = copilotCode;
    }

    public String getCopilotName() {
        return copilotName;
    }

    public void setCopilotName(String copilotName) {
        this.copilotName = copilotName;
    }

    public Double getTotalQtyEa() {
        return totalQtyEa;
    }

    public void setTotalQtyEa(Double totalQtyEa) {
        this.totalQtyEa = totalQtyEa;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getDataSources() {
        return dataSources;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }

    public String getIsFee() {
        return isFee;
    }

    public void setIsFee(String isFee) {
        this.isFee = isFee;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<BmsDispatchOrderSiteData> getSiteList() {
        return siteList;
    }

    public void setSiteList(List<BmsDispatchOrderSiteData> siteList) {
        this.siteList = siteList;
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getSignBy() {
        return signBy;
    }

    public void setSignBy(String signBy) {
        this.signBy = signBy;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Double getTotalQtyFrame() {
        return totalQtyFrame;
    }

    public void setTotalQtyFrame(Double totalQtyFrame) {
        this.totalQtyFrame = totalQtyFrame;
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

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
}
