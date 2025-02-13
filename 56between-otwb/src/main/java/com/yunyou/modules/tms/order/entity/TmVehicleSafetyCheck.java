package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出车安全检查表Entity
 * @author ZYF
 * @version 2020-07-15
 */
public class TmVehicleSafetyCheck extends DataEntity<TmVehicleSafetyCheck> {

	private static final long serialVersionUID = 1L;
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
	@JsonFormat(pattern = "HH:mm:ss")
	private Date departureTime;
	// 出车里程表数
	private BigDecimal departureOdometerNumber;
	// 回场时间
	@JsonFormat(pattern = "HH:mm:ss")
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

	public TmVehicleSafetyCheck() {
		super();
	}

	public TmVehicleSafetyCheck(String id){
		super(id);
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
}