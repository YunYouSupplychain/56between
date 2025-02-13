package com.yunyou.modules.bms.business.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 描述：入库数据
 */
public class BmsInboundData extends DataEntity<BmsInboundData> {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "入库单号")
    private String orderNo;
    @ExcelField(title = "订单类型", dictType = "BMS_INBOUND_TYPE")
    private String orderType;
    @ExcelField(title = "业务类型", dictType = "OMS_BUSINESS_ORDER_TYPE")
    private String businessType;
    @ExcelField(title = "库存模式", dictType = "BMS_BUSINESS_TYPE")
    private String businessModel;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "订单日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date orderDate;
    @ExcelField(title = "采购订单号")
    private String poNo;
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
    @ExcelField(title = "批次号")
    private String lotNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "生产日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date lotAtt01;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "失效日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date lotAtt02;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "入库日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date lotAtt03;
    @ExcelField(title = "批次属性04")
    private String lotAtt04;
    @ExcelField(title = "批次属性05")
    private String lotAtt05;
    @ExcelField(title = "批次属性06")
    private String lotAtt06;
    @ExcelField(title = "批次属性07")
    private String lotAtt07;
    @ExcelField(title = "批次属性08")
    private String lotAtt08;
    @ExcelField(title = "批次属性09")
    private String lotAtt09;
    @ExcelField(title = "批次属性10")
    private String lotAtt10;
    @ExcelField(title = "批次属性11")
    private String lotAtt11;
    @ExcelField(title = "批次属性12")
    private String lotAtt12;
    @ExcelField(title = "订单数量")
    private Double orderQty;
    @ExcelField(title = "订单箱数")
    private Double orderQtyCs;
    @ExcelField(title = "订单托数")
    private Double orderQtyPl;
    @ExcelField(title = "实收数量")
    private Double receiptQty;
    @ExcelField(title = "实收箱数")
    private Double receiptQtyCs;
    @ExcelField(title = "实收托数")
    private Double receiptQtyPl;
    @ExcelField(title = "重量")
    private Double weight;
    @ExcelField(title = "体积")
    private Double volume;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
    @ExcelField(title = "收货时间")
    private Date receiveTime;
    @ExcelField(title = "收货库位编码")
    private String receiveCode;
    @ExcelField(title = "托盘跟踪号")
    private String traceId;
    // 数据来源
    private String dataSources;
    // 是否参与计费
    private String isFee;
    // 机构ID
    private String orgId;

    // 订单号A
    private String orderNoA;
    // 订单号B
    private String orderNoB;
    // 订单号C
    private String orderNoC;

    public BmsInboundData() {
        super();
    }

    public BmsInboundData(String id) {
        super(id);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
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

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public Date getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(Date lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    public Date getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(Date lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    public String getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    public String getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    public String getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    public String getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    public String getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    public String getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    public String getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    public String getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    public String getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
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

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(String receiveCode) {
        this.receiveCode = receiveCode;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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