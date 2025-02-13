package com.yunyou.modules.tms.order.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.tms.order.entity.TmExceptionHandleBill;

import java.util.Date;
import java.util.List;

public class TmExceptionHandleBillEntity extends TmExceptionHandleBill {
    private static final long serialVersionUID = 1L;

    private String outletName;      // 网点名称
    private String customerName;    // 客户名称
    private Date registerTimeFrom;
    private Date registerTimeTo;
    private Date happenTimeFrom;
    private Date happenTimeTo;

    private List<TmExceptionHandleBillDetailEntity> tmExceptionHandleBillDetailList = Lists.newArrayList();

    private List<TmExceptionHandleBillFeeEntity> tmExceptionHandleBillFeeList = Lists.newArrayList();

    public List<TmExceptionHandleBillDetailEntity> getTmExceptionHandleBillDetailList() {
        return tmExceptionHandleBillDetailList;
    }

    public void setTmExceptionHandleBillDetailList(List<TmExceptionHandleBillDetailEntity> tmExceptionHandleBillDetailList) {
        this.tmExceptionHandleBillDetailList = tmExceptionHandleBillDetailList;
    }

    public List<TmExceptionHandleBillFeeEntity> getTmExceptionHandleBillFeeList() {
        return tmExceptionHandleBillFeeList;
    }

    public void setTmExceptionHandleBillFeeList(List<TmExceptionHandleBillFeeEntity> tmExceptionHandleBillFeeList) {
        this.tmExceptionHandleBillFeeList = tmExceptionHandleBillFeeList;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getRegisterTimeFrom() {
        return registerTimeFrom;
    }

    public void setRegisterTimeFrom(Date registerTimeFrom) {
        this.registerTimeFrom = registerTimeFrom;
    }

    public Date getRegisterTimeTo() {
        return registerTimeTo;
    }

    public void setRegisterTimeTo(Date registerTimeTo) {
        this.registerTimeTo = registerTimeTo;
    }

    public Date getHappenTimeFrom() {
        return happenTimeFrom;
    }

    public void setHappenTimeFrom(Date happenTimeFrom) {
        this.happenTimeFrom = happenTimeFrom;
    }

    public Date getHappenTimeTo() {
        return happenTimeTo;
    }

    public void setHappenTimeTo(Date happenTimeTo) {
        this.happenTimeTo = happenTimeTo;
    }
}
