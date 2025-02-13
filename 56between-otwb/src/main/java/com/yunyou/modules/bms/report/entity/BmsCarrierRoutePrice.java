package com.yunyou.modules.bms.report.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 承运商路由价格
 */
public class BmsCarrierRoutePrice extends DataEntity<BmsCarrierRoutePrice> {
    private static final long serialVersionUID = 1602581750264058770L;
    // 承运商编码
    private String carrierCode;
    @ExcelField(title = "承运商", type = 1)
    private String carrierName;
    // 起始地编码
    private String startAreaCode;
    @ExcelField(title = "仓库", type = 1)
    private String startAreaName;
    // 目的地编码
    private String endAreaCode;
    @ExcelField(title = "配送店", type = 1)
    private String endAreaName;
    @ExcelField(title = "距离", type = 1)
    private Double mileage;
    @ExcelField(title = "车型", type = 1)
    private String carType;
    // 运输价格体系编码
    private String transportGroupCode;
    @ExcelField(title = "运输价格体系", type = 1)
    private String transportGroupName;
    @ExcelField(title = "阶梯范围从", type = 1)
    private Double fm;
    @ExcelField(title = "阶梯范围到", type = 1)
    private Double to;
    @ExcelField(title = "单价", type = 1)
    private Double price;
    // 机构ID
    private String orgId;

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getTransportGroupCode() {
        return transportGroupCode;
    }

    public void setTransportGroupCode(String transportGroupCode) {
        this.transportGroupCode = transportGroupCode;
    }

    public String getTransportGroupName() {
        return transportGroupName;
    }

    public void setTransportGroupName(String transportGroupName) {
        this.transportGroupName = transportGroupName;
    }

    public Double getFm() {
        return fm;
    }

    public void setFm(Double fm) {
        this.fm = fm;
    }

    public Double getTo() {
        return to;
    }

    public void setTo(Double to) {
        this.to = to;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
