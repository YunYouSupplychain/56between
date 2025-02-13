package com.yunyou.modules.tms.report.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmGasDailyStatistics extends DataEntity<TmGasDailyStatistics> {
    private static final long serialVersionUID = -8683803891459584252L;

    @ExcelField(title = "加油站编码")
    private String gasCode;
    @ExcelField(title = "加油站名称")
    private String gasName;
    @ExcelField(title = "日期")
    private String date;
    @ExcelField(title = "油品编码")
    private String oilCode;
    @ExcelField(title = "油品名称")
    private String oilName;
    @ExcelField(title = "调度量")
    private Double orderQty;
    @ExcelField(title = "提油量")
    private Double pickupQty;
    @ExcelField(title = "卸油量")
    private Double dischargeQty;

    private Date queryDate;
    private Date fmDate;
    private Date toDate;
    private String baseOrgId;
    private String orgId;

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

    public Double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Double orderQty) {
        this.orderQty = orderQty;
    }

    public Double getPickupQty() {
        return pickupQty;
    }

    public void setPickupQty(Double pickupQty) {
        this.pickupQty = pickupQty;
    }

    public Double getDischargeQty() {
        return dischargeQty;
    }

    public void setDischargeQty(Double dischargeQty) {
        this.dischargeQty = dischargeQty;
    }

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
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
}
