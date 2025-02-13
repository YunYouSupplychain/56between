package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 仓库区域Entity
 */
public class SysWmsArea extends DataEntity<SysWmsArea> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "区域编码不能为空")
    private String areaCode;// 区域编码
    @NotNull(message = "区域名称不能为空")
    private String areaName;// 区域名称
    private String def1;// 自定义1
    private String def2;// 自定义2
    private String def3;// 自定义3
    private String def4;// 自定义4
    private String def5;// 自定义5
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    private String dataSetName;// 数据套名称

    public SysWmsArea() {
        super();
    }

    public SysWmsArea(String id) {
        super(id);
    }

    public SysWmsArea(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
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