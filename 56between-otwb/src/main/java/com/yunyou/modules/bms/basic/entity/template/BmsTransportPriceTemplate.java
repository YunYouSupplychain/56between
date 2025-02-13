package com.yunyou.modules.bms.basic.entity.template;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;

public class BmsTransportPriceTemplate implements Serializable {
    private static final long serialVersionUID = -1233359173760394006L;

    @ExcelField(title = "起点编码")
    private String startPlaceCode;
    @ExcelField(title = "起点名称")
    private String startPlaceName;
    @ExcelField(title = "终点编码")
    private String endPlaceCode;
    @ExcelField(title = "终点名称")
    private String endPlaceName;
    @ExcelField(title = "车型编码")
    private String carTypeCode;
    @ExcelField(title = "车型")
    private String carType;
    @ExcelField(title = "区域编码")
    private String regionCode;
    @ExcelField(title = "区域名称")
    private String regionName;
    @ExcelField(title = "单价")
    private BigDecimal price;
    @ExcelField(title = "物流点数")
    private BigDecimal logisticsPoints;
    @ExcelField(title = "区间1从")
    private BigDecimal fm1;
    @ExcelField(title = "区间1到")
    private BigDecimal to1;
    @ExcelField(title = "单价1")
    private BigDecimal price1;
    @ExcelField(title = "区间2从")
    private BigDecimal fm2;
    @ExcelField(title = "区间2到")
    private BigDecimal to2;
    @ExcelField(title = "单价2")
    private BigDecimal price2;
    @ExcelField(title = "区间3从")
    private BigDecimal fm3;
    @ExcelField(title = "区间3到")
    private BigDecimal to3;
    @ExcelField(title = "单价3")
    private BigDecimal price3;
    @ExcelField(title = "区间4从")
    private BigDecimal fm4;
    @ExcelField(title = "区间4到")
    private BigDecimal to4;
    @ExcelField(title = "单价4")
    private BigDecimal price4;
    @ExcelField(title = "区间5从")
    private BigDecimal fm5;
    @ExcelField(title = "区间5到")
    private BigDecimal to5;
    @ExcelField(title = "单价5")
    private BigDecimal price5;

    public String getStartPlaceCode() {
        return StringUtils.isNotBlank(startPlaceCode) ? startPlaceCode : "";
    }

    public void setStartPlaceCode(String startPlaceCode) {
        this.startPlaceCode = startPlaceCode;
    }

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public void setStartPlaceName(String startPlaceName) {
        this.startPlaceName = startPlaceName;
    }

    public String getEndPlaceCode() {
        return StringUtils.isNotBlank(endPlaceCode) ? endPlaceCode : "";
    }

    public void setEndPlaceCode(String endPlaceCode) {
        this.endPlaceCode = endPlaceCode;
    }

    public String getEndPlaceName() {
        return endPlaceName;
    }

    public void setEndPlaceName(String endPlaceName) {
        this.endPlaceName = endPlaceName;
    }

    public String getCarTypeCode() {
        return StringUtils.isNotBlank(carTypeCode) ? carTypeCode : "";
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getRegionCode() {
        return StringUtils.isNotBlank(regionCode) ? regionCode : "";
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getLogisticsPoints() {
        return logisticsPoints;
    }

    public void setLogisticsPoints(BigDecimal logisticsPoints) {
        this.logisticsPoints = logisticsPoints;
    }

    public BigDecimal getFm1() {
        return fm1;
    }

    public void setFm1(BigDecimal fm1) {
        this.fm1 = fm1;
    }

    public BigDecimal getTo1() {
        return to1;
    }

    public void setTo1(BigDecimal to1) {
        this.to1 = to1;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getFm2() {
        return fm2;
    }

    public void setFm2(BigDecimal fm2) {
        this.fm2 = fm2;
    }

    public BigDecimal getTo2() {
        return to2;
    }

    public void setTo2(BigDecimal to2) {
        this.to2 = to2;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getFm3() {
        return fm3;
    }

    public void setFm3(BigDecimal fm3) {
        this.fm3 = fm3;
    }

    public BigDecimal getTo3() {
        return to3;
    }

    public void setTo3(BigDecimal to3) {
        this.to3 = to3;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public BigDecimal getFm4() {
        return fm4;
    }

    public void setFm4(BigDecimal fm4) {
        this.fm4 = fm4;
    }

    public BigDecimal getTo4() {
        return to4;
    }

    public void setTo4(BigDecimal to4) {
        this.to4 = to4;
    }

    public BigDecimal getPrice4() {
        return price4;
    }

    public void setPrice4(BigDecimal price4) {
        this.price4 = price4;
    }

    public BigDecimal getFm5() {
        return fm5;
    }

    public void setFm5(BigDecimal fm5) {
        this.fm5 = fm5;
    }

    public BigDecimal getTo5() {
        return to5;
    }

    public void setTo5(BigDecimal to5) {
        this.to5 = to5;
    }

    public BigDecimal getPrice5() {
        return price5;
    }

    public void setPrice5(BigDecimal price5) {
        this.price5 = price5;
    }
}
