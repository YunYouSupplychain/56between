package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 承运商类型关系Entity
 */
public class SysWmsCarrierTypeRelation extends DataEntity<SysWmsCarrierTypeRelation> {

    private static final long serialVersionUID = 1L;
    private String carrierCode;// 承运商编码
    private String type;// 类型
    private String dataSet;// 数据套

    public SysWmsCarrierTypeRelation() {
        super();
    }

    public SysWmsCarrierTypeRelation(String id) {
        super(id);
    }

    public SysWmsCarrierTypeRelation(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}