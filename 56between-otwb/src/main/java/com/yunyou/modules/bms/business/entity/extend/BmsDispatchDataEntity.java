package com.yunyou.modules.bms.business.entity.extend;

import com.yunyou.modules.bms.business.entity.BmsDispatchData;

import java.util.Date;

/**
 * 描述：派车配载数据扩展实体
 *
 * @author Jianhua
 * @version 2019/7/11
 */
public class BmsDispatchDataEntity extends BmsDispatchData {

    private static final long serialVersionUID = 4885255161143375575L;
    // 机构名称
    private String orgName;

    /*查询条件*/
    private Date orderDateFm;
    private Date orderDateTo;
    private Date dispatchTimeFm;
    private Date dispatchTimeTo;

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getDispatchTimeFm() {
        return dispatchTimeFm;
    }

    public void setDispatchTimeFm(Date dispatchTimeFm) {
        this.dispatchTimeFm = dispatchTimeFm;
    }

    public Date getDispatchTimeTo() {
        return dispatchTimeTo;
    }

    public void setDispatchTimeTo(Date dispatchTimeTo) {
        this.dispatchTimeTo = dispatchTimeTo;
    }
}
