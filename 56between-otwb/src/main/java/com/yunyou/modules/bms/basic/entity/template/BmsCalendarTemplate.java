package com.yunyou.modules.bms.basic.entity.template;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BmsCalendarTemplate implements Serializable {
    private static final long serialVersionUID = -193209456983701374L;

    @ExcelField(title = "日期", type = 2)
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date date;
    @ExcelField(title = "类型", type = 2)
    private String type;
    @ExcelField(title = "系数", type = 2)
    private BigDecimal coefficient;
    @ExcelField(title = "备注", type = 2)
    private String remarks;
    @ExcelField(title = "机构编码", type = 2)
    private String orgCode;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
