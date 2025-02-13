package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 路由Entity
 */
public class SysBmsRoute extends DataEntity<SysBmsRoute> {

    private static final long serialVersionUID = 1L;
    private String routeCode;// 路由代码
    private String routeName;// 路由名称
    private String startAreaId;// 起始地ID
    private String startAreaCode;// 起始地编码
    private String startAreaName;// 起始地名称
    private String endAreaId;// 目的地ID
    private String endAreaCode;// 目的地编码
    private String endAreaName;// 目的地名称
    private Double mileage;// 标准里程
    private Double timeliness;// 标准时效
    private String dataSet;// 数据套
    private String dataSetName;// 数据套名称

    public SysBmsRoute() {
        super();
    }

    public SysBmsRoute(String id) {
        super(id);
    }

    public SysBmsRoute(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    @ExcelField(title = "路由代码", type = 1, align = 2, sort = 6)
    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    @ExcelField(title = "路由名称", type = 1, align = 2, sort = 7)
    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStartAreaId() {
        return startAreaId;
    }

    public void setStartAreaId(String startAreaId) {
        this.startAreaId = startAreaId;
    }

    @ExcelField(title = "起始地编码", align = 2, sort = 9)
    public String getStartAreaCode() {
        return startAreaCode;
    }

    public void setStartAreaCode(String startAreaCode) {
        this.startAreaCode = startAreaCode;
    }

    @ExcelField(title = "起始地名称", align = 2, sort = 10)
    public String getStartAreaName() {
        return startAreaName;
    }

    public void setStartAreaName(String startAreaName) {
        this.startAreaName = startAreaName;
    }

    public String getEndAreaId() {
        return endAreaId;
    }

    public void setEndAreaId(String endAreaId) {
        this.endAreaId = endAreaId;
    }

    @ExcelField(title = "目的地编码", align = 2, sort = 12)
    public String getEndAreaCode() {
        return endAreaCode;
    }

    public void setEndAreaCode(String endAreaCode) {
        this.endAreaCode = endAreaCode;
    }

    @ExcelField(title = "目的地名称", align = 2, sort = 13)
    public String getEndAreaName() {
        return endAreaName;
    }

    public void setEndAreaName(String endAreaName) {
        this.endAreaName = endAreaName;
    }

    @ExcelField(title = "标准里程", align = 2, sort = 14)
    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    @ExcelField(title = "标准时效", align = 2, sort = 15)
    public Double getTimeliness() {
        return timeliness;
    }

    public void setTimeliness(Double timeliness) {
        this.timeliness = timeliness;
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