package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 质检规则Entity
 */
public class SysWmsRuleQcClass extends DataEntity<SysWmsRuleQcClass> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "质检规则编码不能为空")
    private String ruleCode;// 质检规则编码
    private String lineNo;// 行号
    @NotNull(message = "级差区间从（大于）不能为空")
    private Double fmClass;// 级差区间从（大于）
    @NotNull(message = "级差区间到（小于）不能为空")
    private Double toClass;// 级差区间到（小于等于）
    @NotNull(message = "数量类型不能为空")
    private String qtyType;// 数量类型（按数量/按比例）
    @NotNull(message = "数量/比例不能为空")
    private Double qty;// 数量/比例
    private String headerId;// 头表Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    public SysWmsRuleQcClass() {
        super();
    }

    public SysWmsRuleQcClass(String id) {
        super(id);
    }

    public SysWmsRuleQcClass(String headerId, String dataSet) {
        this.headerId = headerId;
        this.dataSet = dataSet;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public Double getFmClass() {
        return fmClass;
    }

    public void setFmClass(Double fmClass) {
        this.fmClass = fmClass;
    }

    public Double getToClass() {
        return toClass;
    }

    public void setToClass(Double toClass) {
        this.toClass = toClass;
    }

    public String getQtyType() {
        return qtyType;
    }

    public void setQtyType(String qtyType) {
        this.qtyType = qtyType;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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