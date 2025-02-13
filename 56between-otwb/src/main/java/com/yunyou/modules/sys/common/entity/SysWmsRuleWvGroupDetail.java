package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 波次规则组明细Entity
 */
public class SysWmsRuleWvGroupDetail extends DataEntity<SysWmsRuleWvGroupDetail> {

    private static final long serialVersionUID = 1L;
    private String groupCode;// 规则组编码
    private String lineNo;// 行号
    private String ruleCode;// 波次规则编码
    private String headerId;// 表头Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private String ruleName;// 波次规则名称
    private String dataSetName;// 数据套名称

    public SysWmsRuleWvGroupDetail() {
        super();
    }

    public SysWmsRuleWvGroupDetail(String id) {
        super(id);
    }

    public SysWmsRuleWvGroupDetail(String headerId, String dataSet) {
        this.headerId = headerId;
        this.dataSet = dataSet;
    }

    public SysWmsRuleWvGroupDetail(String id, String headerId, String dataSet) {
        super(id);
        this.headerId = headerId;
        this.dataSet = dataSet;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
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

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}