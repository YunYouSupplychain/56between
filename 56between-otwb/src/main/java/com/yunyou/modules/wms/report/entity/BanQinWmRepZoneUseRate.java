package com.yunyou.modules.wms.report.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

public class BanQinWmRepZoneUseRate extends DataEntity<BanQinWmRepZoneUseRate> {
    private static final long serialVersionUID = 8499070134984433695L;
    @ExcelField(title = "库区编码", type = 1, sort = 1)
    private String zoneCode;
    @ExcelField(title = "库区名称", type = 1, sort = 2)
    private String zoneName;
    @ExcelField(title = "总数量", type = 1, sort = 3)
    private Integer total;
    @ExcelField(title = "使用", type = 1, sort = 4)
    private Integer use;
    @ExcelField(title = "空余", type = 1, sort = 5)
    private Integer spare;
    @ExcelField(title = "使用率", type = 1, sort = 6)
    private String rate;
    private String orgId;

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getUse() {
        return use;
    }

    public void setUse(Integer use) {
        this.use = use;
    }

    public Integer getSpare() {
        return spare;
    }

    public void setSpare(Integer spare) {
        this.spare = spare;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
