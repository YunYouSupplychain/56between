package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 费用科目Entity
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
public class BmsBillSubject extends DataEntity<BmsBillSubject> {

    private static final long serialVersionUID = 1L;
    // 费用科目代码
    private String billSubjectCode;
    // 费用科目名称
    private String billSubjectName;
    // 费用模块
    private String billModule;
    // 费用类别
    private String billCategory;
    // 机构
    private String orgId;

    public BmsBillSubject() {
        super();
    }

    public BmsBillSubject(String id) {
        super(id);
    }

    public String getBillSubjectCode() {
        return billSubjectCode;
    }

    public void setBillSubjectCode(String billSubjectCode) {
        this.billSubjectCode = billSubjectCode;
    }

    public String getBillSubjectName() {
        return billSubjectName;
    }

    public void setBillSubjectName(String billSubjectName) {
        this.billSubjectName = billSubjectName;
    }

    public String getBillModule() {
        return billModule;
    }

    public void setBillModule(String billModule) {
        this.billModule = billModule;
    }

    public String getBillCategory() {
        return billCategory;
    }

    public void setBillCategory(String billCategory) {
        this.billCategory = billCategory;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}