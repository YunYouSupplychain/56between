package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.List;

/**
 * 开票对象Entity
 */
public class SysBmsInvoiceObject extends DataEntity<SysBmsInvoiceObject> {

    private static final long serialVersionUID = 1L;
    private String code;// 编码
    private String name;// 名称
    private String type;// 类型
    private String principal;// 开票对象负责人
    private String phone;// 电话
    private String address;// 地址
    private String bank;// 开户行
    private String bankAccount;// 银行账户
    private String dataSet;// 数据套
    private List<SysBmsInvoiceObjectDetail> sysBmsInvoiceObjectDetailList = Lists.newArrayList();// 子表列表

    // 扩展字段
    private String dataSetName;// 数据套名称

    public SysBmsInvoiceObject() {
        super();
        this.setIdType(IDTYPE_AUTO);
    }

    public SysBmsInvoiceObject(String id) {
        super(id);
    }

    public SysBmsInvoiceObject(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    @ExcelField(title = "编码", align = 2, sort = 7)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ExcelField(title = "名称", align = 2, sort = 8)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title = "类型", align = 2, sort = 9)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<SysBmsInvoiceObjectDetail> getSysBmsInvoiceObjectDetailList() {
        return sysBmsInvoiceObjectDetailList;
    }

    public void setSysBmsInvoiceObjectDetailList(List<SysBmsInvoiceObjectDetail> sysBmsInvoiceObjectDetailList) {
        this.sysBmsInvoiceObjectDetailList = sysBmsInvoiceObjectDetailList;
    }
}