package com.yunyou.modules.bms.business.entity.extend;

import com.yunyou.modules.bms.business.entity.BmsExceptionData;

import java.util.Date;


/**
 * 描述：异常数据扩展实体
 *
 * @author Jianhua
 * @version 2019/7/11
 */
public class BmsExceptionDataEntity extends BmsExceptionData {

    private static final long serialVersionUID = 7578990814362607696L;
    private String skuClassName;
    private String orgName;

    /*查询条件*/
    private Date orderDateFm;
    private Date orderDateTo;
    private Date dispatchDateFm;
    private Date dispatchDateTo;

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
