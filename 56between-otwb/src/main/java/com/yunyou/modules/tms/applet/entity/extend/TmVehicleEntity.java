package com.yunyou.modules.tms.applet.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TmVehicleEntity implements Serializable {

    private String carNo;		// 车牌号
    private String transportEquipmentTypeCode;  // 设备类型编码
    private String transportEquipmentTypeName; // 运输设备类型名称
    private String carrierCode;		// 承运商编码
    private String carrierName;     // 承运商名称
    private String carType;		    // 车辆类型
    private String dispatchBase;	// 调度基地
    private String dispatchBaseName;// 调度基地名称
    private String mainDriver;		// 主驾驶
    private String mainDriverName;  // 主驾驶姓名
    private String mainDriverTel;   // 主驾驶电话
    private String copilot;		    // 副驾驶
    private String copilotName;     // 副驾驶姓名
    private String trailer;		    // 挂车
    private String carBrand;		// 车辆品牌
    private String carModel;		// 车辆型号
    private String carColor;		// 车辆颜色
    private String carBodyNo;		// 车身号码
    private String supplierCode;	// 供应商编码
    private String supplierName;    // 供应商名称
    private String ownership;		// 所有权
    private Double approvedLoadingWeight;	// 核定装载重量
    private Double approvedLoadingCubic;	// 核定装载体积
    private Double totalTractionWeight;		// 牵引总重量(吨)
    private Double equipmentQuality;		// 装备质量(吨)
    private Integer doorNumber;		// 车门个数
    private Double length;		    // 车长
    private Double width;		    // 车宽
    private Double height;		    // 车高
    private String isTemperatureControl;	// 是否温控
    private String temperatureType;		// 车辆温别
    private String minTemperature;		// 最低温
    private String maxTemperature;		// 最高温
    private String refrigerationEquipmentCode;	// 制冷设备编码
    private String isRisk;		    // 是否危品车
    private String riskLevel;		// 危险等级
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date buyingTime;		// 购车时间
    private String purchaseLocation;	// 购车地点
    private Double purchaseAmount;		// 购车金额
    private String emissionStandard;	// 车辆排放标准
    private Double oilConsumption;		// 油耗(升)
    private Double mileage;		    // 行车里程(公里)
    private Double horsepower;		// 马力(匹)
    private Double depreciableLife;	// 折旧年限(年)
    private Double scrappedLife;	// 报废年限(年)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activeTime;		// 启动时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scrappedTime;		// 报废时间
    private Double salvageRate;		// 残值率(%)
    private Long axleNumber;		// 轴数
    private String engineNo;		// 发动机号
    private String oilType;		    // 用油型号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registeredTime;	// 注册时间
    private String registeredLocation;	// 注册地点
    private String drivingLicenseNo;	// 行驶证号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date drivingLicenseExpiryTime;	// 行驶证有效期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operatingLicenseExpiryTime;// 运营证有效期
    private Double operatingDuration;		// 运营时长
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tollCollectionTime;		// 通行缴费时间
    private String orgId;		    // 机构ID
    private String status;          // 状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date annualReviewExpiryTime;        // 车辆年审到期时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date twoDimensionExpiryTime;        // 车辆二维到期时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tankInspectionExpiryTime;      // 车辆罐检到期时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insuranceExpiryTime;           // 保险有限期
    private Integer vehicleMaintenanceMileage;  // 车辆保养里程数
    private Integer vehicleApplyGreaseMileage;  // 车辆打黄油保养里程数
    private Integer vehicleOilChangeMileage;    // 车辆换机油保养里程数

    private List<String> carNoList = Lists.newArrayList();      // 查询条件-车牌号

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getTransportEquipmentTypeCode() {
        return transportEquipmentTypeCode;
    }

    public void setTransportEquipmentTypeCode(String transportEquipmentTypeCode) {
        this.transportEquipmentTypeCode = transportEquipmentTypeCode;
    }

    public String getTransportEquipmentTypeName() {
        return transportEquipmentTypeName;
    }

    public void setTransportEquipmentTypeName(String transportEquipmentTypeName) {
        this.transportEquipmentTypeName = transportEquipmentTypeName;
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

    public String getDispatchBase() {
        return dispatchBase;
    }

    public void setDispatchBase(String dispatchBase) {
        this.dispatchBase = dispatchBase;
    }

    public String getDispatchBaseName() {
        return dispatchBaseName;
    }

    public void setDispatchBaseName(String dispatchBaseName) {
        this.dispatchBaseName = dispatchBaseName;
    }

    public String getMainDriver() {
        return mainDriver;
    }

    public void setMainDriver(String mainDriver) {
        this.mainDriver = mainDriver;
    }

    public String getMainDriverName() {
        return mainDriverName;
    }

    public void setMainDriverName(String mainDriverName) {
        this.mainDriverName = mainDriverName;
    }

    public String getMainDriverTel() {
        return mainDriverTel;
    }

    public void setMainDriverTel(String mainDriverTel) {
        this.mainDriverTel = mainDriverTel;
    }

    public String getCopilot() {
        return copilot;
    }

    public void setCopilot(String copilot) {
        this.copilot = copilot;
    }

    public String getCopilotName() {
        return copilotName;
    }

    public void setCopilotName(String copilotName) {
        this.copilotName = copilotName;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarBodyNo() {
        return carBodyNo;
    }

    public void setCarBodyNo(String carBodyNo) {
        this.carBodyNo = carBodyNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public Double getApprovedLoadingWeight() {
        return approvedLoadingWeight;
    }

    public void setApprovedLoadingWeight(Double approvedLoadingWeight) {
        this.approvedLoadingWeight = approvedLoadingWeight;
    }

    public Double getApprovedLoadingCubic() {
        return approvedLoadingCubic;
    }

    public void setApprovedLoadingCubic(Double approvedLoadingCubic) {
        this.approvedLoadingCubic = approvedLoadingCubic;
    }

    public Double getTotalTractionWeight() {
        return totalTractionWeight;
    }

    public void setTotalTractionWeight(Double totalTractionWeight) {
        this.totalTractionWeight = totalTractionWeight;
    }

    public Double getEquipmentQuality() {
        return equipmentQuality;
    }

    public void setEquipmentQuality(Double equipmentQuality) {
        this.equipmentQuality = equipmentQuality;
    }

    public Integer getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(Integer doorNumber) {
        this.doorNumber = doorNumber;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getIsTemperatureControl() {
        return isTemperatureControl;
    }

    public void setIsTemperatureControl(String isTemperatureControl) {
        this.isTemperatureControl = isTemperatureControl;
    }

    public String getTemperatureType() {
        return temperatureType;
    }

    public void setTemperatureType(String temperatureType) {
        this.temperatureType = temperatureType;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getRefrigerationEquipmentCode() {
        return refrigerationEquipmentCode;
    }

    public void setRefrigerationEquipmentCode(String refrigerationEquipmentCode) {
        this.refrigerationEquipmentCode = refrigerationEquipmentCode;
    }

    public String getIsRisk() {
        return isRisk;
    }

    public void setIsRisk(String isRisk) {
        this.isRisk = isRisk;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Date getBuyingTime() {
        return buyingTime;
    }

    public void setBuyingTime(Date buyingTime) {
        this.buyingTime = buyingTime;
    }

    public String getPurchaseLocation() {
        return purchaseLocation;
    }

    public void setPurchaseLocation(String purchaseLocation) {
        this.purchaseLocation = purchaseLocation;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getEmissionStandard() {
        return emissionStandard;
    }

    public void setEmissionStandard(String emissionStandard) {
        this.emissionStandard = emissionStandard;
    }

    public Double getOilConsumption() {
        return oilConsumption;
    }

    public void setOilConsumption(Double oilConsumption) {
        this.oilConsumption = oilConsumption;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(Double horsepower) {
        this.horsepower = horsepower;
    }

    public Double getDepreciableLife() {
        return depreciableLife;
    }

    public void setDepreciableLife(Double depreciableLife) {
        this.depreciableLife = depreciableLife;
    }

    public Double getScrappedLife() {
        return scrappedLife;
    }

    public void setScrappedLife(Double scrappedLife) {
        this.scrappedLife = scrappedLife;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Date getScrappedTime() {
        return scrappedTime;
    }

    public void setScrappedTime(Date scrappedTime) {
        this.scrappedTime = scrappedTime;
    }

    public Double getSalvageRate() {
        return salvageRate;
    }

    public void setSalvageRate(Double salvageRate) {
        this.salvageRate = salvageRate;
    }

    public Long getAxleNumber() {
        return axleNumber;
    }

    public void setAxleNumber(Long axleNumber) {
        this.axleNumber = axleNumber;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }

    public Date getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(Date registeredTime) {
        this.registeredTime = registeredTime;
    }

    public String getRegisteredLocation() {
        return registeredLocation;
    }

    public void setRegisteredLocation(String registeredLocation) {
        this.registeredLocation = registeredLocation;
    }

    public String getDrivingLicenseNo() {
        return drivingLicenseNo;
    }

    public void setDrivingLicenseNo(String drivingLicenseNo) {
        this.drivingLicenseNo = drivingLicenseNo;
    }

    public Date getDrivingLicenseExpiryTime() {
        return drivingLicenseExpiryTime;
    }

    public void setDrivingLicenseExpiryTime(Date drivingLicenseExpiryTime) {
        this.drivingLicenseExpiryTime = drivingLicenseExpiryTime;
    }

    public Date getOperatingLicenseExpiryTime() {
        return operatingLicenseExpiryTime;
    }

    public void setOperatingLicenseExpiryTime(Date operatingLicenseExpiryTime) {
        this.operatingLicenseExpiryTime = operatingLicenseExpiryTime;
    }

    public Double getOperatingDuration() {
        return operatingDuration;
    }

    public void setOperatingDuration(Double operatingDuration) {
        this.operatingDuration = operatingDuration;
    }

    public Date getTollCollectionTime() {
        return tollCollectionTime;
    }

    public void setTollCollectionTime(Date tollCollectionTime) {
        this.tollCollectionTime = tollCollectionTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public List<String> getCarNoList() {
        return carNoList;
    }

    public void setCarNoList(List<String> carNoList) {
        this.carNoList = carNoList;
    }
}