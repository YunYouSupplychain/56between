package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 质检规则Entity
 */
public class SysWmsRuleQcDetail extends DataEntity<SysWmsRuleQcDetail> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "质检规则编码不能为空")
    private String ruleCode;// 质检规则编码
    private String lineNo;// 行号
    @NotNull(message = "合格率区间从不能为空")
    private Double fmRate;// 合格率区间从
    @NotNull(message = "合格率区间到不能为空")
    private Double toRate;// 合格率区间到
    @NotNull(message = "质检处理建议不能为空")
    private String qcSuggest;// 质检处理建议
    private String headerId;// 头表Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    public SysWmsRuleQcDetail() {
        super();
    }

    public SysWmsRuleQcDetail(String id) {
        super(id);
    }

    public SysWmsRuleQcDetail(String headerId, String dataSet) {
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

    public Double getFmRate() {
        return fmRate;
    }

    public void setFmRate(Double fmRate) {
        this.fmRate = fmRate;
    }

    public Double getToRate() {
        return toRate;
    }

    public void setToRate(Double toRate) {
        this.toRate = toRate;
    }

    public String getQcSuggest() {
        return qcSuggest;
    }

    public void setQcSuggest(String qcSuggest) {
        this.qcSuggest = qcSuggest;
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