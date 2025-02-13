package com.yunyou.modules.tms.basic.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 运输人员信息Entity
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmDriver extends DataEntity<TmDriver> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 编码
	private String name;		// 姓名
	private String carrierCode;		// 承运商编码
	private String phone;		// 手机
	private String idCard;		// 身份证
	private Date birthDate;		// 出生日期
	private String tempResidenceCertificateNo;		// 暂住证号
	private String nation;		// 民族
	private String nativePlace;		// 籍贯
	private String educationLevel;		// 文化程度
	private String maritalStatus;		// 婚姻状况
	private String politicalAffiliation;		// 政治面貌
	private String allowDriverCarType;		// 准驾车型
	private String personnelNature;		// 人员性质
	private Integer drivingAge;		// 驾龄
	private String isMilitaryService;		// 是否服兵役
	private String isInternalDriver;		// 是否内部驾驶员
	private String emergencyContact;		// 紧急联系人
	private String emergencyContactRelation;		// 与紧急联系人关系
	private String emergencyContactTel;		// 紧急联系电话
	private String currentAddress;		// 现居地址
	private String registeredAddress;		// 户口地址
	private String contractNo;		// 劳务合同号
	private Date reportDate;		// 到岗日期
	private Date contractExpireDate;		// 合同有效期
	private Double basicWage;		// 基本工资
	private String employmentQualificationCertificateNo;		// 从业资格证号
	private String comprehensiveInsuranceNo;		// 综合保险号
	private String socialInsuranceNo;		// 社会保险号
	private String wordCardNo;		// 工作卡号
	private String driverLicenseNo;		// 驾驶证号
	private String driverLicenseType;		// 驾驶证类型
	private Date firstReceiveLicenseDate;		// 初领证日期
	private Date driverLicenseAnnualInspectionDate;		// 驾驶证年检日期
	private Integer deductPoint;		// 交通违规扣分
	private Date banDrivingDateFm;		// 停运日期从
	private Date banDriverDateTo;		// 停运日期到
	private String banDrivingReason;		// 停运原因
	private Integer height;		// 身高
	private Double weight;		// 体重
	private String bloodType;		// 血型
	private String vision;		// 视力
	private String shoeSize;		// 鞋码
	private String health;		// 健康状况
	private String healthCertificateNo;		// 健康证号
	private Date healthCertificateExpireDate;		// 健康证失效日期
	private String mentalityQuality;		// 心理素质
	private String interpersonalRelationship;		// 人际关系
	private String teamSpirit;		// 团队精神
	private String orgId;		// 机构ID
	private String account;		// 账号

	public TmDriver() {
		super();
	}

	public TmDriver(String id){
		super(id);
	}

	public TmDriver(String code, String orgId) {
		this.code = code;
		this.orgId = orgId;
	}

	@ExcelField(title="编码", align=2, sort=7)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="姓名", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="承运商编码", align=2, sort=9)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	@ExcelField(title="手机", align=2, sort=10)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="身份证", align=2, sort=11)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="出生日期", align=2, sort=12)
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	@ExcelField(title="暂住证号", align=2, sort=13)
	public String getTempResidenceCertificateNo() {
		return tempResidenceCertificateNo;
	}

	public void setTempResidenceCertificateNo(String tempResidenceCertificateNo) {
		this.tempResidenceCertificateNo = tempResidenceCertificateNo;
	}
	
	@ExcelField(title="民族", align=2, sort=14)
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	
	@ExcelField(title="籍贯", align=2, sort=15)
	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	
	@ExcelField(title="文化程度", dictType="SYS_EDUCATION_LEVEL", align=2, sort=16)
	public String getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}
	
	@ExcelField(title="婚姻状况", dictType="SYS_MARITAL_STATUS", align=2, sort=17)
	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	
	@ExcelField(title="政治面貌", dictType="SYS_POLITICAL_AFFILIATION", align=2, sort=18)
	public String getPoliticalAffiliation() {
		return politicalAffiliation;
	}

	public void setPoliticalAffiliation(String politicalAffiliation) {
		this.politicalAffiliation = politicalAffiliation;
	}
	
	@ExcelField(title="准驾车型", align=2, sort=19)
	public String getAllowDriverCarType() {
		return allowDriverCarType;
	}

	public void setAllowDriverCarType(String allowDriverCarType) {
		this.allowDriverCarType = allowDriverCarType;
	}
	
	@ExcelField(title="人员性质", dictType = "TMS_PERSONNEL_NATURE", align=2, sort=20)
	public String getPersonnelNature() {
		return personnelNature;
	}

	public void setPersonnelNature(String personnelNature) {
		this.personnelNature = personnelNature;
	}
	
	@ExcelField(title="驾龄", align=2, sort=21)
	public Integer getDrivingAge() {
		return drivingAge;
	}

	public void setDrivingAge(Integer drivingAge) {
		this.drivingAge = drivingAge;
	}
	
	@ExcelField(title="是否服兵役", dictType="SYS_YES_NO", align=2, sort=22)
	public String getIsMilitaryService() {
		return isMilitaryService;
	}

	public void setIsMilitaryService(String isMilitaryService) {
		this.isMilitaryService = isMilitaryService;
	}
	
	@ExcelField(title="是否内部驾驶员", dictType="SYS_YES_NO", align=2, sort=23)
	public String getIsInternalDriver() {
		return isInternalDriver;
	}

	public void setIsInternalDriver(String isInternalDriver) {
		this.isInternalDriver = isInternalDriver;
	}
	
	@ExcelField(title="紧急联系人", align=2, sort=24)
	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	
	@ExcelField(title="与紧急联系人关系", align=2, sort=25)
	public String getEmergencyContactRelation() {
		return emergencyContactRelation;
	}

	public void setEmergencyContactRelation(String emergencyContactRelation) {
		this.emergencyContactRelation = emergencyContactRelation;
	}
	
	@ExcelField(title="紧急联系电话", align=2, sort=26)
	public String getEmergencyContactTel() {
		return emergencyContactTel;
	}

	public void setEmergencyContactTel(String emergencyContactTel) {
		this.emergencyContactTel = emergencyContactTel;
	}
	
	@ExcelField(title="现居地址", align=2, sort=27)
	public String getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}
	
	@ExcelField(title="户口地址", align=2, sort=28)
	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}
	
	@ExcelField(title="劳务合同号", align=2, sort=29)
	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="到岗日期", align=2, sort=30)
	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="合同有效期", align=2, sort=31)
	public Date getContractExpireDate() {
		return contractExpireDate;
	}

	public void setContractExpireDate(Date contractExpireDate) {
		this.contractExpireDate = contractExpireDate;
	}
	
	@ExcelField(title="基本工资", align=2, sort=32)
	public Double getBasicWage() {
		return basicWage;
	}

	public void setBasicWage(Double basicWage) {
		this.basicWage = basicWage;
	}
	
	@ExcelField(title="从业资格证号", align=2, sort=33)
	public String getEmploymentQualificationCertificateNo() {
		return employmentQualificationCertificateNo;
	}

	public void setEmploymentQualificationCertificateNo(String employmentQualificationCertificateNo) {
		this.employmentQualificationCertificateNo = employmentQualificationCertificateNo;
	}
	
	@ExcelField(title="综合保险号", align=2, sort=34)
	public String getComprehensiveInsuranceNo() {
		return comprehensiveInsuranceNo;
	}

	public void setComprehensiveInsuranceNo(String comprehensiveInsuranceNo) {
		this.comprehensiveInsuranceNo = comprehensiveInsuranceNo;
	}
	
	@ExcelField(title="社会保险号", align=2, sort=35)
	public String getSocialInsuranceNo() {
		return socialInsuranceNo;
	}

	public void setSocialInsuranceNo(String socialInsuranceNo) {
		this.socialInsuranceNo = socialInsuranceNo;
	}
	
	@ExcelField(title="工作卡号", align=2, sort=36)
	public String getWordCardNo() {
		return wordCardNo;
	}

	public void setWordCardNo(String wordCardNo) {
		this.wordCardNo = wordCardNo;
	}
	
	@ExcelField(title="驾驶证号", align=2, sort=37)
	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}

	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}
	
	@ExcelField(title="驾驶证类型", dictType="TMS_DRIVER_LICENSE_TYPE", align=2, sort=38)
	public String getDriverLicenseType() {
		return driverLicenseType;
	}

	public void setDriverLicenseType(String driverLicenseType) {
		this.driverLicenseType = driverLicenseType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="初领证日期", align=2, sort=39)
	public Date getFirstReceiveLicenseDate() {
		return firstReceiveLicenseDate;
	}

	public void setFirstReceiveLicenseDate(Date firstReceiveLicenseDate) {
		this.firstReceiveLicenseDate = firstReceiveLicenseDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="驾驶证年检日期", align=2, sort=40)
	public Date getDriverLicenseAnnualInspectionDate() {
		return driverLicenseAnnualInspectionDate;
	}

	public void setDriverLicenseAnnualInspectionDate(Date driverLicenseAnnualInspectionDate) {
		this.driverLicenseAnnualInspectionDate = driverLicenseAnnualInspectionDate;
	}
	
	@ExcelField(title="交通违规扣分", align=2, sort=41)
	public Integer getDeductPoint() {
		return deductPoint;
	}

	public void setDeductPoint(Integer deductPoint) {
		this.deductPoint = deductPoint;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="停运日期从", align=2, sort=42)
	public Date getBanDrivingDateFm() {
		return banDrivingDateFm;
	}

	public void setBanDrivingDateFm(Date banDrivingDateFm) {
		this.banDrivingDateFm = banDrivingDateFm;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="停运日期到", align=2, sort=43)
	public Date getBanDriverDateTo() {
		return banDriverDateTo;
	}

	public void setBanDriverDateTo(Date banDriverDateTo) {
		this.banDriverDateTo = banDriverDateTo;
	}
	
	@ExcelField(title="停运原因", align=2, sort=44)
	public String getBanDrivingReason() {
		return banDrivingReason;
	}

	public void setBanDrivingReason(String banDrivingReason) {
		this.banDrivingReason = banDrivingReason;
	}
	
	@ExcelField(title="身高", align=2, sort=45)
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	
	@ExcelField(title="体重", align=2, sort=46)
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	@ExcelField(title="血型", dictType="SYS_BLOOD_TYPE", align=2, sort=47)
	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	
	@ExcelField(title="视力", align=2, sort=48)
	public String getVision() {
		return vision;
	}

	public void setVision(String vision) {
		this.vision = vision;
	}
	
	@ExcelField(title="鞋码", align=2, sort=49)
	public String getShoeSize() {
		return shoeSize;
	}

	public void setShoeSize(String shoeSize) {
		this.shoeSize = shoeSize;
	}
	
	@ExcelField(title="健康状况", dictType="SYS_HEALTH", align=2, sort=50)
	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}
	
	@ExcelField(title="健康证号", align=2, sort=51)
	public String getHealthCertificateNo() {
		return healthCertificateNo;
	}

	public void setHealthCertificateNo(String healthCertificateNo) {
		this.healthCertificateNo = healthCertificateNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="健康证失效日期", align=2, sort=52)
	public Date getHealthCertificateExpireDate() {
		return healthCertificateExpireDate;
	}

	public void setHealthCertificateExpireDate(Date healthCertificateExpireDate) {
		this.healthCertificateExpireDate = healthCertificateExpireDate;
	}
	
	@ExcelField(title="心理素质", align=2, sort=53)
	public String getMentalityQuality() {
		return mentalityQuality;
	}

	public void setMentalityQuality(String mentalityQuality) {
		this.mentalityQuality = mentalityQuality;
	}
	
	@ExcelField(title="人际关系", align=2, sort=54)
	public String getInterpersonalRelationship() {
		return interpersonalRelationship;
	}

	public void setInterpersonalRelationship(String interpersonalRelationship) {
		this.interpersonalRelationship = interpersonalRelationship;
	}
	
	@ExcelField(title="团队精神", align=2, sort=55)
	public String getTeamSpirit() {
		return teamSpirit;
	}

	public void setTeamSpirit(String teamSpirit) {
		this.teamSpirit = teamSpirit;
	}
	
	@ExcelField(title="机构ID", align=2, sort=56)
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
}