package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 循环级别Entity
 */
public class SysWmsCycle extends DataEntity<SysWmsCycle> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "循环级别编码不能为空")
    private String cycleCode;// 循环级别编码
    @NotNull(message = "循环级别名称不能为空")
    private String cycleName;// 循环级别名称
    private Double cycleLife;// 循环周期
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private String dataSetName;// 数据套名称

    public SysWmsCycle() {
        super();
    }

    public SysWmsCycle(String id) {
        super(id);
    }

    public SysWmsCycle(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getCycleCode() {
        return cycleCode;
    }

    public void setCycleCode(String cycleCode) {
        this.cycleCode = cycleCode;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public Double getCycleLife() {
        return cycleLife;
    }

    public void setCycleLife(Double cycleLife) {
        this.cycleLife = cycleLife;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}