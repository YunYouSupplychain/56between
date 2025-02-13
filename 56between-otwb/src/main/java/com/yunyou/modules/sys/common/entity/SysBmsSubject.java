package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 费用科目Entity
 */
public class SysBmsSubject extends DataEntity<SysBmsSubject> {

    private static final long serialVersionUID = 1L;
    // 费用科目代码
    private String billSubjectCode;
    // 费用科目名称
    private String billSubjectName;
    // 费用模块
    private String billModule;
    // 费用类别
    private String billCategory;
    // 数据套
    private String dataSet;

    /**
     * 扩展字段
     */
    // 数据套名称
    private String dataSetName;

    public SysBmsSubject() {
        super();
    }

    public SysBmsSubject(String id) {
        super(id);
    }

    public SysBmsSubject(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
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