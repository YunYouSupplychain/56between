package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 批次属性Entity
 */
public class SysWmsLotDetail extends DataEntity<SysWmsLotDetail> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "批次属性编码不能为空")
    private String lotCode;// 批次属性编码
    @NotNull(message = "批次属性不能为空")
    private String lotAtt;// 批次属性
    @NotNull(message = "批次标签不能为空")
    private String title;// 批次标签
    private String foreignTitle;// 批次标签（外语）
    @NotNull(message = "输入控制不能为空")
    private String inputControl;// 输入控制（R必输、O可选、F禁用）
    private String fieldType;// 字段显示类型（字符串、数字、日期、日期时间、弹出框、下拉框）
    private String key;// 属性选项（Key值，适用于弹出框和下拉框）
    private String headerId;// 头表Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    public SysWmsLotDetail() {
        super();
    }

    public SysWmsLotDetail(String id) {
        super(id);
    }

    public SysWmsLotDetail(String headerId, String dataSet) {
        this.headerId = headerId;
        this.dataSet = dataSet;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getLotAtt() {
        return lotAtt;
    }

    public void setLotAtt(String lotAtt) {
        this.lotAtt = lotAtt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getForeignTitle() {
        return foreignTitle;
    }

    public void setForeignTitle(String foreignTitle) {
        this.foreignTitle = foreignTitle;
    }

    public String getInputControl() {
        return inputControl;
    }

    public void setInputControl(String inputControl) {
        this.inputControl = inputControl;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}