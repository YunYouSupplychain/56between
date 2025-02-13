package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

public class SysTmsBusinessRoute extends DataEntity<SysTmsBusinessRoute> {

    private static final long serialVersionUID = -9168407398587249646L;
    private String code;
    private String name;
    private String dataSet;

    private String dataSetName;

    public SysTmsBusinessRoute() {
    }

    public SysTmsBusinessRoute(String id) {
        super(id);
    }

    public SysTmsBusinessRoute(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
