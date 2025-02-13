package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysWmsCarrierTypeRelation;

/**
 * 承运商类型关系Entity
 */
public class SysWmsCarrierTypeRelationEntity extends SysWmsCarrierTypeRelation {

    private String dataSetName;// 数据套名称
    private String carrierName;// 承运商编码

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }
}