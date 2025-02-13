package com.yunyou.modules.tms.spare.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmSpareSoHeader extends DataEntity<TmSpareSoHeader> {
    private static final long serialVersionUID = -1553388999620075292L;

    private String spareSoNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date orderTime;
    private String orderStatus;
    private String orderType;
    private String customerNo;
    private String operator;
    private String orgId;
    private String baseOrgId;

    public TmSpareSoHeader() {
    }

    public TmSpareSoHeader(String id) {
        super(id);
    }

    public TmSpareSoHeader(String spareSoNo, String orgId) {
        this.spareSoNo = spareSoNo;
        this.orgId = orgId;
    }

    public String getSpareSoNo() {
        return spareSoNo;
    }

    public void setSpareSoNo(String spareSoNo) {
        this.spareSoNo = spareSoNo;
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
