package com.yunyou.modules.tms.report.entity;

import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmGasOilStatistics extends DataEntity<TmGasOilStatistics> {

    private String gasCode;
    private String gasName;
    private String date;
    private String oilCode;
    private String oilName;
    private Double qty;

    private String timeDimension; // 按天、按月、按年
    private Date fmDate;
    private Date toDate;
    private String baseOrgId;
    private String orgId;
    private String codeAndName;

    public String getGasCode() {
        return gasCode;
    }

    public void setGasCode(String gasCode) {
        this.gasCode = gasCode;
    }

    public String getGasName() {
        return gasName;
    }

    public void setGasName(String gasName) {
        this.gasName = gasName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOilCode() {
        return oilCode;
    }

    public void setOilCode(String oilCode) {
        this.oilCode = oilCode;
    }

    public String getOilName() {
        return oilName;
    }

    public void setOilName(String oilName) {
        this.oilName = oilName;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Date getFmDate() {
        return fmDate;
    }

    public void setFmDate(Date fmDate) {
        this.fmDate = fmDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

    public String getTimeDimension() {
        return timeDimension;
    }

    public void setTimeDimension(String timeDimension) {
        this.timeDimension = timeDimension;
    }

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }
}
