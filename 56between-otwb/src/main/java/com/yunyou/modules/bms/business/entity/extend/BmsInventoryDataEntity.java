package com.yunyou.modules.bms.business.entity.extend;

import com.yunyou.modules.bms.business.entity.BmsInventoryData;

import java.util.Date;

/**
 * 描述：库存数据扩展实体
 *
 * @author Jianhua
 * @version 2019/7/11
 */
public class BmsInventoryDataEntity extends BmsInventoryData {
    private static final long serialVersionUID = -6596637745844713919L;
    private String skuClassName;
    // 机构名称
    private String orgName;

    /*查询条件*/
    private Date dateFm;
    private Date dateTo;

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

    public Date getDateFm() {
        return dateFm;
    }

    public void setDateFm(Date dateFm) {
        this.dateFm = dateFm;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
