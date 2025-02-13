package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 业务类型-订单类型关联关系Entity
 */
public class SysOmsBusinessOrderTypeRelation extends DataEntity<SysOmsBusinessOrderTypeRelation> {
    private static final long serialVersionUID = 1L;

    private String businessOrderType;// 业务订单类型
    private String orderType;// 作业任务类型
    private String pushSystem;// 下发系统
    private String pushOrderType;// 下发订单类型
    private String dataSet;// 数据套

    // 扩展字段
    private String dataSetName;// 数据套名称
    private String oldBusinessOrderType;
    private String oldOrderType;
    private String oldPushSystem;

    public SysOmsBusinessOrderTypeRelation() {
        super();
    }

    public SysOmsBusinessOrderTypeRelation(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public SysOmsBusinessOrderTypeRelation(String id) {
        super(id);
    }

    public String getBusinessOrderType() {
        return businessOrderType;
    }

    public void setBusinessOrderType(String businessOrderType) {
        this.businessOrderType = businessOrderType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPushSystem() {
        return pushSystem;
    }

    public void setPushSystem(String pushSystem) {
        this.pushSystem = pushSystem;
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

    public String getOldBusinessOrderType() {
        return oldBusinessOrderType;
    }

    public void setOldBusinessOrderType(String oldBusinessOrderType) {
        this.oldBusinessOrderType = oldBusinessOrderType;
    }

    public String getOldOrderType() {
        return oldOrderType;
    }

    public void setOldOrderType(String oldOrderType) {
        this.oldOrderType = oldOrderType;
    }

    public String getOldPushSystem() {
        return oldPushSystem;
    }

    public void setOldPushSystem(String oldPushSystem) {
        this.oldPushSystem = oldPushSystem;
    }

    public String getPushOrderType() {
        return pushOrderType;
    }

    public void setPushOrderType(String pushOrderType) {
        this.pushOrderType = pushOrderType;
    }
}