package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 客户Entity
 */
public class SysBmsCustomer extends DataEntity<SysBmsCustomer> {

    private static final long serialVersionUID = 1L;
    private String ebcuCustomerNo;// 客户代码
    private String ebcuNameCn;// 中文名称
    private String ebcuShortName;// 简称
    private String ebcuNameEn;// 英文名称
    private String ebcuType;// 客户类型
    private String ebcuIndustryType;// 行业类型:1化工,2物流,3仓储
    private String ebcuMainBusiness;// 主营业务
    private String ebcuTel;// 电话
    private String ebcuFax;// 传真
    private String ebcuZipCode;// 邮编
    private String ebcuAddress;// 地址
    private String ebcuFinanceCode;// 财务代码
    private String ebcuTaxRegistNo;// 平台税务登记号
    private String ebcuBusinessNo;// 工商登记号
    private Double ebcuTaxRate;// 税率
    private Double ebcuTaxRateValue;// 税率值
    private String ebcuIsGeneralTaxpayer;// 是否一般纳税人
    private String timeZone;// 时区
    private String dataSet;// 数据套
    private String checkPerson;// 对账人
    private String areaId;// 区域ID
    private String areaCode;// 区域编码
    private String areaName;// 区域名称
    private String project;// 项目
    private String dataSetName;// 数据套名称

    public SysBmsCustomer() {
        super();
    }

    public SysBmsCustomer(String id) {
        super(id);
    }

    public SysBmsCustomer(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getEbcuCustomerNo() {
        return ebcuCustomerNo;
    }

    public void setEbcuCustomerNo(String ebcuCustomerNo) {
        this.ebcuCustomerNo = ebcuCustomerNo;
    }

    public String getEbcuNameCn() {
        return ebcuNameCn;
    }

    public void setEbcuNameCn(String ebcuNameCn) {
        this.ebcuNameCn = ebcuNameCn;
    }

    public String getEbcuShortName() {
        return ebcuShortName;
    }

    public void setEbcuShortName(String ebcuShortName) {
        this.ebcuShortName = ebcuShortName;
    }

    public String getEbcuNameEn() {
        return ebcuNameEn;
    }

    public void setEbcuNameEn(String ebcuNameEn) {
        this.ebcuNameEn = ebcuNameEn;
    }

    public String getEbcuType() {
        return ebcuType;
    }

    public void setEbcuType(String ebcuType) {
        this.ebcuType = ebcuType;
    }

    public String getEbcuIndustryType() {
        return ebcuIndustryType;
    }

    public void setEbcuIndustryType(String ebcuIndustryType) {
        this.ebcuIndustryType = ebcuIndustryType;
    }

    public String getEbcuMainBusiness() {
        return ebcuMainBusiness;
    }

    public void setEbcuMainBusiness(String ebcuMainBusiness) {
        this.ebcuMainBusiness = ebcuMainBusiness;
    }

    public String getEbcuTel() {
        return ebcuTel;
    }

    public void setEbcuTel(String ebcuTel) {
        this.ebcuTel = ebcuTel;
    }

    public String getEbcuFax() {
        return ebcuFax;
    }

    public void setEbcuFax(String ebcuFax) {
        this.ebcuFax = ebcuFax;
    }

    public String getEbcuZipCode() {
        return ebcuZipCode;
    }

    public void setEbcuZipCode(String ebcuZipCode) {
        this.ebcuZipCode = ebcuZipCode;
    }

    public String getEbcuAddress() {
        return ebcuAddress;
    }

    public void setEbcuAddress(String ebcuAddress) {
        this.ebcuAddress = ebcuAddress;
    }

    public String getEbcuFinanceCode() {
        return ebcuFinanceCode;
    }

    public void setEbcuFinanceCode(String ebcuFinanceCode) {
        this.ebcuFinanceCode = ebcuFinanceCode;
    }

    public String getEbcuTaxRegistNo() {
        return ebcuTaxRegistNo;
    }

    public void setEbcuTaxRegistNo(String ebcuTaxRegistNo) {
        this.ebcuTaxRegistNo = ebcuTaxRegistNo;
    }

    public String getEbcuBusinessNo() {
        return ebcuBusinessNo;
    }

    public void setEbcuBusinessNo(String ebcuBusinessNo) {
        this.ebcuBusinessNo = ebcuBusinessNo;
    }

    public Double getEbcuTaxRate() {
        return ebcuTaxRate;
    }

    public void setEbcuTaxRate(Double ebcuTaxRate) {
        this.ebcuTaxRate = ebcuTaxRate;
    }

    public Double getEbcuTaxRateValue() {
        return ebcuTaxRateValue;
    }

    public void setEbcuTaxRateValue(Double ebcuTaxRateValue) {
        this.ebcuTaxRateValue = ebcuTaxRateValue;
    }

    public String getEbcuIsGeneralTaxpayer() {
        return ebcuIsGeneralTaxpayer;
    }

    public void setEbcuIsGeneralTaxpayer(String ebcuIsGeneralTaxpayer) {
        this.ebcuIsGeneralTaxpayer = ebcuIsGeneralTaxpayer;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}