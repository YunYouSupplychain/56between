package com.yunyou.modules.bms.business.entity.extend;

import com.yunyou.modules.bms.business.entity.BmsOutboundData;

import java.util.Date;

/**
 * 描述：出库数据扩展实体
 *
 * @author Jianhua
 * @version 2019/6/26
 */
public class BmsOutboundDataEntity extends BmsOutboundData {

    private static final long serialVersionUID = -6035395027074767759L;
    private String skuClassName;
    private String orgName; // 机构名称

    /*额外查询条件*/
    private Date orderDateFm;
    private Date orderDateTo;
    private Date shipTimeFm;
    private Date shipTimeTo;

    public String getSkuClassName() {
        return skuClassName;
    }

    public void setSkuClassName(String skuClassName) {
        this.skuClassName = skuClassName;
    }

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

    public Date getShipTimeFm() {
        return shipTimeFm;
    }

    public void setShipTimeFm(Date shipTimeFm) {
        this.shipTimeFm = shipTimeFm;
    }

    public Date getShipTimeTo() {
        return shipTimeTo;
    }

    public void setShipTimeTo(Date shipTimeTo) {
        this.shipTimeTo = shipTimeTo;
    }
}
