package com.yunyou.modules.bms.basic.entity.template;

import com.yunyou.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 描述：承运商路由导入模板
 *
 * @author liujianhua
 * @version 2019-11-20
 */
public class BmsCarrierRouteTemplate implements Serializable {

    private static final long serialVersionUID = -5024684892920478276L;
    @ExcelField(title = "承运商编码", align = 2)
    private String carrierCode;
    @ExcelField(title = "路由名称", align = 2)
    private String routeName;
    @ExcelField(title = "起始地编码", align = 2)
    private String startAreaCode;
    @ExcelField(title = "目的地编码", align = 2)
    private String endAreaCode;
    @ExcelField(title = "标准里程", align = 2)
    private BigDecimal mileage;
    @ExcelField(title = "标准时效", align = 2)
    private BigDecimal timeliness;

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

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

    public String getEndAreaCode() {
        return endAreaCode;
    }

    public void setEndAreaCode(String endAreaCode) {
        this.endAreaCode = endAreaCode;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public BigDecimal getTimeliness() {
        return timeliness;
    }

    public void setTimeliness(BigDecimal timeliness) {
        this.timeliness = timeliness;
    }

}
