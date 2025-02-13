package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 路由Entity
 *
 * @author zqs
 * @version 2018-06-15
 */
public class BmsCarrierRoute extends DataEntity<BmsCarrierRoute> {

    private static final long serialVersionUID = 1L;
    // 承运商编码
    private String carrierCode;
    // 路由代码
    private String routeCode;
    // 路由名称
    private String routeName;
    // 起始地ID
    private String startAreaId;
    // 目的地ID
    private String endAreaId;
    // 标准里程
    private BigDecimal mileage;
    // 标准时效
    private BigDecimal timeliness;
    // 机构ID
    private String orgId;

    public BmsCarrierRoute() {
        super();
    }

    public BmsCarrierRoute(String id) {
        super(id);
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStartAreaId() {
        return startAreaId;
    }

    public void setStartAreaId(String startAreaId) {
        this.startAreaId = startAreaId;
    }

    public String getEndAreaId() {
        return endAreaId;
    }

    public void setEndAreaId(String endAreaId) {
        this.endAreaId = endAreaId;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public BigDecimal getTimeliness() {
        return timeliness;
    }

    public void setTimeliness(BigDecimal timeliness) {
        this.timeliness = timeliness;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}