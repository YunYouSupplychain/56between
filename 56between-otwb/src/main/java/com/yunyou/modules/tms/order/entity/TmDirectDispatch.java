package com.yunyou.modules.tms.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 直接调度派车信息
 *
 * @author liujianhua
 * @version 2022.8.9
 */
public class TmDirectDispatch implements Serializable {
    private static final long serialVersionUID = 6831079788774686641L;

    /**
     * 运输订单ID
     */
    private String ids;
    /**
     * 派车网点
     */
    private String dispatchOutletCode;
    /**
     * 派车人
     */
    private String dispatcher;
    /**
     * 派车时间
     */
    private Date dispatchTime;
    /**
     * 派车单类型
     */
    private String dispatchType;
    /**
     * 班次
     */
    private String shift;
    /**
     * 月台
     */
    private String platform;
    /**
     * 始发地区域ID
     */
    private String startAreaId;
    /**
     * 目的地区域ID
     */
    private String endAreaId;
    /**
     * 承运商
     */
    private String carrierCode;
    /**
     * 车牌号
     */
    private String carNo;
    /**
     * 司机
     */
    private String driver;
    /**
     * 司机电话
     */
    private String driverTel;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 基础数据机构ID
     */
    private String baseOrgId;
    /**
     * 机构ID
     */
    private String orgId;
    /**
     * 标签ids
     */
    private String labelIds;
    /**
     * 是否新建派车单
     */
    private String isNew;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

    public String getDriverTel() {
        return driverTel;
    }

    public void setDriverTel(String driverTel) {
        this.driverTel = driverTel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
}
