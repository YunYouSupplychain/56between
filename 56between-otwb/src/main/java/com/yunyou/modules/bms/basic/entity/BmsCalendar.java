package com.yunyou.modules.bms.basic.entity;

import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 日历
 */
public class BmsCalendar extends DataEntity<BmsCalendar> {
    private static final long serialVersionUID = -8881390902302873282L;

    /**
     * 日期
     */
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date date;
    /**
     * 日历类型(1:普通假日 2:法定假日 3:工作日)
     */
    private String type;
    /**
     * 系数
     */
    private BigDecimal coefficient;
    /**
     * 机构ID
     */
    private String orgId;

    public BmsCalendar() {
    }

    public BmsCalendar(String id) {
        super(id);
    }

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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
