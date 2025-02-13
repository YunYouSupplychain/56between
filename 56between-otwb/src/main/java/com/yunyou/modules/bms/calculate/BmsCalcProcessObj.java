package com.yunyou.modules.bms.calculate;

import java.math.BigDecimal;
import java.util.List;

/**
 * 计算过程对象
 *
 * @author liujinahua
 * @version 2022.8.11
 */
public class BmsCalcProcessObj {

    /**
     * 计算过程类型
     */
    private String processType;
    /**
     * 计算使用值
     */
    private BigDecimal calcValue;
    /**
     * 逻辑使用值
     */
    private BigDecimal logicalValue;
    /**
     * 计算关联数据ID
     */
    private List<String> calcRelationIds;

    public BmsCalcProcessObj() {
    }

    public BmsCalcProcessObj(String processType, BigDecimal calcValue, BigDecimal logicalValue, List<String> calcRelationIds) {
        this.processType = processType;
        this.calcValue = calcValue;
        this.logicalValue = logicalValue;
        this.calcRelationIds = calcRelationIds;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public BigDecimal getCalcValue() {
        return calcValue;
    }

    public void setCalcValue(BigDecimal calcValue) {
        this.calcValue = calcValue;
    }

    public BigDecimal getLogicalValue() {
        return logicalValue;
    }

    public void setLogicalValue(BigDecimal logicalValue) {
        this.logicalValue = logicalValue;
    }

    public List<String> getCalcRelationIds() {
        return calcRelationIds;
    }

    public void setCalcRelationIds(List<String> calcRelationIds) {
        this.calcRelationIds = calcRelationIds;
    }
}
