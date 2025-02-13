package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 计费条款参数Entity
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
public class BmsBillTermsParameter extends DataEntity<BmsBillTermsParameter> {

    private static final long serialVersionUID = 1L;
    // 计费条款代码
    private String billTermsCode;
    // 序号
    private Integer seqNo;
    // 字段
    private String field;
    // 描述
    private String title;
    // 格式(日期、下拉框、文本)
    private String type;
    // 字段选项(枚举字典)
    private String fieldOption;
    // 默认值
    private String defaultValue;
    // 是否启用
    private String isEnable;
    // 是否显示
    private String isShow;
    // 是否作为结算日期
    private String isSettleDate;
    // 机构
    private String orgId;

    public BmsBillTermsParameter() {
        super();
    }

    public BmsBillTermsParameter(String id) {
        super(id);
    }

    public String getBillTermsCode() {
        return billTermsCode;
    }

    public void setBillTermsCode(String billTermsCode) {
        this.billTermsCode = billTermsCode;
    }

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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}