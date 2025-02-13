package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 波次规则Entity
 */
public class SysWmsRuleWvDetail extends DataEntity<SysWmsRuleWvDetail> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "规则编码不能为空")
    private String ruleCode;// 规则编码
    @NotNull(message = "行号不能为空")
    private String lineNo;// 行号
    @NotNull(message = "主规则不能为空")
    private String mainCode;// 主规则编码
    private String desc;// 描述
    private String isEnable;// 是否启用
    private String sql;// 订单限制SQL
    private String headerId;// 头表Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套
    private List<SysWmsRuleWvDetailWv> ruleWvDetailWvList = Lists.newArrayList();// 波次限制明细
    private List<SysWmsRuleWvDetailOrder> ruleWvDetailOrderList = Lists.newArrayList();//订单限制明细

    public SysWmsRuleWvDetail() {
        super();
    }

    public SysWmsRuleWvDetail(String id) {
        super(id);
    }

    public SysWmsRuleWvDetail(String id, String headerId, String dataSet) {
        super(id);
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

    public String getMainCode() {
        return mainCode;
    }

    public void setMainCode(String mainCode) {
        this.mainCode = mainCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
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

    public List<SysWmsRuleWvDetailWv> getRuleWvDetailWvList() {
        return ruleWvDetailWvList;
    }

    public void setRuleWvDetailWvList(List<SysWmsRuleWvDetailWv> ruleWvDetailWvList) {
        this.ruleWvDetailWvList = ruleWvDetailWvList;
    }

    public List<SysWmsRuleWvDetailOrder> getRuleWvDetailOrderList() {
        return ruleWvDetailOrderList;
    }

    public void setRuleWvDetailOrderList(List<SysWmsRuleWvDetailOrder> ruleWvDetailOrderList) {
        this.ruleWvDetailOrderList = ruleWvDetailOrderList;
    }
}