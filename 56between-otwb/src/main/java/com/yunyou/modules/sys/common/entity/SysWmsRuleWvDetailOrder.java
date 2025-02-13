package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 波次规则Entity
 */
public class SysWmsRuleWvDetailOrder extends DataEntity<SysWmsRuleWvDetailOrder> {

    private static final long serialVersionUID = 1L;
    private String ruleCode;// 规则编码
    private String lineNo;// 行号
    private String andOr;// AND或OR运算符
    private String leftBracket;// 左括号
    private String orderAttCode;// 出库单属性编码
    private String orderAttName;// 出库单属性名称
    private String operator;// 运算符
    private String value;// 值
    private String rightBracket;// 右括号
    private String headerId;// 头表Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    public SysWmsRuleWvDetailOrder() {
        super();
    }

    public SysWmsRuleWvDetailOrder(String id) {
        super(id);
    }

    public SysWmsRuleWvDetailOrder(String id, String headerId, String dataSet) {
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

    public String getAndOr() {
        return andOr;
    }

    public void setAndOr(String andOr) {
        this.andOr = andOr;
    }

    public String getLeftBracket() {
        return leftBracket;
    }

    public void setLeftBracket(String leftBracket) {
        this.leftBracket = leftBracket;
    }

    public String getOrderAttCode() {
        return orderAttCode;
    }

    public void setOrderAttCode(String orderAttCode) {
        this.orderAttCode = orderAttCode;
    }

    public String getOrderAttName() {
        return orderAttName;
    }

    public void setOrderAttName(String orderAttName) {
        this.orderAttName = orderAttName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRightBracket() {
        return rightBracket;
    }

    public void setRightBracket(String rightBracket) {
        this.rightBracket = rightBracket;
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