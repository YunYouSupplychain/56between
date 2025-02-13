package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 库存周转规则Entity
 */
public class SysWmsRuleRotationHeader extends DataEntity<SysWmsRuleRotationHeader> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "规则编码不能为空")
    private String ruleCode;// 规则编码
    @NotNull(message = "规则名称不能为空")
    private String ruleName;// 规则名称
    private String rotationType;// 周转类型（库存周转优先、包装优先）
    private String lotCode;// 批次属性编码
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private List<SysWmsRuleRotationDetail> ruleRotationDetailList = Lists.newArrayList();
    private String lotName;// 批次属性名称
    private String dataSetName;// 数据套名称

    public SysWmsRuleRotationHeader() {
        super();
    }

    public SysWmsRuleRotationHeader(String id) {
        super(id);
    }

    public SysWmsRuleRotationHeader(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRotationType() {
        return rotationType;
    }

    public void setRotationType(String rotationType) {
        this.rotationType = rotationType;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public List<SysWmsRuleRotationDetail> getRuleRotationDetailList() {
        return ruleRotationDetailList;
    }

    public void setRuleRotationDetailList(List<SysWmsRuleRotationDetail> ruleRotationDetailList) {
        this.ruleRotationDetailList = ruleRotationDetailList;
    }
}