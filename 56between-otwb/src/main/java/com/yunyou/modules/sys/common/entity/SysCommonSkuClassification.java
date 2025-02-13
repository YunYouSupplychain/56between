package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 商品分类
 */
public class SysCommonSkuClassification extends DataEntity<SysCommonSkuClassification> {

    private String code;
    private String name;
    private String dataSet;

    public SysCommonSkuClassification() {
    }

    public SysCommonSkuClassification(String id) {
        super(id);
    }

    public SysCommonSkuClassification(String code, String dataSet) {
        this.code = code;
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
}
