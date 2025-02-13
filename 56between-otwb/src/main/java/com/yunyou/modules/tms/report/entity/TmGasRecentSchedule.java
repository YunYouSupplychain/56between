package com.yunyou.modules.tms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmGasRecentSchedule extends DataEntity<TmGasRecentSchedule> {
    private static final long serialVersionUID = 2299675125607735037L;

    @ExcelField(title = "客户编码", type = 1, sort = 1)
    private String ownerCode;
    @ExcelField(title = "客户名称", type = 1, sort = 2)
    private String ownerName;
    @ExcelField(title = "商品编码", type = 1, sort = 3)
    private String skuCode;
    @ExcelField(title = "商品名称", type = 1, sort = 4)
    private String skuName;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    @ExcelField(title = "最近一次计划时间", type = 1, sort = 5)
    private Date lastPlanTime;
    @ExcelField(title = "计划量", type = 1, sort = 6)
    private Double planQty;
    @ExcelField(title = "近30日日均量", type = 1, sort = 7)
    private Double dailyAverageQty;
    private String baseOrgId;
    private String orgId;

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Date getLastPlanTime() {
        return lastPlanTime;
    }

    public void setLastPlanTime(Date lastPlanTime) {
        this.lastPlanTime = lastPlanTime;
    }

    public Double getPlanQty() {
        return planQty;
    }

    public void setPlanQty(Double planQty) {
        this.planQty = planQty;
    }

    public Double getDailyAverageQty() {
        return dailyAverageQty;
    }

    public void setDailyAverageQty(Double dailyAverageQty) {
        this.dailyAverageQty = dailyAverageQty;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
