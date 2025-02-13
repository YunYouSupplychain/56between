package com.yunyou.modules.bms.finance.entity.extend;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 费用计算业务参数
 */
public class BmsCalcBusinessParams {

    // 业务订单号
    private String orderNo;
    // 客户订单号
    private String customerNo;
    // 业务时间
    private Date businessDate;
    // 供应商编码
    private String supplierCode;
    // 供应商名称
    private String supplierName;
    // 收货方编码
    private String consigneeCode;
    // 收货方名称
    private String consigneeName;
    // 发货方编码
    private String shipCode;
    // 发货方名称
    private String shipName;
    // 承运商编码
    private String carrierCode;
    // 承运商名称
    private String carrierName;
    // 货主编码
    private String ownerCode;
    // 货主名称
    private String ownerName;
    // 品类
    private String skuClass;
    // 商品编码
    private String skuCode;
    // 商品名称
    private String skuName;
    // 车型编码
    private String carTypeCode;
    // 车牌号
    private String vehicleNo;
    // 驾驶员
    private String driverCode;
    // 起点编码
    private String startPlaceCode;
    // 起点名称
    private String startPlaceName;
    // 终点编码
    private String endPlaceCode;
    // 终点名称
    private String endPlaceName;
    // 区域编码
    private String regionCode;
    // 区域编码
    private String regionName;
    // 检疫证类型
    private String quarantineType;
    // 计费条款输出对象值
    private BigDecimal outputValue;
    // 计费条款发生量值
    private BigDecimal occurrenceQty;
    // 费用计算商品参数
    private List<BmsCalcSkuParams> skuParams;
    // 业务数据机构ID
    private String orgId;
    // 业务数据机构编码
    private String orgCode;
    // 业务数据机构名称
    private String orgName;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

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

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuClass() {
        return skuClass;
    }

    public void setSkuClass(String skuClass) {
        this.skuClass = skuClass;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getStartPlaceCode() {
        return startPlaceCode;
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
        return endPlaceCode;
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

    public String getRegionCode() {
        return regionCode;
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

    public String getQuarantineType() {
        return quarantineType;
    }

    public void setQuarantineType(String quarantineType) {
        this.quarantineType = quarantineType;
    }

    public BigDecimal getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(BigDecimal outputValue) {
        this.outputValue = outputValue;
    }

    public BigDecimal getOccurrenceQty() {
        return occurrenceQty;
    }

    public void setOccurrenceQty(BigDecimal occurrenceQty) {
        this.occurrenceQty = occurrenceQty;
    }

    public List<BmsCalcSkuParams> getSkuParams() {
        return skuParams;
    }

    public void setSkuParams(List<BmsCalcSkuParams> skuParams) {
        this.skuParams = skuParams;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
