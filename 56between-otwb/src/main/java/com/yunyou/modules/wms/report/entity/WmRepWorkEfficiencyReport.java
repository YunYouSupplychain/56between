package com.yunyou.modules.wms.report.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

public class WmRepWorkEfficiencyReport extends DataEntity<WmRepWorkEfficiencyReport> {
    private static final long serialVersionUID = -7989082146331317080L;

    // 日期
    private String operateDate;
    // 序号
    private Integer serialNo;
    // 姓名
    private String operator;
    // 总件数
    private Long totalEaNum;
    // 总单数
    private Long totalOrderNum;
    // 单均件（H）
    private Double averageEaNum;
    // 作业时长
    private Double workingTime;
    // 件效率（件/H）
    private Double eaEfficiency;
    // 单效率（单/H）
    private Double orderEfficiency;

    @ExcelField(title = "日期", type = 1, align = 1, sort = 1)
    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    @ExcelField(title = "序号", type = 1, align = 1, sort = 2)
    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    @ExcelField(title = "姓名", type = 1, align = 1, sort = 3)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @ExcelField(title = "总件数", type = 1, align = 1, sort = 4)
    public Long getTotalEaNum() {
        return totalEaNum;
    }

    public void setTotalEaNum(Long totalEaNum) {
        this.totalEaNum = totalEaNum;
    }

    @ExcelField(title = "总单数", type = 1, align = 1, sort = 5)
    public Long getTotalOrderNum() {
        return totalOrderNum;
    }

    public void setTotalOrderNum(Long totalOrderNum) {
        this.totalOrderNum = totalOrderNum;
    }

    @ExcelField(title = "单均件", type = 1, align = 1, sort = 6)
    public Double getAverageEaNum() {
        return averageEaNum;
    }

    public void setAverageEaNum(Double averageEaNum) {
        this.averageEaNum = averageEaNum;
    }

    @ExcelField(title = "作业时长", type = 1, align = 1, sort = 7)
    public Double getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(Double workingTime) {
        this.workingTime = workingTime;
    }

    @ExcelField(title = "件效率（件/H）", type = 1, align = 1, sort = 8)
    public Double getEaEfficiency() {
        return eaEfficiency;
    }

    public void setEaEfficiency(Double eaEfficiency) {
        this.eaEfficiency = eaEfficiency;
    }

    @ExcelField(title = "单效率（单/H）", type = 1, align = 1, sort = 9)
    public Double getOrderEfficiency() {
        return orderEfficiency;
    }

    public void setOrderEfficiency(Double orderEfficiency) {
        this.orderEfficiency = orderEfficiency;
    }

}
