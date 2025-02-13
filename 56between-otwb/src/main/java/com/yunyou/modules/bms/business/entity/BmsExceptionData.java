package com.yunyou.modules.bms.business.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 描述：异常数据
 */
public class BmsExceptionData extends DataEntity<BmsExceptionData> {

    private static final long serialVersionUID = 1L;
    @ExcelField(title = "异常单号")
    private String orderNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "订单日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date orderDate;
    @ExcelField(title = "派车单号")
    private String dispatchNo;
    @ExcelField(title = "委托方编码")
    private String principalCode;
    @ExcelField(title = "委托方名称")
    private String principalName;
    @ExcelField(title = "客户编码")
    private String customerCode;
    @ExcelField(title = "客户名称")
    private String customerName;
    @ExcelField(title = "承运商编码")
    private String carrierCode;
    @ExcelField(title = "承运商名称")
    private String carrierName;
    @ExcelField(title = "车型", dictType = "TMS_CAR_TYPE")
    private String carType;
    @ExcelField(title = "车牌号")
    private String vehicleNo;
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
    @ExcelField(title = "异常数量")
    private Double exceptionQty;
    @ExcelField(title = "异常箱数")
    private Double exceptionQtyCs;
    @ExcelField(title = "异常托数")
    private Double exceptionQtyPl;
    @ExcelField(title = "重量")
    private Double weight;
    @ExcelField(title = "体积")
    private Double volume;
    @ExcelField(title = "异常类型", dictType = "BMS_EXCEPTION_TYPE")
    private String exceptionType;
    @ExcelField(title = "异常原因")
    private String exceptionReason;
    @ExcelField(title = "责任方类型")
    private String responsibility;
    @ExcelField(title = "责任方", dictType = "BMS_RESPONSIBILITY")
    private String responsiblePerson;
    // 机构ID
    private String orgId;
    // 数据来源
    private String dataSources;
    // 是否参与计费
    private String isFee;

    // 订单号A(车辆门店单号)
    private String orderNoA;
    // 订单号B(母单号)
    private String orderNoB;
    // 订单号C(子单号)
    private String orderNoC;
    // 登记时间
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    private Date registrationTime;
    // 登记人
    private String registrant;
    // 项目编码
    private String projectCode;
    // 项目名称
    private String projectName;

    public BmsExceptionData() {
        super();
    }

    public BmsExceptionData(String id) {
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

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
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

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Double getExceptionQty() {
        return exceptionQty;
    }

    public void setExceptionQty(Double exceptionQty) {
        this.exceptionQty = exceptionQty;
    }

    public Double getExceptionQtyCs() {
        return exceptionQtyCs;
    }

    public void setExceptionQtyCs(Double exceptionQtyCs) {
        this.exceptionQtyCs = exceptionQtyCs;
    }

    public Double getExceptionQtyPl() {
        return exceptionQtyPl;
    }

    public void setExceptionQtyPl(Double exceptionQtyPl) {
        this.exceptionQtyPl = exceptionQtyPl;
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

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }

    public void setExceptionReason(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
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

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getRegistrant() {
        return registrant;
    }

    public void setRegistrant(String registrant) {
        this.registrant = registrant;
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