package com.yunyou.modules.bms.business.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 描述：派车配载数据
 */
public class BmsDispatchData extends DataEntity<BmsDispatchData> {

    private static final long serialVersionUID = 1L;
    @ExcelField(title = "派车单号")
    private String orderNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "订单日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date orderDate;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    @ExcelField(title = "派车时间")
    private Date dispatchTime;
    @ExcelField(title = "运输方式", dictType = "TMS_TRANSPORT_METHOD")
    private String transportMethod;
    @ExcelField(title = "承运商编码")
    private String carrierCode;
    @ExcelField(title = "承运商名称")
    private String carrierName;
    @ExcelField(title = "车型", dictType = "TMS_CAR_TYPE")
    private String carType;
    @ExcelField(title = "车牌号")
    private String vehicleNo;
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

    // 订单A(门店配载单号)
    private String orderNoA;
    // 订单B(车辆门店单号)
    private String orderNoB;
    // 订单C(母单号)
    private String orderNoC;
    // 订单D(子单号)
    private String orderNoD;
    // 订单E(门店调入单号)
    private String orderNoE;
    // 订单F(框号)
    private String orderNoF;
    // 订单G(栈板号)
    private String orderNoG;
    // 业务类型
    private String businessType;
    // 业务模式
    private String businessModel;
    // 是否冰品
    private String isIces;
    // 是否检疫
    private String isIqc;
    // 检疫类型
    private String quarantineType;
    // 项目编码
    private String projectCode;
    // 项目名称
    private String projectName;

    public BmsDispatchData() {
        super();
    }

    public BmsDispatchData(String id) {
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

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
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

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
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

    public String getOrderNoA() {
        return orderNoA;
    }

    public void setOrderNoA(String orderNoA) {
        this.orderNoA = orderNoA;
    }

    public String getOrderNoB() {
        return orderNoB;
    }

    public void setOrderNoB(String orderNoB) {
        this.orderNoB = orderNoB;
    }

    public String getOrderNoC() {
        return orderNoC;
    }

    public void setOrderNoC(String orderNoC) {
        this.orderNoC = orderNoC;
    }

    public String getOrderNoD() {
        return orderNoD;
    }

    public void setOrderNoD(String orderNoD) {
        this.orderNoD = orderNoD;
    }

    public String getOrderNoE() {
        return orderNoE;
    }

    public void setOrderNoE(String orderNoE) {
        this.orderNoE = orderNoE;
    }

    public String getOrderNoF() {
        return orderNoF;
    }

    public void setOrderNoF(String orderNoF) {
        this.orderNoF = orderNoF;
    }

    public String getOrderNoG() {
        return orderNoG;
    }

    public void setOrderNoG(String orderNoG) {
        this.orderNoG = orderNoG;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public String getIsIces() {
        return isIces;
    }

    public void setIsIces(String isIces) {
        this.isIces = isIces;
    }

    public String getIsIqc() {
        return isIqc;
    }

    public void setIsIqc(String isIqc) {
        this.isIqc = isIqc;
    }

    public String getQuarantineType() {
        return quarantineType;
    }

    public void setQuarantineType(String quarantineType) {
        this.quarantineType = quarantineType;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}