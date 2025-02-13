package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysCommonSkuClassification;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SysCommonSkuClassificationEntity extends SysCommonSkuClassification {

    private String dataSetName;// 数据套名称
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toDate;

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
