package com.yunyou.modules.bms.finance.entity.extend;

import com.yunyou.modules.bms.finance.entity.BmsBillDetail;

import java.util.Date;

/**
 * 描述：费用明细扩展实体
 *
 * @author Jianhua
 * @version 2019/7/15
 */
public class BmsBillDetailEntity extends BmsBillDetail {

    private static final long serialVersionUID = 8053252294480500909L;
    // 机构名称
    private String orgName;
    // 业务日期从
    private Date businessDateFm;
    // 业务日期到
    private Date businessDateTo;
    // 排除为0量
    private String filterZero;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getBusinessDateFm() {
        return businessDateFm;
    }

    public void setBusinessDateFm(Date businessDateFm) {
        this.businessDateFm = businessDateFm;
    }

    public Date getBusinessDateTo() {
        return businessDateTo;
    }

    public void setBusinessDateTo(Date businessDateTo) {
        this.businessDateTo = businessDateTo;
    }

    public String getFilterZero() {
        return filterZero;
    }

    public void setFilterZero(String filterZero) {
        this.filterZero = filterZero;
    }
}
