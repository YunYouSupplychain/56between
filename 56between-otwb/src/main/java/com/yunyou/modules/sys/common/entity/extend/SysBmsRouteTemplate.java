package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;

/**
 * 描述：路由导入模板
 *
 * @author liujianhua created on 2019-11-20
 */
public class SysBmsRouteTemplate implements Serializable {

    @ExcelField(title = "路由名称", align = 2)
    private String routeName;
    @ExcelField(title = "起始地编码", align = 2)
    private String startAreaCode;
    @ExcelField(title = "起始地名称", align = 2)
    private String startAreaName;
    @ExcelField(title = "目的地编码", align = 2)
    private String endAreaCode;
    @ExcelField(title = "目的地名称", align = 2)
    private String endAreaName;
    @ExcelField(title = "标准里程", align = 2)
    private Double mileage;
    @ExcelField(title = "标准时效", align = 2)
    private Double timeliness;
    @ExcelField(title = "数据套", align = 2)
    private String dataSet;

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStartAreaCode() {
        return startAreaCode;
    }

    public void setStartAreaCode(String startAreaCode) {
        this.startAreaCode = startAreaCode;
    }

    public String getStartAreaName() {
        return startAreaName;
    }

    public void setStartAreaName(String startAreaName) {
        this.startAreaName = startAreaName;
    }

    public String getEndAreaCode() {
        return endAreaCode;
    }

    public void setEndAreaCode(String endAreaCode) {
        this.endAreaCode = endAreaCode;
    }

    public String getEndAreaName() {
        return endAreaName;
    }

    public void setEndAreaName(String endAreaName) {
        this.endAreaName = endAreaName;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

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
}
