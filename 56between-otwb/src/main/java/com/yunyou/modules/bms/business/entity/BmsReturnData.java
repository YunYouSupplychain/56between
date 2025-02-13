package com.yunyou.modules.bms.business.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 描述：退货数据
 */
public class BmsReturnData extends DataEntity<BmsReturnData> {

    private static final long serialVersionUID = 1L;
    @ExcelField(title = "退货单号")
    private String orderNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "订单日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date orderDate;
    @ExcelField(title = "客户退货单号")
    private String customerNo;
    @ExcelField(title = "派车单号")
    private String dispatchNo;
    @ExcelField(title = "收货方编码")
    private String consigneeCode;
    @ExcelField(title = "收货方名称")
    private String consigneeName;
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
    @ExcelField(title = "退货数量")
    private Double orderQty;
    @ExcelField(title = "退货箱数")
    private Double orderQtyCs;
    @ExcelField(title = "退货托数")
    private Double orderQtyPl;
    @ExcelField(title = "实收数量")
    private Double receiptQty;
    @ExcelField(title = "实收箱数")
    private Double receiptQtyCs;
    @ExcelField(title = "实收托数")
    private Double receiptQtyPl;
    @ExcelField(title = "返厂数量")
    private Double returnedQty;
    @ExcelField(title = "返厂箱数")
    private Double returnedQtyCs;
    @ExcelField(title = "返厂托数")
    private Double returnedQtyPl;
    @ExcelField(title = "重量")
    private Double weight;
    @ExcelField(title = "体积")
    private Double volume;
    // 异常类型
    private String exceptionType;
    // 异常原因
    private String exceptionReason;
    // 机构ID
    private String orgId;
    // 数据来源
    private String dataSources;
    // 是否参与计费
    private String isFee;

    // 订单号A（车辆门店单号）
    private String orderNoA;
    // 项目编码
    private String projectCode;
    // 项目名称
    private String projectName;

    public BmsReturnData() {
        super();
    }

    public BmsReturnData(String id) {
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

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
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

    public Double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Double orderQty) {
        this.orderQty = orderQty;
    }

    public Double getOrderQtyCs() {
        return orderQtyCs;
    }

    public void setOrderQtyCs(Double orderQtyCs) {
        this.orderQtyCs = orderQtyCs;
    }

    public Double getOrderQtyPl() {
        return orderQtyPl;
    }

    public void setOrderQtyPl(Double orderQtyPl) {
        this.orderQtyPl = orderQtyPl;
    }

    public Double getReceiptQty() {
        return receiptQty;
    }

    public void setReceiptQty(Double receiptQty) {
        this.receiptQty = receiptQty;
    }

    public Double getReceiptQtyCs() {
        return receiptQtyCs;
    }

    public void setReceiptQtyCs(Double receiptQtyCs) {
        this.receiptQtyCs = receiptQtyCs;
    }

    public Double getReceiptQtyPl() {
        return receiptQtyPl;
    }

    public void setReceiptQtyPl(Double receiptQtyPl) {
        this.receiptQtyPl = receiptQtyPl;
    }

    public Double getReturnedQty() {
        return returnedQty;
    }

    public void setReturnedQty(Double returnedQty) {
        this.returnedQty = returnedQty;
    }

    public Double getReturnedQtyCs() {
        return returnedQtyCs;
    }

    public void setReturnedQtyCs(Double returnedQtyCs) {
        this.returnedQtyCs = returnedQtyCs;
    }

    public Double getReturnedQtyPl() {
        return returnedQtyPl;
    }

    public void setReturnedQtyPl(Double returnedQtyPl) {
        this.returnedQtyPl = returnedQtyPl;
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