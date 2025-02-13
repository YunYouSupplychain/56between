package com.yunyou.modules.tms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class TmTransportationProfit extends DataEntity<TmTransportationProfit> {
    private static final long serialVersionUID = 1414814606627969995L;

    @ExcelField(title = "运输单号", type = 1, sort = 1)
    private String transportNo;
    @ExcelField(title = "客户编码", type = 1, sort = 2)
    private String customerCode;
    @ExcelField(title = "客户名称", type = 1, sort = 3)
    private String customerName;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    @ExcelField(title = "订单时间", type = 1, sort = 4)
    private Date orderTime;
    @ExcelField(title = "发货地区", type = 1, sort = 5)
    private String shipCity;
    @ExcelField(title = "收货地区", type = 1, sort = 6)
    private String consigneeCity;
    @ExcelField(title = "收入金额", type = 1, sort = 7)
    private Double income;
    @ExcelField(title = "成本金额", type = 1, sort = 8)
    private Double expenditure;
    @ExcelField(title = "利润金额", type = 1, sort = 9)
    private Double profit;
    @ExcelField(title = "利润率", type = 1, sort = 10)
    private String profitRate;
    private String baseOrgId;
    private String orgId;

    private Date fmDate;
    private Date toDate;

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public String getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(String profitRate) {
        this.profitRate = profitRate;
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
}
