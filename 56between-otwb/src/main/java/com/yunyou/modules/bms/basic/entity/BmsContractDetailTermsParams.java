package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 合同明细条款参数
 */
public class BmsContractDetailTermsParams extends DataEntity<BmsContractDetailTermsParams> {

    private static final long serialVersionUID = 1L;
    /**
     * 合同明细ID
     */
    private String fkId;
    /**
     * 包含or排除
     */
    private String includeOrExclude;
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
    /**
     * 机构ID
     */
    private String orgId;

    public BmsContractDetailTermsParams() {
        super();
    }

    public BmsContractDetailTermsParams(String id) {
        super(id);
    }

    public BmsContractDetailTermsParams(String fkId, String orgId) {
        this.fkId = fkId;
        this.orgId = orgId;
    }

    public String getFkId() {
        return fkId;
    }

    public void setFkId(String fkId) {
        this.fkId = fkId;
    }

    public String getIncludeOrExclude() {
        return includeOrExclude;
    }

    public void setIncludeOrExclude(String includeOrExclude) {
        this.includeOrExclude = includeOrExclude;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
