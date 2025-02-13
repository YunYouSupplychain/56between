package com.yunyou.modules.bms.interactive;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：BMS拉取数据条件项
 *
 * @author Jianhua
 * @version 2019/7/10
 */
public class BmsPullDataCondition implements Serializable {
    private static final long serialVersionUID = -2766636611702727285L;
    public static final char DATA_TYPE_INBOUND = '0';
    public static final char DATA_TYPE_OUTBOUND = '1';
    public static final char DATA_TYPE_INVENTORY = '2';
    public static final char DATA_TYPE_DISPATCH = '3';
    public static final char DATA_TYPE_EXCEPTION = '4';
    public static final char DATA_TYPE_RETURN = '5';
    public static final char DATA_TYPE_DISPATCH_ORDER = '6';
    public static final char DATA_TYPE_WAYBILL_ORDER = '7';
    // 数据类型
    private char dateType;
    // 日期从
    private Date fmDate;
    // 日期到
    private Date toDate;
    // 数据来源
    private String dataSources;
    // 机构ID
    private String orgId;

    public BmsPullDataCondition() {
    }

    public BmsPullDataCondition(char dateType, Date fmDate, Date toDate) {
        this.dateType = dateType;
        this.fmDate = fmDate;
        this.toDate = toDate;
    }

    public char getDateType() {
        return dateType;
    }

    public void setDateType(char dateType) {
        this.dateType = dateType;
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

    public String getDataSources() {
        return dataSources;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}
