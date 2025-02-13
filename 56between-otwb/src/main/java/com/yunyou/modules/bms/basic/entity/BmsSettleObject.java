package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 结算对象Entity
 *
 * @author zqs
 * @version 2018-06-15
 */
public class BmsSettleObject extends DataEntity<BmsSettleObject> {

    private static final long serialVersionUID = 1L;
    // 结算对象代码
    private String settleObjectCode;
    // 结算对象名称
    private String settleObjectName;
    // 结算类别
    private String settleCategory;
    // 结算方式
    private String settleType;
    // 项目代码
    private String projectCode;
    // 项目名称
    private String projectName;
    // 电话
    private String telephone;
    // 联系人
    private String contacts;
    // 地址
    private String address;
    // 开户银行
    private String bank;
    // 银行账户
    private String bankAccount;
    // 银行账户名
    private String bankAccountName;
    // 机构ID
    private String orgId;

    public BmsSettleObject() {
        super();
    }

    public BmsSettleObject(String id) {
        super(id);
    }

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    public String getSettleCategory() {
        return settleCategory;
    }

    public void setSettleCategory(String settleCategory) {
        this.settleCategory = settleCategory;
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}