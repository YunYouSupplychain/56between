package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 运输订单基本信息Entity
 *
 * @author liujianhua
 * @version 2020-03-04
 */
public class TmPreTransportOrderHeader extends DataEntity<TmPreTransportOrderHeader> {

    private static final long serialVersionUID = 1L;
    // 运输单号
    private String transportNo;
    // 订单时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;
    // 订单类型
    private String orderType;
    // 订单状态
    private String orderStatus;
    // 客户单号
    private String customerNo;
    // 物流单号
    private String trackingNo;
    // 委托方编码
    private String principalCode;
    // 客户编码
    private String customerCode;
    // 品牌
    private String brand;
    // 揽收网点编码
    private String receiveOutletCode;
    // 运输方式
    private String transportMethod;
    // 交货方式
    private String deliveryMethod;
    // 发货方编码
    private String shipCode;
    // 发货城市ID
    private String shipCityId;
    // 发货城市名称(供第三方录入使用)
    private String shipCityName;
    // 发货人
    private String shipper;
    // 发货人联系电话
    private String shipperTel;
    // 发货地址
    private String shipAddress;
    // 收货方编码
    private String consigneeCode;
    // 目的地城市ID
    private String consigneeCityId;
    // 目的地城市名称(供第三方录入使用)
    private String consigneeCityName;
    // 收货人
    private String consignee;
    // 收货人联系电话
    private String consigneeTel;
    // 收货地址
    private String consigneeAddress;
    // 目的地城市配送网点编码
    private String outletCode;
    // 指定签收人
    private String designatedSignBy;
    // 机构ID
    private String orgId;
    // 基础数据机构ID
    private String baseOrgId;
    // 调度计划单号
    private String dispatchPlanNo;
    // 数据来源
    private String dataSource;
    // 来源单号
    private String sourceNo;
    // 自定义
    private String def1; // 子单号
    private String def2; // 客户大类
    private String def3; // 区域
    private String def4; // 城市
    private String def5; // 客户行业类型
    private String def6; // 客户范围
    private String def7;
    private String def8;
    private String def9; // 业务订单类型
    private String def10;

    // 订单来源
    private String orderSource;
    // 渠道
    private String channel;
    // 服务模式
    private String serviceMode;
    // 供应商
    private String supplierCode;
    // 采购单号
    private String poNo;

    public TmPreTransportOrderHeader() {
    }

    public TmPreTransportOrderHeader(String id) {
        super(id);
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getPrincipalCode() {
        return principalCode;
    }

    public void setPrincipalCode(String principalCode) {
        this.principalCode = principalCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getReceiveOutletCode() {
        return receiveOutletCode;
    }

    public void setReceiveOutletCode(String receiveOutletCode) {
        this.receiveOutletCode = receiveOutletCode;
    }

    public String getTransportMethod() {
        return transportMethod;
    }

    public void setTransportMethod(String transportMethod) {
        this.transportMethod = transportMethod;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
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

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperTel() {
        return shipperTel;
    }

    public void setShipperTel(String shipperTel) {
        this.shipperTel = shipperTel;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
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

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getDesignatedSignBy() {
        return designatedSignBy;
    }

    public void setDesignatedSignBy(String designatedSignBy) {
        this.designatedSignBy = designatedSignBy;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getDispatchPlanNo() {
        return dispatchPlanNo;
    }

    public void setDispatchPlanNo(String dispatchPlanNo) {
        this.dispatchPlanNo = dispatchPlanNo;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getSourceNo() {
        return sourceNo;
    }

    public void setSourceNo(String sourceNo) {
        this.sourceNo = sourceNo;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef10() {
        return def10;
    }

    public void setDef10(String def10) {
        this.def10 = def10;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }
}