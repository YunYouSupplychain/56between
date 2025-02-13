package com.yunyou.modules.bms.finance.entity.extend;

/**
 * 条款条件参数
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsCalcTermsParams {
    /**
     * 序号
     */
    private Integer seqNo;
    /**
     * 字段
     */
    private String field;
    /**
     * 描述
     */
    private String title;
    /**
     * 格式(日期、下拉框、文本)
     */
    private String type;
    /**
     * 字段选项(枚举字典)
     */
    private String fieldOption;
    /**
     * 字段值
     */
    private String fieldValue;
    /**
     * 是否启用
     */
    private String isEnable;
    /**
     * 是否显示
     */
    private String isShow;
    /**
     * 是否作为结算日期
     */
    private String isSettleDate;

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldOption() {
        return fieldOption;
    }

    public void setFieldOption(String fieldOption) {
        this.fieldOption = fieldOption;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsSettleDate() {
        return isSettleDate;
    }

    public void setIsSettleDate(String isSettleDate) {
        this.isSettleDate = isSettleDate;
    }
}
