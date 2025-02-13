package com.yunyou.modules.bms.business.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 描述：运单数据
 */
public class BmsWaybillData extends DataEntity<BmsWaybillData> {

    private static final long serialVersionUID = 1L;
    @ExcelField(title = "运输单号")
    private String orderNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "订单日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date orderDate;
    @ExcelField(title = "运输方式", dictType = "TMS_TRANSPORT_METHOD")
    private String transportMethod;
    @ExcelField(title = "承运商编码")
    private String carrierCode;
    @ExcelField(title = "承运商名称")
    private String carrierName;
    @ExcelField(title = "车型", dictType = "TMS_CAR_TYPE")
    private String carType;
    @ExcelField(title = "委托方编码")
    private String principalCode;
    @ExcelField(title = "委托方名称")
    private String principalName;
    @ExcelField(title = "发货方编码")
    private String shipCode;
    @ExcelField(title = "发货方名称")
    private String shipName;
    @ExcelField(title = "发货城市ID")
    private String shipCityId;
    @ExcelField(title = "发货城市名称")
    private String shipCityName;
    // 起始地
    private String origin;
    @ExcelField(title = "收货方编号")
    private String consigneeCode;
    @ExcelField(title = "收货方名称")
    private String consigneeName;
    @ExcelField(title = "目的地城市ID")
    private String consigneeCityId;
    @ExcelField(title = "目的地城市名称")
    private String consigneeCityName;
    // 目的地
    private String destination;
    @ExcelField(title = "货主编码")
    private String ownerCode;
    @ExcelField(title = "货主名称")
    private String ownerName;
    @ExcelField(title = "供应商编码")
    private String supplierCode;
    @ExcelField(title = "供应商名称")
    private String supplierName;
    @ExcelField(title = "商品编码")
    private String skuCode;
    @ExcelField(title = "商品名称")
    private String skuName;
    @ExcelField(title = "品类")
    private String skuClass;
    @ExcelField(title = "数量")
    private Double qty;
    @ExcelField(title = "箱数")
    private Double qtyCs;
    @ExcelField(title = "托数")
    private Double qtyPl;
    @ExcelField(title = "重量")
    private Double weight;
    @ExcelField(title = "体积")
    private Double volume;
    // 机构ID
    private String orgId;
    // 数据来源
    private String dataSources;
    // 是否参与计费
    private String isFee;

    public BmsWaybillData() {
        super();
    }

    public BmsWaybillData(String id) {
        super(id);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getTransportMethod() {
        return transportMethod;
    }

    public void setTransportMethod(String transportMethod) {
        this.transportMethod = transportMethod;
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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPrincipalCode() {
        return principalCode;
    }

    public void setPrincipalCode(String principalCode) {
        this.principalCode = principalCode;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
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

    public String getShipCityId() {
        return shipCityId;
    }

    public void setShipCityId(String shipCityId) {
        this.shipCityId = shipCityId;
    }

    public String getShipCityName() {
        return shipCityName;
    }

    public void setShipCityName(String shipCityName) {
        this.shipCityName = shipCityName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

    public String getConsigneeCityId() {
        return consigneeCityId;
    }

    public void setConsigneeCityId(String consigneeCityId) {
        this.consigneeCityId = consigneeCityId;
    }

    public String getConsigneeCityName() {
        return consigneeCityName;
    }

    public void setConsigneeCityName(String consigneeCityName) {
        this.consigneeCityName = consigneeCityName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public String getSkuClass() {
        return skuClass;
    }

    public void setSkuClass(String skuClass) {
        this.skuClass = skuClass;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getQtyCs() {
        return qtyCs;
    }

    public void setQtyCs(Double qtyCs) {
        this.qtyCs = qtyCs;
    }

    public Double getQtyPl() {
        return qtyPl;
    }

    public void setQtyPl(Double qtyPl) {
        this.qtyPl = qtyPl;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDataSources() {
        return dataSources;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }

    public String getIsFee() {
        return isFee;
    }

    public void setIsFee(String isFee) {
        this.isFee = isFee;
    }
}