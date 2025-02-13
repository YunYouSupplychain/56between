package com.yunyou.modules.wms.outbound.entity;

import java.util.Date;

/**
 * 更新收货信息日志
 * @author WMJ
 * @version 2020-03-19
 */
public class BanQinWmUpdateConsigneeInfoLogEntity extends BanQinWmUpdateConsigneeInfoLog {
    private Date createDateFm;
    private Date createDateTo;
    private String trackingNo;

    public Date getCreateDateFm() {
        return createDateFm;
    }

    public void setCreateDateFm(Date createDateFm) {
        this.createDateFm = createDateFm;
    }

    public Date getCreateDateTo() {
        return createDateTo;
    }

    public void setCreateDateTo(Date createDateTo) {
        this.createDateTo = createDateTo;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }
}
