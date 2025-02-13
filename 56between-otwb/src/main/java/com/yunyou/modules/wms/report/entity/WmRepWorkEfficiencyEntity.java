package com.yunyou.modules.wms.report.entity;

import java.io.Serializable;
import java.util.Date;

public class WmRepWorkEfficiencyEntity implements Serializable {
    private static final long serialVersionUID = -3656601219822454885L;
    // 姓名
    private String operator;
    // 单号
    private String orderNo;
    // 操作日期
    private Date operateDate;
    // 数量
    private Long orderNum;
    private String orgId;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
