package com.yunyou.modules.tms.applet.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TmMiddleSalaryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String remarks;
    // 工资日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date salaryDate;
    // 人员编码
    private String personCode;
    // 单据号
    private String orderNo;
    // 工资类型
    private String salaryType;
    // 时长
    private BigDecimal duration;
    // 工资系数
    private BigDecimal salaryCoefficient;
    // 工资基数
    private BigDecimal salaryBase;
    // 工资金额
    private BigDecimal salaryAmount;
    // 装罐数
    private BigDecimal tankQty;
    // 装罐单价
    private BigDecimal tankPrice;
    // 装罐费
    private BigDecimal tankAmount;
    // 基础数据机构ID
    private String baseOrgId;
    // 版本号
    private Long recVer = 0L;

    // 检查时间从
    private Date salaryDateFm;
    // 检查时间到
    private Date salaryDateTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(Date salaryDate) {
        this.salaryDate = salaryDate;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public BigDecimal getSalaryCoefficient() {
        return salaryCoefficient;
    }

    public void setSalaryCoefficient(BigDecimal salaryCoefficient) {
        this.salaryCoefficient = salaryCoefficient;
    }

    public BigDecimal getSalaryBase() {
        return salaryBase;
    }

    public void setSalaryBase(BigDecimal salaryBase) {
        this.salaryBase = salaryBase;
    }

    public BigDecimal getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(BigDecimal salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public BigDecimal getTankQty() {
        return tankQty;
    }

    public void setTankQty(BigDecimal tankQty) {
        this.tankQty = tankQty;
    }

    public BigDecimal getTankPrice() {
        return tankPrice;
    }

    public void setTankPrice(BigDecimal tankPrice) {
        this.tankPrice = tankPrice;
    }

    public BigDecimal getTankAmount() {
        return tankAmount;
    }

    public void setTankAmount(BigDecimal tankAmount) {
        this.tankAmount = tankAmount;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public Long getRecVer() {
        return recVer;
    }

    public void setRecVer(Long recVer) {
        this.recVer = recVer;
    }

    public Date getSalaryDateFm() {
        return salaryDateFm;
    }

    public void setSalaryDateFm(Date salaryDateFm) {
        this.salaryDateFm = salaryDateFm;
    }

    public Date getSalaryDateTo() {
        return salaryDateTo;
    }

    public void setSalaryDateTo(Date salaryDateTo) {
        this.salaryDateTo = salaryDateTo;
    }
}
