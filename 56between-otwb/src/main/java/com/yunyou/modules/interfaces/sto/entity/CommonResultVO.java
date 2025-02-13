package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;

/**
 * 返回实体
 */
public class CommonResultVO implements Serializable {
    private static final long serialVersionUID = -7746969028923254528L;
    // 运单号
    private String waybillNo;
    // 操作网点名称
    private String opOrgName;
    // 扫描网点code
    private String opOrgCode;
    // 扫描网点所在城市
    private String opOrgCityName;
    // 扫描网点所在省份
    private String opOrgProvinceName;
    // 扫描网点电话
    private String opOrgTel;
    // 扫描时间
    private String opTime;
    // 扫描类型
    private String scanType;
    // 扫描员
    private String opEmpName;
    // 扫描员code
    private String opEmpCode;
    // 轨迹描述信息
    private String memo;
    // 派件员姓名 收件员姓名
    private String bizEmpName;
    // 派件员电话 收件员电话
    private String bizEmpCode;
    // 派件员电话 收件员电话
    private String bizEmpPhone;
    // 派件员电话 收件员电话
    private String bizEmpTel;
    // 下一站名称
    private String nextOrgName;
    // 下一站编码
    private String nextOrgCode;
    // 问题件原因名称
    private String issueName;
    // 签收人
    private String signoffPeople;
    // 重量，单位：kg
    private Double weight;
    // 包号
    private String containerNo;
    // 寄件网点编号
    private String orderOrgCode;
    // 寄件网点名称
    private String orderOrgName;
    // 运输任务号
    private String transportTaskNo;
    // 车牌号
    private String carNo;

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getOpOrgName() {
        return opOrgName;
    }

    public void setOpOrgName(String opOrgName) {
        this.opOrgName = opOrgName;
    }

    public String getOpOrgCode() {
        return opOrgCode;
    }

    public void setOpOrgCode(String opOrgCode) {
        this.opOrgCode = opOrgCode;
    }

    public String getOpOrgCityName() {
        return opOrgCityName;
    }

    public void setOpOrgCityName(String opOrgCityName) {
        this.opOrgCityName = opOrgCityName;
    }

    public String getOpOrgProvinceName() {
        return opOrgProvinceName;
    }

    public void setOpOrgProvinceName(String opOrgProvinceName) {
        this.opOrgProvinceName = opOrgProvinceName;
    }

    public String getOpOrgTel() {
        return opOrgTel;
    }

    public void setOpOrgTel(String opOrgTel) {
        this.opOrgTel = opOrgTel;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getOpEmpName() {
        return opEmpName;
    }

    public void setOpEmpName(String opEmpName) {
        this.opEmpName = opEmpName;
    }

    public String getOpEmpCode() {
        return opEmpCode;
    }

    public void setOpEmpCode(String opEmpCode) {
        this.opEmpCode = opEmpCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getBizEmpName() {
        return bizEmpName;
    }

    public void setBizEmpName(String bizEmpName) {
        this.bizEmpName = bizEmpName;
    }

    public String getBizEmpCode() {
        return bizEmpCode;
    }

    public void setBizEmpCode(String bizEmpCode) {
        this.bizEmpCode = bizEmpCode;
    }

    public String getBizEmpPhone() {
        return bizEmpPhone;
    }

    public void setBizEmpPhone(String bizEmpPhone) {
        this.bizEmpPhone = bizEmpPhone;
    }

    public String getBizEmpTel() {
        return bizEmpTel;
    }

    public void setBizEmpTel(String bizEmpTel) {
        this.bizEmpTel = bizEmpTel;
    }

    public String getNextOrgName() {
        return nextOrgName;
    }

    public void setNextOrgName(String nextOrgName) {
        this.nextOrgName = nextOrgName;
    }

    public String getNextOrgCode() {
        return nextOrgCode;
    }

    public void setNextOrgCode(String nextOrgCode) {
        this.nextOrgCode = nextOrgCode;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getSignoffPeople() {
        return signoffPeople;
    }

    public void setSignoffPeople(String signoffPeople) {
        this.signoffPeople = signoffPeople;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getOrderOrgCode() {
        return orderOrgCode;
    }

    public void setOrderOrgCode(String orderOrgCode) {
        this.orderOrgCode = orderOrgCode;
    }

    public String getOrderOrgName() {
        return orderOrgName;
    }

    public void setOrderOrgName(String orderOrgName) {
        this.orderOrgName = orderOrgName;
    }

    public String getTransportTaskNo() {
        return transportTaskNo;
    }

    public void setTransportTaskNo(String transportTaskNo) {
        this.transportTaskNo = transportTaskNo;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
