package com.yunyou.modules.oms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 业务类型-订单类型关联关系Entity
 *
 * @author zyf
 * @version 2019-07-03
 */
public class OmBusinessOrderTypeRelation extends DataEntity<OmBusinessOrderTypeRelation> {
    private static final long serialVersionUID = 1L;

    private String businessOrderType;// 业务订单类型
    private String orderType;// 作业任务类型
    private String pushSystem;// 下发系统
    private String pushOrderType;// 下发订单类型
    private String orgId;// 机构

    public OmBusinessOrderTypeRelation() {
        super();
    }

    public OmBusinessOrderTypeRelation(String id) {
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

    public String getPushOrderType() {
        return pushOrderType;
    }

    public void setPushOrderType(String pushOrderType) {
        this.pushOrderType = pushOrderType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}