package com.yunyou.modules.tms.spare.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmSparePoHeader extends DataEntity<TmSparePoHeader> {
    private static final long serialVersionUID = -7522436221972134089L;

    private String sparePoNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date orderTime;
    private String orderStatus;
    private String orderType;
    private String customerNo;
    private String operator;
    private String orgId;
    private String baseOrgId;

    public TmSparePoHeader() {
    }

    public TmSparePoHeader(String id) {
        super(id);
    }

    public TmSparePoHeader(String sparePoNo, String orgId) {
        this.sparePoNo = sparePoNo;
        this.orgId = orgId;
    }

    public String getSparePoNo() {
        return sparePoNo;
    }

    public void setSparePoNo(String sparePoNo) {
        this.sparePoNo = sparePoNo;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }
}
