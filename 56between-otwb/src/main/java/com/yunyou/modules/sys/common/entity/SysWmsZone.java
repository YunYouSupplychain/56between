package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 库区Entity
 */
public class SysWmsZone extends DataEntity<SysWmsZone> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "库区编码不能为空")
    @ExcelField(title = "库区编码**必填")
    private String zoneCode;
    @NotNull(message = "库区名称不能为空")
    @ExcelField(title = "库区名称**必填")
    private String zoneName;
    @ExcelField(title = "库区类型**必填 填写\n平面仓库区\n货架仓库区\n高架仓库区\n重力式货架库区\n堆垛机货架库区", dictType = "SYS_WM_ZONE_TYPE")
    private String type;
    @NotNull(message = "区域编码不能为空")
    @ExcelField(title = "区域编码**必填")
    private String areaCode;
    @ExcelField(title = "区域名称", type = 1)
    private String areaName;
    private String def1;// 自定义1
    private String def2;// 自定义2
    private String def3;// 自定义3
    private String def4;// 自定义4
    private String def5;// 自定义5
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private String dataSetName;// 数据套名称

    public SysWmsZone() {
        super();
    }

    public SysWmsZone(String id) {
        super(id);
    }

    public SysWmsZone(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}