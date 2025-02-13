package com.yunyou.modules.wms.inventory.entity;

import java.math.BigDecimal;

/**
 * 进出存合计Entity
 * @author WMJ
 * @version 2020-04-23
 */
public class BanQinWmActInOutEntity extends BanQinWmActLog {
    private String year;
    private String month;
    private String days;
    private BigDecimal plBefore;
    private BigDecimal qtyBefore;
    private BigDecimal grossWeightBefore;
    private BigDecimal plIn;
    private BigDecimal qtyIn;
    private BigDecimal grossWeightIn;
    private BigDecimal plOut;
    private BigDecimal qtyOut;
    private BigDecimal grossWeightOut;
    private BigDecimal plAfter;
    private BigDecimal qtyAfter;
    private BigDecimal grossWeightAfter;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public BigDecimal getPlBefore() {
        return plBefore;
    }

    public void setPlBefore(BigDecimal plBefore) {
        this.plBefore = plBefore;
    }

    public BigDecimal getQtyBefore() {
        return qtyBefore;
    }

    public void setQtyBefore(BigDecimal qtyBefore) {
        this.qtyBefore = qtyBefore;
    }

    public BigDecimal getGrossWeightBefore() {
        return grossWeightBefore;
    }

    public void setGrossWeightBefore(BigDecimal grossWeightBefore) {
        this.grossWeightBefore = grossWeightBefore;
    }

    public BigDecimal getPlIn() {
        return plIn;
    }

    public void setPlIn(BigDecimal plIn) {
        this.plIn = plIn;
    }

    public BigDecimal getQtyIn() {
        return qtyIn;
    }

    public void setQtyIn(BigDecimal qtyIn) {
        this.qtyIn = qtyIn;
    }

    public BigDecimal getGrossWeightIn() {
        return grossWeightIn;
    }

    public void setGrossWeightIn(BigDecimal grossWeightIn) {
        this.grossWeightIn = grossWeightIn;
    }

    public BigDecimal getPlOut() {
        return plOut;
    }

    public void setPlOut(BigDecimal plOut) {
        this.plOut = plOut;
    }

    public BigDecimal getQtyOut() {
        return qtyOut;
    }

    public void setQtyOut(BigDecimal qtyOut) {
        this.qtyOut = qtyOut;
    }

    public BigDecimal getGrossWeightOut() {
        return grossWeightOut;
    }

    public void setGrossWeightOut(BigDecimal grossWeightOut) {
        this.grossWeightOut = grossWeightOut;
    }

    public BigDecimal getPlAfter() {
        return plAfter;
    }

    public void setPlAfter(BigDecimal plAfter) {
        this.plAfter = plAfter;
    }

    public BigDecimal getQtyAfter() {
        return qtyAfter;
    }

    public void setQtyAfter(BigDecimal qtyAfter) {
        this.qtyAfter = qtyAfter;
    }

    public BigDecimal getGrossWeightAfter() {
        return grossWeightAfter;
    }

    public void setGrossWeightAfter(BigDecimal grossWeightAfter) {
        this.grossWeightAfter = grossWeightAfter;
    }
}