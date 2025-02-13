package com.yunyou.modules.wms.outbound.entity;

import java.util.Date;

/**
 * 描述：交接清单Entity
 *
 * @author Jianhua on 2020-2-6
 */
public class BanQinWmOutHandoverHeaderEntity extends BanQinWmOutHandoverHeader {

    private String orgName; // 机构名称

    /*查询条件*/
    private Date handoverTimeFm;
    private Date handoverTimeTo;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getHandoverTimeFm() {
        return handoverTimeFm;
    }

    public void setHandoverTimeFm(Date handoverTimeFm) {
        this.handoverTimeFm = handoverTimeFm;
    }

    public Date getHandoverTimeTo() {
        return handoverTimeTo;
    }

    public void setHandoverTimeTo(Date handoverTimeTo) {
        this.handoverTimeTo = handoverTimeTo;
    }
}
