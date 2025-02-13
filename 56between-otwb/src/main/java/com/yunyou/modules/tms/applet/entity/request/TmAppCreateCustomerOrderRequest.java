package com.yunyou.modules.tms.applet.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TmAppCreateCustomerOrderRequest implements Serializable {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date arriveTime;
    private String userId;

    private String orgId;
    private String baseOrgId;
    private String ownerCode;
    private String operator;
    private List<TmAppCreateCustomerOrderSkuListRequest> skuList = Lists.newArrayList();

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<TmAppCreateCustomerOrderSkuListRequest> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<TmAppCreateCustomerOrderSkuListRequest> skuList) {
        this.skuList = skuList;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }
}