package com.yunyou.modules.sys.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 客户Entity
 */
public class SysOmsCustomer extends DataEntity<SysOmsCustomer> {

    private static final long serialVersionUID = 1L;
    private String ebcuQuickCode;// 快速录入码
    private String ebcuCustomerNo;// 客户代码
    private String pmCode;// 客户代码
    private String ebcuIndustryType;// 行业类型
    private String ebcuPlatformNature;// 平台性质:1民营,2国有,3外资
    private String ebcuNameCn;// 中文名称
    private String ebcuShortName;// 简称
    private String ebcuNameEn;// 英文名称
    private String ebcuTel;// 电话
    private String ebcuFax;// 传真
    private String ebcuUrl;// 网址
    private String ebcuAddress;// 地址
    private String ebcuFinanceCode;// 财务代码
    private String ebcuTaxRegistNo;// 平台税务登记号
    private String ebcuBusinessNo;// 工商登记号
    private String ebcuEbflId;// 客户评估审计
    private String ebcuCustomerStatus;// 客户状态:0启用， 1停用
    private Date ebcuRegistrationDate;// 注册日期
    private String ebcuMaxGuaranteeAmount;// 担保额度
    private String ebcuMainBusiness;// 主营业务
    private String ebcuIsSubsupplier;// 是否是分供方Y:是N:否
    private String ebcuIsCustomer;// 是否是分供方Y:是N:否
    private String ebcuIsSettlement;// 是否是结算对象Y:是N:否
    private String ebcuRemark;// 备注
    private String ebcuEscoId;// 所属公司ID
    private String ebcuCdhCode;// CDH系统反馈编号

    private String ebcuSubstr1;// 预留字段1
    private String ebcuSubstr2;// 预留字段2
    private String ebcuSubstr3;// 预留字段3
    private String ebcuSubstr4;// 预留字段4
    private String ebcuSubstr5;// 预留字段5
    private String ebcuSubstr6;// 预留字段6
    private String ebcuSubstr7;// 预留字段7
    private String ebcuSubstr8;// 预留字段8

    private Date ebcuSubdate1;// 预留字段7
    private Date ebcuSubdate2;// 预留字段
    private Date ebcuSubdate3;// 预留字段
    private Date ebcuSubdate4;// 预留字段
    private Date ebcuSubdate5;// 预留字段

    private String ebcuSubnum1;// 预留字段
    private String ebcuSubnum2;// 预留字段
    private String ebcuSubnum3;// 预留字段

    private String ebcuChangeCode;// 交换平台代码
    private String dataSet;// 数据套
    private String timeZone;// 时区
    private String ebcuEbplCountryCode;// 国家
    private String ebcuEbplProvinceCode;// 省
    private String ebcuEbplCityCode;// 市
    private String ebcuIsGeneralTaxpayer;// 是否一般纳税人
    private String ebcuZipCode;// 邮编
    private Double ebcuTaxRate;// 税率
    private Double ebcuTaxRateValue;// 税率值
    private Double ebcuExchangeRate;// 汇率
    private String ebcuCurrency;// 币种
    private Double ebcuOrderAmountLimit;// 订单总金额上限额度
    private Double ebcuDistributeInterval;// 分发间隔
    private Double ebcuIntervalCapacity;// 分发间隔饱和量
    private Double ebcuDefaultAcceptTime;// 分发默认接收时长
    private Double ebcuDefaultRejectTime;// 分发默认拒绝时长
    private String ebcuDispatchIssueType;// 下发方式
    private String ebcuDistributePriority;// 分发优先级
    private String ebcuType;// 客户类型
    private String clerkCode;// 业务员代码
    private String vipStatus;// 会员状态

    private String brand;// 品牌
    private String majorClass;// 大类
    private String rangeType;// 范围类型

    public SysOmsCustomer() {
        super();
    }

    public SysOmsCustomer(String id) {
        super(id);
    }

    public SysOmsCustomer(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    @ExcelField(title = "快速录入码", align = 2, sort = 1)
    public String getEbcuQuickCode() {
        return ebcuQuickCode;
    }

    public void setEbcuQuickCode(String ebcuQuickCode) {
        this.ebcuQuickCode = ebcuQuickCode;
    }

    @ExcelField(title = "客户代码", align = 2, sort = 2)
    public String getPmCode() {
        return pmCode;
    }

    public void setPmCode(String pmCode) {
        this.pmCode = pmCode;
    }

    @ExcelField(title = "行业类型:1化工,2物流,3仓储", align = 2, sort = 3)
    public String getEbcuIndustryType() {
        return ebcuIndustryType;
    }

    public void setEbcuIndustryType(String ebcuIndustryType) {
        this.ebcuIndustryType = ebcuIndustryType;
    }

    @ExcelField(title = "平台性质:1民营,2国有,3外资", align = 2, sort = 4)
    public String getEbcuPlatformNature() {
        return ebcuPlatformNature;
    }

    public void setEbcuPlatformNature(String ebcuPlatformNature) {
        this.ebcuPlatformNature = ebcuPlatformNature;
    }

    @ExcelField(title = "中文名称", align = 2, sort = 5)
    public String getEbcuNameCn() {
        return ebcuNameCn;
    }

    public void setEbcuNameCn(String ebcuNameCn) {
        this.ebcuNameCn = ebcuNameCn;
    }

    @ExcelField(title = "简称", align = 2, sort = 6)
    public String getEbcuShortName() {
        return ebcuShortName;
    }

    public void setEbcuShortName(String ebcuShortName) {
        this.ebcuShortName = ebcuShortName;
    }

    @ExcelField(title = "英文名称", align = 2, sort = 7)
    public String getEbcuNameEn() {
        return ebcuNameEn;
    }

    public void setEbcuNameEn(String ebcuNameEn) {
        this.ebcuNameEn = ebcuNameEn;
    }

    @ExcelField(title = "电话", align = 2, sort = 8)
    public String getEbcuTel() {
        return ebcuTel;
    }

    public void setEbcuTel(String ebcuTel) {
        this.ebcuTel = ebcuTel;
    }

    @ExcelField(title = "传真", align = 2, sort = 9)
    public String getEbcuFax() {
        return ebcuFax;
    }

    public void setEbcuFax(String ebcuFax) {
        this.ebcuFax = ebcuFax;
    }

    @ExcelField(title = "网址", align = 2, sort = 10)
    public String getEbcuUrl() {
        return ebcuUrl;
    }

    public void setEbcuUrl(String ebcuUrl) {
        this.ebcuUrl = ebcuUrl;
    }

    @ExcelField(title = "地址", align = 2, sort = 11)
    public String getEbcuAddress() {
        return ebcuAddress;
    }

    public void setEbcuAddress(String ebcuAddress) {
        this.ebcuAddress = ebcuAddress;
    }

    @ExcelField(title = "财务代码", align = 2, sort = 12)
    public String getEbcuFinanceCode() {
        return ebcuFinanceCode;
    }

    public void setEbcuFinanceCode(String ebcuFinanceCode) {
        this.ebcuFinanceCode = ebcuFinanceCode;
    }

    @ExcelField(title = "平台税务登记号", align = 2, sort = 13)
    public String getEbcuTaxRegistNo() {
        return ebcuTaxRegistNo;
    }

    public void setEbcuTaxRegistNo(String ebcuTaxRegistNo) {
        this.ebcuTaxRegistNo = ebcuTaxRegistNo;
    }

    @ExcelField(title = "工商登记号", align = 2, sort = 14)
    public String getEbcuBusinessNo() {
        return ebcuBusinessNo;
    }

    public void setEbcuBusinessNo(String ebcuBusinessNo) {
        this.ebcuBusinessNo = ebcuBusinessNo;
    }

    @ExcelField(title = "客户评估审计", align = 2, sort = 15)
    public String getEbcuEbflId() {
        return ebcuEbflId;
    }

    public void setEbcuEbflId(String ebcuEbflId) {
        this.ebcuEbflId = ebcuEbflId;
    }

    @ExcelField(title = "客户状态:ENABLE可用,DISABLE不可用,ADD新增", align = 2, sort = 16)
    public String getEbcuCustomerStatus() {
        return ebcuCustomerStatus;
    }

    public void setEbcuCustomerStatus(String ebcuCustomerStatus) {
        this.ebcuCustomerStatus = ebcuCustomerStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "注册日期", align = 2, sort = 17)
    public Date getEbcuRegistrationDate() {
        return ebcuRegistrationDate;
    }

    public void setEbcuRegistrationDate(Date ebcuRegistrationDate) {
        this.ebcuRegistrationDate = ebcuRegistrationDate;
    }

    @ExcelField(title = "担保额度", align = 2, sort = 18)
    public String getEbcuMaxGuaranteeAmount() {
        return ebcuMaxGuaranteeAmount;
    }

    public void setEbcuMaxGuaranteeAmount(String ebcuMaxGuaranteeAmount) {
        this.ebcuMaxGuaranteeAmount = ebcuMaxGuaranteeAmount;
    }

    @ExcelField(title = "主营业务", align = 2, sort = 19)
    public String getEbcuMainBusiness() {
        return ebcuMainBusiness;
    }

    public void setEbcuMainBusiness(String ebcuMainBusiness) {
        this.ebcuMainBusiness = ebcuMainBusiness;
    }

    @ExcelField(title = "是否是分供方Y:是N:否", align = 2, sort = 20)
    public String getEbcuIsSubsupplier() {
        return ebcuIsSubsupplier;
    }

    public void setEbcuIsSubsupplier(String ebcuIsSubsupplier) {
        this.ebcuIsSubsupplier = ebcuIsSubsupplier;
    }

    @ExcelField(title = "是否是分供方Y:是N:否", align = 2, sort = 21)
    public String getEbcuIsCustomer() {
        return ebcuIsCustomer;
    }

    public void setEbcuIsCustomer(String ebcuIsCustomer) {
        this.ebcuIsCustomer = ebcuIsCustomer;
    }

    @ExcelField(title = "是否是结算对象Y:是N:否", align = 2, sort = 22)
    public String getEbcuIsSettlement() {
        return ebcuIsSettlement;
    }

    public void setEbcuIsSettlement(String ebcuIsSettlement) {
        this.ebcuIsSettlement = ebcuIsSettlement;
    }

    @ExcelField(title = "备注", align = 2, sort = 23)
    public String getEbcuRemark() {
        return ebcuRemark;
    }

    public void setEbcuRemark(String ebcuRemark) {
        this.ebcuRemark = ebcuRemark;
    }

    @ExcelField(title = "所属公司ID", align = 2, sort = 24)
    public String getEbcuEscoId() {
        return ebcuEscoId;
    }

    public void setEbcuEscoId(String ebcuEscoId) {
        this.ebcuEscoId = ebcuEscoId;
    }

    @ExcelField(title = "预留字段1", align = 2, sort = 25)
    public String getEbcuSubstr1() {
        return ebcuSubstr1;
    }

    public void setEbcuSubstr1(String ebcuSubstr1) {
        this.ebcuSubstr1 = ebcuSubstr1;
    }

    @ExcelField(title = "预留字段2", align = 2, sort = 26)
    public String getEbcuSubstr2() {
        return ebcuSubstr2;
    }

    public void setEbcuSubstr2(String ebcuSubstr2) {
        this.ebcuSubstr2 = ebcuSubstr2;
    }

    @ExcelField(title = "预留字段3", align = 2, sort = 27)
    public String getEbcuSubstr3() {
        return ebcuSubstr3;
    }

    public void setEbcuSubstr3(String ebcuSubstr3) {
        this.ebcuSubstr3 = ebcuSubstr3;
    }

    @ExcelField(title = "预留字段4", align = 2, sort = 28)
    public String getEbcuSubstr4() {
        return ebcuSubstr4;
    }

    public void setEbcuSubstr4(String ebcuSubstr4) {
        this.ebcuSubstr4 = ebcuSubstr4;
    }

    @ExcelField(title = "预留字段5", align = 2, sort = 29)
    public String getEbcuSubstr5() {
        return ebcuSubstr5;
    }

    public void setEbcuSubstr5(String ebcuSubstr5) {
        this.ebcuSubstr5 = ebcuSubstr5;
    }

    @ExcelField(title = "预留字段6", align = 2, sort = 30)
    public String getEbcuSubstr6() {
        return ebcuSubstr6;
    }

    public void setEbcuSubstr6(String ebcuSubstr6) {
        this.ebcuSubstr6 = ebcuSubstr6;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预留字段7", align = 2, sort = 31)
    public Date getEbcuSubdate1() {
        return ebcuSubdate1;
    }

    public void setEbcuSubdate1(Date ebcuSubdate1) {
        this.ebcuSubdate1 = ebcuSubdate1;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预留字段", align = 2, sort = 32)
    public Date getEbcuSubdate2() {
        return ebcuSubdate2;
    }

    public void setEbcuSubdate2(Date ebcuSubdate2) {
        this.ebcuSubdate2 = ebcuSubdate2;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预留字段", align = 2, sort = 33)
    public Date getEbcuSubdate3() {
        return ebcuSubdate3;
    }

    public void setEbcuSubdate3(Date ebcuSubdate3) {
        this.ebcuSubdate3 = ebcuSubdate3;
    }

    @ExcelField(title = "CDH系统反馈编号", align = 2, sort = 39)
    public String getEbcuCdhCode() {
        return ebcuCdhCode;
    }

    public void setEbcuCdhCode(String ebcuCdhCode) {
        this.ebcuCdhCode = ebcuCdhCode;
    }

    @ExcelField(title = "预留字段", align = 2, sort = 40)
    public String getEbcuSubstr7() {
        return ebcuSubstr7;
    }

    public void setEbcuSubstr7(String ebcuSubstr7) {
        this.ebcuSubstr7 = ebcuSubstr7;
    }

    @ExcelField(title = "预留字段", align = 2, sort = 41)
    public String getEbcuSubstr8() {
        return ebcuSubstr8;
    }

    public void setEbcuSubstr8(String ebcuSubstr8) {
        this.ebcuSubstr8 = ebcuSubstr8;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预留字段", align = 2, sort = 42)
    public Date getEbcuSubdate4() {
        return ebcuSubdate4;
    }

    public void setEbcuSubdate4(Date ebcuSubdate4) {
        this.ebcuSubdate4 = ebcuSubdate4;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预留字段", align = 2, sort = 43)
    public Date getEbcuSubdate5() {
        return ebcuSubdate5;
    }

    public void setEbcuSubdate5(Date ebcuSubdate5) {
        this.ebcuSubdate5 = ebcuSubdate5;
    }

    @ExcelField(title = "预留字段", align = 2, sort = 44)
    public String getEbcuSubnum1() {
        return ebcuSubnum1;
    }

    public void setEbcuSubnum1(String ebcuSubnum1) {
        this.ebcuSubnum1 = ebcuSubnum1;
    }

    @ExcelField(title = "预留字段", align = 2, sort = 45)
    public String getEbcuSubnum2() {
        return ebcuSubnum2;
    }

    public void setEbcuSubnum2(String ebcuSubnum2) {
        this.ebcuSubnum2 = ebcuSubnum2;
    }

    @ExcelField(title = "预留字段", align = 2, sort = 46)
    public String getEbcuSubnum3() {
        return ebcuSubnum3;
    }

    public void setEbcuSubnum3(String ebcuSubnum3) {
        this.ebcuSubnum3 = ebcuSubnum3;
    }

    @ExcelField(title = "交换平台代码", align = 2, sort = 47)
    public String getEbcuChangeCode() {
        return ebcuChangeCode;
    }

    public void setEbcuChangeCode(String ebcuChangeCode) {
        this.ebcuChangeCode = ebcuChangeCode;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    @ExcelField(title = "时区", align = 2, sort = 49)
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @ExcelField(title = "国家", align = 2, sort = 51)
    public String getEbcuEbplCountryCode() {
        return ebcuEbplCountryCode;
    }

    public void setEbcuEbplCountryCode(String ebcuEbplCountryCode) {
        this.ebcuEbplCountryCode = ebcuEbplCountryCode;
    }

    @ExcelField(title = "省", align = 2, sort = 52)
    public String getEbcuEbplProvinceCode() {
        return ebcuEbplProvinceCode;
    }

    public void setEbcuEbplProvinceCode(String ebcuEbplProvinceCode) {
        this.ebcuEbplProvinceCode = ebcuEbplProvinceCode;
    }

    @ExcelField(title = "市", align = 2, sort = 53)
    public String getEbcuEbplCityCode() {
        return ebcuEbplCityCode;
    }

    public void setEbcuEbplCityCode(String ebcuEbplCityCode) {
        this.ebcuEbplCityCode = ebcuEbplCityCode;
    }

    @ExcelField(title = "客户代码", align = 2, sort = 54)
    public String getEbcuCustomerNo() {
        return ebcuCustomerNo;
    }

    public void setEbcuCustomerNo(String ebcuCustomerNo) {
        this.ebcuCustomerNo = ebcuCustomerNo;
    }

    @ExcelField(title = "是否一般纳税人", align = 2, sort = 55)
    public String getEbcuIsGeneralTaxpayer() {
        return ebcuIsGeneralTaxpayer;
    }

    public void setEbcuIsGeneralTaxpayer(String ebcuIsGeneralTaxpayer) {
        this.ebcuIsGeneralTaxpayer = ebcuIsGeneralTaxpayer;
    }

    @ExcelField(title = "邮编", align = 2, sort = 56)
    public String getEbcuZipCode() {
        return ebcuZipCode;
    }

    public void setEbcuZipCode(String ebcuZipCode) {
        this.ebcuZipCode = ebcuZipCode;
    }

    @ExcelField(title = "税率", align = 2, sort = 57)
    public Double getEbcuTaxRate() {
        return ebcuTaxRate;
    }

    public void setEbcuTaxRate(Double ebcuTaxRate) {
        this.ebcuTaxRate = ebcuTaxRate;
    }

    @ExcelField(title = "税率值", align = 2, sort = 58)
    public Double getEbcuTaxRateValue() {
        return ebcuTaxRateValue;
    }

    public void setEbcuTaxRateValue(Double ebcuTaxRateValue) {
        this.ebcuTaxRateValue = ebcuTaxRateValue;
    }

    @ExcelField(title = "订单总金额上限额度", align = 2, sort = 59)
    public Double getEbcuOrderAmountLimit() {
        return ebcuOrderAmountLimit;
    }

    public void setEbcuOrderAmountLimit(Double ebcuOrderAmountLimit) {
        this.ebcuOrderAmountLimit = ebcuOrderAmountLimit;
    }

    @ExcelField(title = "分发间隔", align = 2, sort = 60)
    public Double getEbcuDistributeInterval() {
        return ebcuDistributeInterval;
    }

    public void setEbcuDistributeInterval(Double ebcuDistributeInterval) {
        this.ebcuDistributeInterval = ebcuDistributeInterval;
    }

    @ExcelField(title = "分发间隔饱和量", align = 2, sort = 61)
    public Double getEbcuIntervalCapacity() {
        return ebcuIntervalCapacity;
    }

    public void setEbcuIntervalCapacity(Double ebcuIntervalCapacity) {
        this.ebcuIntervalCapacity = ebcuIntervalCapacity;
    }

    @ExcelField(title = "分发默认接收时长", align = 2, sort = 62)
    public Double getEbcuDefaultAcceptTime() {
        return ebcuDefaultAcceptTime;
    }

    public void setEbcuDefaultAcceptTime(Double ebcuDefaultAcceptTime) {
        this.ebcuDefaultAcceptTime = ebcuDefaultAcceptTime;
    }

    @ExcelField(title = "分发默认拒绝时长", align = 2, sort = 63)
    public Double getEbcuDefaultRejectTime() {
        return ebcuDefaultRejectTime;
    }

    public void setEbcuDefaultRejectTime(Double ebcuDefaultRejectTime) {
        this.ebcuDefaultRejectTime = ebcuDefaultRejectTime;
    }

    @ExcelField(title = "下发方式", align = 2, sort = 64)
    public String getEbcuDispatchIssueType() {
        return ebcuDispatchIssueType;
    }

    public void setEbcuDispatchIssueType(String ebcuDispatchIssueType) {
        this.ebcuDispatchIssueType = ebcuDispatchIssueType;
    }

    @ExcelField(title = "分发优先级", align = 2, sort = 65)
    public String getEbcuDistributePriority() {
        return ebcuDistributePriority;
    }

    public void setEbcuDistributePriority(String ebcuDistributePriority) {
        this.ebcuDistributePriority = ebcuDistributePriority;
    }

    public String getEbcuType() {
        return ebcuType;
    }

    public void setEbcuType(String ebcuType) {
        this.ebcuType = ebcuType;
    }

    public Double getEbcuExchangeRate() {
        return ebcuExchangeRate;
    }

    public void setEbcuExchangeRate(Double ebcuExchangeRate) {
        this.ebcuExchangeRate = ebcuExchangeRate;
    }

    public String getEbcuCurrency() {
        return ebcuCurrency;
    }

    public void setEbcuCurrency(String ebcuCurrency) {
        this.ebcuCurrency = ebcuCurrency;
    }

    public String getClerkCode() {
        return clerkCode;
    }

    public void setClerkCode(String clerkCode) {
        this.clerkCode = clerkCode;
    }

    public String getVipStatus() {
        return vipStatus;
    }

    public void setVipStatus(String vipStatus) {
        this.vipStatus = vipStatus;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMajorClass() {
        return majorClass;
    }

    public void setMajorClass(String majorClass) {
        this.majorClass = majorClass;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }
}