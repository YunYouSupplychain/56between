package com.yunyou.modules.bms.basic.entity.extend;

import com.yunyou.modules.bms.basic.entity.BmsCalendar;

import java.util.Date;

public class BmsCalendarEntity extends BmsCalendar {
    private static final long serialVersionUID = 8972034119166441929L;

    private Date fmDate;
    private Date toDate;

    private String orgName;

    public Date getFmDate() {
        return fmDate;
    }

    public void setFmDate(Date fmDate) {
        this.fmDate = fmDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
