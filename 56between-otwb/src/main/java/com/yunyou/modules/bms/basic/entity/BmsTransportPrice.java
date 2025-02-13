package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * 描述：运输价格
 *
 * @author liujianhua
 * @version 2019-11-14
 */
public class BmsTransportPrice extends DataEntity<BmsTransportPrice> {

    private static final long serialVersionUID = 1L;
    /**
     * 运输价格体系表ID
     */
    private String fkId;
    /**
     * 起点编码
     */
    private String startPlaceCode;
    /**
     * 终点编码
     */
    private String endPlaceCode;
    /**
     * 区域编码
     */
    private String regionCode;
    /**
     * 车型编码
     */
    private String carTypeCode;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 物流点数
     */
    private BigDecimal logisticsPoints;
    /**
     * 单位
     */
    private String unit;
    /**
     * 是否启用阶梯价格
     */
    private String isUseStep;
    /**
     * 是否阶梯分段累加方式计算
     */
    private String isAccumulationMethod;
    /**
     * 机构ID
     */
    private String orgId;
    /**
     * 阶梯价格
     */
    private List<BmsTransportSteppedPrice> steppedPrices = Lists.newArrayList();

    public BmsTransportPrice() {
    }

    public BmsTransportPrice(String id) {
        super(id);
    }

    public String getFkId() {
        return fkId;
    }

    public void setFkId(String fkId) {
        this.fkId = fkId;
    }

    public String getStartPlaceCode() {
        return startPlaceCode;
    }

    public void setStartPlaceCode(String startPlaceCode) {
        this.startPlaceCode = startPlaceCode;
    }

    public String getEndPlaceCode() {
        return endPlaceCode;
    }

    public void setEndPlaceCode(String endPlaceCode) {
        this.endPlaceCode = endPlaceCode;
    }

    public String getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIsUseStep() {
        return isUseStep;
    }

    public void setIsUseStep(String isUseStep) {
        this.isUseStep = isUseStep;
    }

    public String getIsAccumulationMethod() {
        return isAccumulationMethod;
    }

    public void setIsAccumulationMethod(String isAccumulationMethod) {
        this.isAccumulationMethod = isAccumulationMethod;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<BmsTransportSteppedPrice> getSteppedPrices() {
        return steppedPrices;
    }

    public void setSteppedPrices(List<BmsTransportSteppedPrice> steppedPrices) {
        this.steppedPrices = steppedPrices;
    }
}