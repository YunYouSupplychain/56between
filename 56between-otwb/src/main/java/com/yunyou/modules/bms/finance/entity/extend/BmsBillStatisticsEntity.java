package com.yunyou.modules.bms.finance.entity.extend;

import com.yunyou.modules.bms.finance.entity.BmsBillStatistics;

import java.util.Date;

/**
 * 描述：
 * <p>
 * create by Jianhua on 2019/7/15
 */
public class BmsBillStatisticsEntity extends BmsBillStatistics {
    private static final long serialVersionUID = -4764357490735932233L;
    // 机构名称
    private String orgName;

    /*查询条件*/
    // 创建人
    private String creator;
    // 创建时间从
    private Date createTimeFm;
    // 创建时间到
    private Date createTimeTo;
    // 排除为0量
    private String filterZero;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTimeFm() {
        return createTimeFm;
    }

    public void setCreateTimeFm(Date createTimeFm) {
        this.createTimeFm = createTimeFm;
    }

    public Date getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(Date createTimeTo) {
        this.createTimeTo = createTimeTo;
    }

    public String getFilterZero() {
        return filterZero;
    }

    public void setFilterZero(String filterZero) {
        this.filterZero = filterZero;
    }
}
