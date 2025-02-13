package com.yunyou.modules.tms.applet.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TmVehicleSafetyCheckEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    private String remarks;
    private String delFlag;
    // 检查日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;
    // 天气状况
    private String weatherCondition;
    // 气温
    private String airTemperature;
    // 车牌号
    private String vehicleNo;
    // 挂车号牌
    private String trailerNo;
    // 核载吨位
    private BigDecimal certifiedTonnage;
    // 类/项
    private String classItem;
    // 出车时间
    @JsonFormat(pattern = "HH:mm")
    private Date departureTime;
    // 出车里程表数
    private BigDecimal departureOdometerNumber;
    // 回场时间
    @JsonFormat(pattern = "HH:mm")
    private Date returnTime;
    // 回场里程表数
    private BigDecimal returnOdometerNumber;
    // 已检查项
    private String checkList;
    // 不符合项
    private String nonConformity;
    // 确认结论
    private String confirmConclusion;
    // 安管员签字
    private String safetySign;
    // 自定义1
    private String def1;
    // 自定义2
    private String def2;
    // 自定义3
    private String def3;
    // 自定义4
    private String def4;
    // 自定义5
    private String def5;
    // 机构ID
    private String orgId;
    // 基础数据机构ID
    private String baseOrgId;
    // 版本号
    private Long recVer = 0L;

    // 检查时间从
    private Date checkDateFm;
    // 检查时间到
    private Date checkDateTo;
    // 是否已签字
    private String isSign;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(String airTemperature) {
        this.airTemperature = airTemperature;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getTrailerNo() {
        return trailerNo;
    }

    public void setTrailerNo(String trailerNo) {
        this.trailerNo = trailerNo;
    }

    public BigDecimal getCertifiedTonnage() {
        return certifiedTonnage;
    }

    public void setCertifiedTonnage(BigDecimal certifiedTonnage) {
        this.certifiedTonnage = certifiedTonnage;
    }

    public String getClassItem() {
        return classItem;
    }

    public void setClassItem(String classItem) {
        this.classItem = classItem;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public BigDecimal getDepartureOdometerNumber() {
        return departureOdometerNumber;
    }

    public void setDepartureOdometerNumber(BigDecimal departureOdometerNumber) {
        this.departureOdometerNumber = departureOdometerNumber;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public BigDecimal getReturnOdometerNumber() {
        return returnOdometerNumber;
    }

    public void setReturnOdometerNumber(BigDecimal returnOdometerNumber) {
        this.returnOdometerNumber = returnOdometerNumber;
    }

    public String getCheckList() {
        return checkList;
    }

    public void setCheckList(String checkList) {
        this.checkList = checkList;
    }

    public String getNonConformity() {
        return nonConformity;
    }

    public void setNonConformity(String nonConformity) {
        this.nonConformity = nonConformity;
    }

    public String getConfirmConclusion() {
        return confirmConclusion;
    }

    public void setConfirmConclusion(String confirmConclusion) {
        this.confirmConclusion = confirmConclusion;
    }

    public String getSafetySign() {
        return safetySign;
    }

    public void setSafetySign(String safetySign) {
        this.safetySign = safetySign;
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

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public Long getRecVer() {
        return recVer;
    }

    public void setRecVer(Long recVer) {
        this.recVer = recVer;
    }

    public Date getCheckDateFm() {
        return checkDateFm;
    }

    public void setCheckDateFm(Date checkDateFm) {
        this.checkDateFm = checkDateFm;
    }

    public Date getCheckDateTo() {
        return checkDateTo;
    }

    public void setCheckDateTo(Date checkDateTo) {
        this.checkDateTo = checkDateTo;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }
}
