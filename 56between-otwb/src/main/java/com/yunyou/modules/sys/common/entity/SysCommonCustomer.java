package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 客户信息Entity
 */
public class SysCommonCustomer extends DataEntity<SysCommonCustomer> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "编码不能为空")
    private String code;// 编码
    @NotNull(message = "名称不能为空")
    private String name;// 名称
    private String shortName;// 简称
    private String foreignName;// 外语名称
    @NotNull(message = "类型不能为空")
    private String type;// 类型
    private String areaId;// 所属城市ID
    private String province;// 省
    private String city;// 市
    private String area;// 区
    private String address;// 详细地址
    private String contacts;// 联系人
    private String tel;// 电话
    private String fax;// 传真
    private String mail;// 邮箱
    private String zipCode;// 邮编
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套
    private String categories;// 大类
    private String industryType;// 行业类型
    private String scope;// 范围
    private String checkPerson;// 对账人
    private String poChannel;// 采购渠道
    private String warehouse;// 仓别
    private String clerkCode;// 业务员
    private String financeCode;// 财务代码
    private String brand;// 品牌
    private String mainBusiness;// 主营业务
    private String taxRegisterNo;// 平台税务登记号
    private String businessNo;// 工商登记号
    private String isGeneralTaxpayer;// 是否一般纳税人
    private Double taxRate;// 税率
    private Double taxRateValue;// 税率值
    private String url;// 网址
    private String unCode;// 统一码
    private String classification;// 分类
    private String routeCode;// 业务路线(业务路线code)
    private String carrierMatchedOrgId;// 承运商对应机构ID
    private String outletMatchedOrgId;// 网点对应机构ID
    private Double repairPrice;// 维修工时单价
    private String settleCode;// 结算对象编码
    private String project;// 项目
    private String def1;// 门店配送周期（冷藏）
    private String def2;// 门店配送周期（冷冻）
    private String def3;
    private String def4;
    private String def5;
    // 客户联系人列表
    private List<SysCommonCustomerContacts> contactsList = Lists.newArrayList();

    public SysCommonCustomer() {
        super();
    }

    public SysCommonCustomer(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public SysCommonCustomer(String id, String code, String dataSet) {
        super(id);
        this.code = code;
        this.dataSet = dataSet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getForeignName() {
        return foreignName;
    }

    public void setForeignName(String foreignName) {
        this.foreignName = foreignName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getPoChannel() {
        return poChannel;
    }

    public void setPoChannel(String poChannel) {
        this.poChannel = poChannel;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getClerkCode() {
        return clerkCode;
    }

    public void setClerkCode(String clerkCode) {
        this.clerkCode = clerkCode;
    }

    public String getFinanceCode() {
        return financeCode;
    }

    public void setFinanceCode(String financeCode) {
        this.financeCode = financeCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMainBusiness() {
        return mainBusiness;
    }

    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    public String getTaxRegisterNo() {
        return taxRegisterNo;
    }

    public void setTaxRegisterNo(String taxRegisterNo) {
        this.taxRegisterNo = taxRegisterNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getIsGeneralTaxpayer() {
        return isGeneralTaxpayer;
    }

    public void setIsGeneralTaxpayer(String isGeneralTaxpayer) {
        this.isGeneralTaxpayer = isGeneralTaxpayer;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getTaxRateValue() {
        return taxRateValue;
    }

    public void setTaxRateValue(Double taxRateValue) {
        this.taxRateValue = taxRateValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUnCode() {
        return unCode;
    }

    public void setUnCode(String unCode) {
        this.unCode = unCode;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getCarrierMatchedOrgId() {
        return carrierMatchedOrgId;
    }

    public void setCarrierMatchedOrgId(String carrierMatchedOrgId) {
        this.carrierMatchedOrgId = carrierMatchedOrgId;
    }

    public String getOutletMatchedOrgId() {
        return outletMatchedOrgId;
    }

    public void setOutletMatchedOrgId(String outletMatchedOrgId) {
        this.outletMatchedOrgId = outletMatchedOrgId;
    }

    public Double getRepairPrice() {
        return repairPrice;
    }

    public void setRepairPrice(Double repairPrice) {
        this.repairPrice = repairPrice;
    }

    public String getSettleCode() {
        return settleCode;
    }

    public void setSettleCode(String settleCode) {
        this.settleCode = settleCode;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    public List<SysCommonCustomerContacts> getContactsList() {
        return contactsList;
    }

    public void setContactsList(List<SysCommonCustomerContacts> contactsList) {
        this.contactsList = contactsList;
    }
}