package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmVehicleSafetyCheck;

import java.util.Date;

public class TmVehicleSafetyCheckEntity extends TmVehicleSafetyCheck {
    private static final long serialVersionUID = 1L;
    // 订单时间从
    private Date checkDateFm;
    // 订单时间到
    private Date checkDateTo;

    public Date getCheckDateFm() {
        return checkDateFm;
    }

    public void setCheckDateFm(Date checkDateFm) {
        this.checkDateFm = checkDateFm;
    }

    public Date getCheckDateTo() {
        return checkDateTo;
    }

    public void setCheckDateTo(Date checkDateTo) {
        this.checkDateTo = checkDateTo;
    }
}
