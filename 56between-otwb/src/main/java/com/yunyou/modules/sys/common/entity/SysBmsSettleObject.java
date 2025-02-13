package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 结算对象Entity
 */
public class SysBmsSettleObject extends DataEntity<SysBmsSettleObject> {

    private static final long serialVersionUID = 1L;
    private String settleObjectCode;// 结算对象代码
    private String settleObjectName;// 结算对象名称
    private String settleCategory;// 结算类别
    private String settleType;// 结算方式
    private String projectCode;// 项目代码
    private String projectName;// 项目名称
    private String telephone;// 电话
    private String contacts;// 联系人
    private String address;// 地址
    private String bank;// 开户银行
    private String bankAccount;// 银行账户
    private String bankAccountName;// 银行账户名
    private String dataSet;// 数据套
    private String dataSetName;// 数据套名称

    public SysBmsSettleObject() {
        super();
    }

    public SysBmsSettleObject(String id) {
        super(id);
    }

    public SysBmsSettleObject(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    @ExcelField(title = "结算对象代码", align = 2, sort = 7)
    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    @ExcelField(title = "结算对象名称", align = 2, sort = 8)
    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    @ExcelField(title = "结算方式", dictType = "settle_type", align = 2, sort = 9)
    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    @ExcelField(title = "结算类别", dictType = "settle_category", align = 2, sort = 10)
    public String getSettleCategory() {
        return settleCategory;
    }

    public void setSettleCategory(String settleCategory) {
        this.settleCategory = settleCategory;
    }

    @ExcelField(title = "项目代码", align = 2, sort = 11)
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @ExcelField(title = "项目名称", align = 2, sort = 12)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}