package com.yunyou.modules.bms.business.entity.extend;

import com.yunyou.modules.bms.business.entity.BmsWaybillData;

import java.util.Date;

/**
 * 描述：运单数据扩展实体
 *
 * @author Jianhua
 * @version 2019/7/11
 */
public class BmsWaybillDataEntity extends BmsWaybillData {

    private static final long serialVersionUID = 4885255161143375575L;
    // 机构名称
    private String orgName;
    private String skuClassName;

    /*查询条件*/
    private Date orderDateFm;
    private Date orderDateTo;

    public String getSkuClassName() {
        return skuClassName;
    }

    public void setSkuClassName(String skuClassName) {
        this.skuClassName = skuClassName;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

}
