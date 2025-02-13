package com.yunyou.modules.bms.calculate.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 费用计算业务数据
 *
 * @author liujianhua
 * @version 2022.8.11
 */
public class BmsCalcBusinessData {

    /**
     * 业务数据机构ID
     */
    private String businessOrgId;
    /**
     * 业务数据机构编码
     */
    private String businessOrgCode;
    /**
     * 业务数据机构名称
     */
    private String businessOrgName;
    /**
     * 业务时间
     */
    private Date businessDate;
    /**
     * 商品编码
     */
    private String skuCode;
    /**
     * 品类
     */
    private String skuClass;
    /**
     * 车型编码
     */
    private String carTypeCode;
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
     * 业务数据ID
     */
    private List<String> businessDataIds;
    /**
     * 商品量参数
     */
    private List<BmsBusinessSkuParams> skuParams;
    /**
     * 计费条款输出对象值
     */
    private BigDecimal outputValue;

    public String getBusinessOrgId() {
        return businessOrgId;
    }

    public void setBusinessOrgId(String businessOrgId) {
        this.businessOrgId = businessOrgId;
    }

    public String getBusinessOrgCode() {
        return businessOrgCode;
    }

    public void setBusinessOrgCode(String businessOrgCode) {
        this.businessOrgCode = businessOrgCode;
    }

    public String getBusinessOrgName() {
        return businessOrgName;
    }

    public void setBusinessOrgName(String businessOrgName) {
        this.businessOrgName = businessOrgName;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuClass() {
        return skuClass;
    }

    public void setSkuClass(String skuClass) {
        this.skuClass = skuClass;
    }

    public String getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
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

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public List<String> getBusinessDataIds() {
        return businessDataIds;
    }

    public void setBusinessDataIds(List<String> businessDataIds) {
        this.businessDataIds = businessDataIds;
    }

    public List<BmsBusinessSkuParams> getSkuParams() {
        return skuParams;
    }

    public void setSkuParams(List<BmsBusinessSkuParams> skuParams) {
        this.skuParams = skuParams;
    }

    public BigDecimal getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(BigDecimal outputValue) {
        this.outputValue = outputValue;
    }
}
