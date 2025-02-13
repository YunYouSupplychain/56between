package com.yunyou.modules.bms.business.entity.extend;

import com.yunyou.modules.bms.business.entity.BmsReturnData;

import java.util.Date;

/**
 * 描述：退货数据扩展实体
 *
 * @author Jianhua
 * @version 2019/7/11
 */
public class BmsReturnDataEntity extends BmsReturnData {
    private static final long serialVersionUID = -5644008039791659301L;
    // 机构名称
    private String orgName;

    /*额外查询条件*/
    private Date orderDateFm;
    private Date orderDateTo;

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
}
