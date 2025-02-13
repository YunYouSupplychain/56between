package com.yunyou.modules.tms.order.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.tms.order.entity.TmDemandPlanHeader;

import java.util.Date;
import java.util.List;

public class TmDemandPlanEntity extends TmDemandPlanHeader {
    private static final long serialVersionUID = 1L;
    // 客户名称
    private String ownerName;
    // 订单时间从
    private Date orderTimeFm;
    // 订单时间到
    private Date orderTimeTo;
    // 到达时间从
    private Date arrivalTimeFm;
    // 到达时间到
    private Date arrivalTimeTo;

    private List<TmDemandPlanDetailEntity> tmDemandPlanDetailList = Lists.newArrayList();

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getOrderTimeFm() {
        return orderTimeFm;
    }

    public void setOrderTimeFm(Date orderTimeFm) {
        this.orderTimeFm = orderTimeFm;
    }

    public Date getOrderTimeTo() {
        return orderTimeTo;
    }

    public void setOrderTimeTo(Date orderTimeTo) {
        this.orderTimeTo = orderTimeTo;
    }

    public Date getArrivalTimeFm() {
        return arrivalTimeFm;
    }

    public void setArrivalTimeFm(Date arrivalTimeFm) {
        this.arrivalTimeFm = arrivalTimeFm;
    }

    public Date getArrivalTimeTo() {
        return arrivalTimeTo;
    }

    public void setArrivalTimeTo(Date arrivalTimeTo) {
        this.arrivalTimeTo = arrivalTimeTo;
    }

    public List<TmDemandPlanDetailEntity> getTmDemandPlanDetailList() {
        return tmDemandPlanDetailList;
    }

    public void setTmDemandPlanDetailList(List<TmDemandPlanDetailEntity> tmDemandPlanDetailList) {
        this.tmDemandPlanDetailList = tmDemandPlanDetailList;
    }
}
