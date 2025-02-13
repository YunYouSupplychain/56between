package com.yunyou.modules.bms.business.entity.extend;

import com.yunyou.modules.bms.business.entity.BmsDispatchOrderData;

import java.util.Date;

/**
 * 描述：派车单数据扩展实体
 *
 * @author Jianhua
 * @version 2019/7/11
 */
public class BmsDispatchOrderDataEntity extends BmsDispatchOrderData {

    private static final long serialVersionUID = -2244431798327647229L;
    // 机构名称
    private String orgName;

    // 查询条件
    private Date orderDateFm;
    private Date orderDateTo;
    private Date dispatchDateFm;
    private Date dispatchDateTo;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getOrderDateFm() {
        return orderDateFm;
    }

    public void setOrderDateFm(Date orderDateFm) {
        this.orderDateFm = orderDateFm;
    }

    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(Date orderDateTo) {
        this.orderDateTo = orderDateTo;
    }

    public Date getDispatchDateFm() {
        return dispatchDateFm;
    }

    public void setDispatchDateFm(Date dispatchDateFm) {
        this.dispatchDateFm = dispatchDateFm;
    }

    public Date getDispatchDateTo() {
        return dispatchDateTo;
    }

    public void setDispatchDateTo(Date dispatchDateTo) {
        this.dispatchDateTo = dispatchDateTo;
    }
}
