package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 客户联系人Entity
 */
public class SysCommonCustomerContacts extends DataEntity<SysCommonCustomerContacts> {

    private static final long serialVersionUID = 1L;
    private SysCommonCustomer customer;// 外键 父类
    private String code;// 联系人编码
    private String name;// 联系人名称
    private String title;// 头衔
    private String tel;// 电话
    private String mobilePhone;// 手机
    private String fax;// 传真
    private String email;// 邮箱
    private String addressName;// 地址名称
    private String address;// 地址
    private String isDefault;// 是否默认
    private String dataSet;// 数据套
    private String projectId;// 项目
    private String category;//课别

    public SysCommonCustomerContacts() {
        super();
        this.setIdType(IDTYPE_UUID);
    }

    public SysCommonCustomerContacts(String id) {
        super(id);
    }

    public SysCommonCustomerContacts(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public SysCommonCustomerContacts(SysCommonCustomer customer) {
        this.customer = customer;
        this.dataSet = customer.getDataSet();
    }

    @NotNull(message = "外键不能为空")
    public SysCommonCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SysCommonCustomer customer) {
        this.customer = customer;
    }

    @ExcelField(title = "联系人编码", align = 2, sort = 7)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ExcelField(title = "联系人名称", align = 2, sort = 8)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title = "头衔", align = 2, sort = 9)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ExcelField(title = "电话", align = 2, sort = 10)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @ExcelField(title = "手机", align = 2, sort = 12)
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @ExcelField(title = "传真", align = 2, sort = 13)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @ExcelField(title = "邮箱", align = 2, sort = 14)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ExcelField(title = "地址名称", align = 2, sort = 15)
    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    @ExcelField(title = "地址", align = 2, sort = 16)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ExcelField(title = "是否默认", dictType = "", align = 2, sort = 17)
    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    @ExcelField(title = "项目", align = 2, sort = 19)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCustomerId() {
        return this.getCustomer().getId();
    }

}