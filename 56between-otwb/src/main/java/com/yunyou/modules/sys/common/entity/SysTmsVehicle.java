package com.yunyou.modules.sys.common.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 车辆信息Entity
 */
public class SysTmsVehicle extends DataEntity<SysTmsVehicle> {
	
	private static final long serialVersionUID = 1L;
	private String carNo;// 车牌号
	private String transportEquipmentTypeCode;// 设备类型编码
	private String carrierCode;// 承运商编码
	private String carType;// 车辆类型
	private String dispatchBase;// 调度基地
	private String mainDriver;// 主驾驶
	private String copilot;// 副驾驶
	private String trailer;// 挂车
	private String carBrand;// 车辆品牌
	private String carModel;// 车辆型号
	private String carColor;// 车辆颜色
	private String carBodyNo;// 车身号码
	private String supplierCode;// 供应商编码
	private String ownership;// 所有权
	private Double approvedLoadingWeight;// 核定装载重量
	private Double approvedLoadingCubic;// 核定装载体积
	private Double totalTractionWeight;// 牵引总重量(吨)
	private Double equipmentQuality;// 装备质量(吨)
	private Integer doorNumber;// 车门个数
	private Double length;// 车长
	private Double width;// 车宽
	private Double height;// 车高
	private String isTemperatureControl;// 是否温控
	private String temperatureType;// 车辆温别
	private String minTemperature;// 最低温
	private String maxTemperature;// 最高温
	private String refrigerationEquipmentCode;// 制冷设备编码
	private String isRisk;// 是否危品车
	private String riskLevel;// 危险等级
	private Date buyingTime;// 购车时间
	private String purchaseLocation;// 购车地点
	private Double purchaseAmount;// 购车金额
	private String emissionStandard;// 车辆排放标准
	private Double oilConsumption;// 油耗(升)
	private Double mileage;// 行车里程(公里)
	private Double horsepower;// 马力(匹)
	private Double depreciableLife;// 折旧年限(年)
	private Double scrappedLife;// 报废年限(年)
	private Date activeTime;// 启动时间
	private Date scrappedTime;// 报废时间
	private Double salvageRate;// 残值率(%)
	private Long axleNumber;// 轴数
	private String engineNo;// 发动机号
	private String oilType;// 用油型号
	private Date registeredTime;// 注册时间
	private String registeredLocation;// 注册地点
	private String drivingLicenseNo;// 行驶证号
	private Date drivingLicenseExpiryTime;// 行驶证有效期
	private Date operatingLicenseExpiryTime;// 运营证有效期
	private Double operatingDuration;// 运营时长
	private Date tollCollectionTime;// 通行缴费时间
	private String dataSet;// 数据套
    private String status;// 状态

	/* 资质信息 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date annualReviewExpiryTime;// 车辆年审到期时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date twoDimensionExpiryTime;// 车辆二维到期时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date tankInspectionExpiryTime;// 车辆罐检到期时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date insuranceExpiryTime;// 保险有限期
	private Integer vehicleMaintenanceMileage;// 车辆保养里程数
	private Integer vehicleApplyGreaseMileage;// 车辆打黄油保养里程数
	private Integer vehicleOilChangeMileage;// 车辆换机油保养里程数

	private List<SysTmsVehiclePart> vehiclePartList = Lists.newArrayList();;
	private List<SysTmsVehicleQualification> vehicleQualificationList = Lists.newArrayList();;

	private String dataSetName;// 数据套名称
	
	public SysTmsVehicle() {
		super();
	}

	public SysTmsVehicle(String id){
		super(id);
	}

	public SysTmsVehicle(String carNo, String dataSet) {
		this.carNo = carNo;
		this.dataSet = dataSet;
	}

	@ExcelField(title="车牌号", align=2, sort=7)
	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	
	@ExcelField(title="设备类型编码", align=2, sort=8)
	public String getTransportEquipmentTypeCode() {
		return transportEquipmentTypeCode;
	}

	public void setTransportEquipmentTypeCode(String transportEquipmentTypeCode) {
		this.transportEquipmentTypeCode = transportEquipmentTypeCode;
	}
	
	@ExcelField(title="承运商编码", align=2, sort=9)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	@ExcelField(title="车辆类型", dictType="TMS_CAR_TYPE", align=2, sort=10)
	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	@ExcelField(title="调度基地", align=2, sort=11)
	public String getDispatchBase() {
		return dispatchBase;
	}

	public void setDispatchBase(String dispatchBase) {
		this.dispatchBase = dispatchBase;
	}
	
	@ExcelField(title="主驾驶", align=2, sort=12)
	public String getMainDriver() {
		return mainDriver;
	}

	public void setMainDriver(String mainDriver) {
		this.mainDriver = mainDriver;
	}
	
	@ExcelField(title="副驾驶", align=2, sort=13)
	public String getCopilot() {
		return copilot;
	}

	public void setCopilot(String copilot) {
		this.copilot = copilot;
	}
	
	@ExcelField(title="挂车", align=2, sort=14)
	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	
	@ExcelField(title="车辆品牌", align=2, sort=15)
	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	
	@ExcelField(title="车辆型号", align=2, sort=16)
	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	
	@ExcelField(title="车辆颜色", align=2, sort=17)
	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	
	@ExcelField(title="车身号码", align=2, sort=18)
	public String getCarBodyNo() {
		return carBodyNo;
	}

	public void setCarBodyNo(String carBodyNo) {
		this.carBodyNo = carBodyNo;
	}
	
	@ExcelField(title="供应商编码", align=2, sort=19)
	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	
	@ExcelField(title="所有权", align=2, sort=20)
	public String getOwnership() {
		return ownership;
	}

	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}
	
	@ExcelField(title="核定装载重量", align=2, sort=21)
	public Double getApprovedLoadingWeight() {
		return approvedLoadingWeight;
	}

	public void setApprovedLoadingWeight(Double approvedLoadingWeight) {
		this.approvedLoadingWeight = approvedLoadingWeight;
	}
	
	@ExcelField(title="核定装载体积", align=2, sort=22)
	public Double getApprovedLoadingCubic() {
		return approvedLoadingCubic;
	}

	public void setApprovedLoadingCubic(Double approvedLoadingCubic) {
		this.approvedLoadingCubic = approvedLoadingCubic;
	}
	
	@ExcelField(title="牵引总重量(吨)", align=2, sort=23)
	public Double getTotalTractionWeight() {
		return totalTractionWeight;
	}

	public void setTotalTractionWeight(Double totalTractionWeight) {
		this.totalTractionWeight = totalTractionWeight;
	}
	
	@ExcelField(title="装备质量(吨)", align=2, sort=24)
	public Double getEquipmentQuality() {
		return equipmentQuality;
	}

	public void setEquipmentQuality(Double equipmentQuality) {
		this.equipmentQuality = equipmentQuality;
	}
	
	@ExcelField(title="车门个数", align=2, sort=25)
	public Integer getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(Integer doorNumber) {
		this.doorNumber = doorNumber;
	}
	
	@ExcelField(title="车长", align=2, sort=26)
	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}
	
	@ExcelField(title="车宽", align=2, sort=27)
	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}
	
	@ExcelField(title="车高", align=2, sort=28)
	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}
	
	@ExcelField(title="是否温控", dictType="SYS_YES_NO", align=2, sort=29)
	public String getIsTemperatureControl() {
		return isTemperatureControl;
	}

	public void setIsTemperatureControl(String isTemperatureControl) {
		this.isTemperatureControl = isTemperatureControl;
	}
	
	@ExcelField(title="车辆温别", dictType="TMS_TEMPERATURE_TYPE", align=2, sort=30)
	public String getTemperatureType() {
		return temperatureType;
	}

	public void setTemperatureType(String temperatureType) {
		this.temperatureType = temperatureType;
	}
	
	@ExcelField(title="最低温", align=2, sort=31)
	public String getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(String minTemperature) {
		this.minTemperature = minTemperature;
	}
	
	@ExcelField(title="最高温", align=2, sort=32)
	public String getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(String maxTemperature) {
		this.maxTemperature = maxTemperature;
	}
	
	@ExcelField(title="制冷设备编码", align=2, sort=33)
	public String getRefrigerationEquipmentCode() {
		return refrigerationEquipmentCode;
	}

	public void setRefrigerationEquipmentCode(String refrigerationEquipmentCode) {
		this.refrigerationEquipmentCode = refrigerationEquipmentCode;
	}
	
	@ExcelField(title="是否危品车", dictType="SYS_YES_NO", align=2, sort=34)
	public String getIsRisk() {
		return isRisk;
	}

	public void setIsRisk(String isRisk) {
		this.isRisk = isRisk;
	}
	
	@ExcelField(title="危险等级", dictType="TMS_RISK_LEVEL", align=2, sort=35)
	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="购车时间", align=2, sort=36)
	public Date getBuyingTime() {
		return buyingTime;
	}

	public void setBuyingTime(Date buyingTime) {
		this.buyingTime = buyingTime;
	}
	
	@ExcelField(title="购车地点", align=2, sort=37)
	public String getPurchaseLocation() {
		return purchaseLocation;
	}

	public void setPurchaseLocation(String purchaseLocation) {
		this.purchaseLocation = purchaseLocation;
	}
	
	@ExcelField(title="购车金额", align=2, sort=38)
	public Double getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(Double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	
	@ExcelField(title="车辆排放标准", align=2, sort=39)
	public String getEmissionStandard() {
		return emissionStandard;
	}

	public void setEmissionStandard(String emissionStandard) {
		this.emissionStandard = emissionStandard;
	}
	
	@ExcelField(title="油耗(升)", align=2, sort=40)
	public Double getOilConsumption() {
		return oilConsumption;
	}

	public void setOilConsumption(Double oilConsumption) {
		this.oilConsumption = oilConsumption;
	}
	
	@ExcelField(title="行车里程(公里)", align=2, sort=41)
	public Double getMileage() {
		return mileage;
	}

	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}
	
	@ExcelField(title="马力(匹)", align=2, sort=42)
	public Double getHorsepower() {
		return horsepower;
	}

	public void setHorsepower(Double horsepower) {
		this.horsepower = horsepower;
	}
	
	@ExcelField(title="折旧年限(年)", align=2, sort=43)
	public Double getDepreciableLife() {
		return depreciableLife;
	}

	public void setDepreciableLife(Double depreciableLife) {
		this.depreciableLife = depreciableLife;
	}
	
	@ExcelField(title="报废年限(年)", align=2, sort=44)
	public Double getScrappedLife() {
		return scrappedLife;
	}

	public void setScrappedLife(Double scrappedLife) {
		this.scrappedLife = scrappedLife;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="启动时间", align=2, sort=45)
	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="报废时间", align=2, sort=46)
	public Date getScrappedTime() {
		return scrappedTime;
	}

	public void setScrappedTime(Date scrappedTime) {
		this.scrappedTime = scrappedTime;
	}
	
	@ExcelField(title="残值率(%)", align=2, sort=47)
	public Double getSalvageRate() {
		return salvageRate;
	}

	public void setSalvageRate(Double salvageRate) {
		this.salvageRate = salvageRate;
	}
	
	@ExcelField(title="轴数", align=2, sort=48)
	public Long getAxleNumber() {
		return axleNumber;
	}

	public void setAxleNumber(Long axleNumber) {
		this.axleNumber = axleNumber;
	}
	
	@ExcelField(title="发动机号", align=2, sort=49)
	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	
	@ExcelField(title="用油型号", align=2, sort=50)
	public String getOilType() {
		return oilType;
	}

	public void setOilType(String oilType) {
		this.oilType = oilType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="注册时间", align=2, sort=51)
	public Date getRegisteredTime() {
		return registeredTime;
	}

	public void setRegisteredTime(Date registeredTime) {
		this.registeredTime = registeredTime;
	}
	
	@ExcelField(title="注册地点", align=2, sort=52)
	public String getRegisteredLocation() {
		return registeredLocation;
	}

	public void setRegisteredLocation(String registeredLocation) {
		this.registeredLocation = registeredLocation;
	}
	
	@ExcelField(title="行驶证号", align=2, sort=53)
	public String getDrivingLicenseNo() {
		return drivingLicenseNo;
	}

	public void setDrivingLicenseNo(String drivingLicenseNo) {
		this.drivingLicenseNo = drivingLicenseNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="行驶证有效期", align=2, sort=54)
	public Date getDrivingLicenseExpiryTime() {
		return drivingLicenseExpiryTime;
	}

	public void setDrivingLicenseExpiryTime(Date drivingLicenseExpiryTime) {
		this.drivingLicenseExpiryTime = drivingLicenseExpiryTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="运营证有效期", align=2, sort=55)
	public Date getOperatingLicenseExpiryTime() {
		return operatingLicenseExpiryTime;
	}

	public void setOperatingLicenseExpiryTime(Date operatingLicenseExpiryTime) {
		this.operatingLicenseExpiryTime = operatingLicenseExpiryTime;
	}
	
	@ExcelField(title="运营时长", align=2, sort=56)
	public Double getOperatingDuration() {
		return operatingDuration;
	}

	public void setOperatingDuration(Double operatingDuration) {
		this.operatingDuration = operatingDuration;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="通行缴费时间", align=2, sort=57)
	public Date getTollCollectionTime() {
		return tollCollectionTime;
	}

	public void setTollCollectionTime(Date tollCollectionTime) {
		this.tollCollectionTime = tollCollectionTime;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public Date getAnnualReviewExpiryTime() {
		return annualReviewExpiryTime;
	}

	public void setAnnualReviewExpiryTime(Date annualReviewExpiryTime) {
		this.annualReviewExpiryTime = annualReviewExpiryTime;
	}

	public Date getTwoDimensionExpiryTime() {
		return twoDimensionExpiryTime;
	}

	public void setTwoDimensionExpiryTime(Date twoDimensionExpiryTime) {
		this.twoDimensionExpiryTime = twoDimensionExpiryTime;
	}

	public Date getTankInspectionExpiryTime() {
		return tankInspectionExpiryTime;
	}

	public void setTankInspectionExpiryTime(Date tankInspectionExpiryTime) {
		this.tankInspectionExpiryTime = tankInspectionExpiryTime;
	}

	public Date getInsuranceExpiryTime() {
		return insuranceExpiryTime;
	}

	public void setInsuranceExpiryTime(Date insuranceExpiryTime) {
		this.insuranceExpiryTime = insuranceExpiryTime;
	}

	public Integer getVehicleMaintenanceMileage() {
		return vehicleMaintenanceMileage;
	}

	public void setVehicleMaintenanceMileage(Integer vehicleMaintenanceMileage) {
		this.vehicleMaintenanceMileage = vehicleMaintenanceMileage;
	}

	public Integer getVehicleApplyGreaseMileage() {
		return vehicleApplyGreaseMileage;
	}

	public void setVehicleApplyGreaseMileage(Integer vehicleApplyGreaseMileage) {
		this.vehicleApplyGreaseMileage = vehicleApplyGreaseMileage;
	}

	public Integer getVehicleOilChangeMileage() {
		return vehicleOilChangeMileage;
	}

	public void setVehicleOilChangeMileage(Integer vehicleOilChangeMileage) {
		this.vehicleOilChangeMileage = vehicleOilChangeMileage;
	}

	public List<SysTmsVehiclePart> getVehiclePartList() {
		return vehiclePartList;
	}

	public void setVehiclePartList(List<SysTmsVehiclePart> vehiclePartList) {
		this.vehiclePartList = vehiclePartList;
	}

	public List<SysTmsVehicleQualification> getVehicleQualificationList() {
		return vehicleQualificationList;
	}

	public void setVehicleQualificationList(List<SysTmsVehicleQualification> vehicleQualificationList) {
		this.vehicleQualificationList = vehicleQualificationList;
	}

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}
}