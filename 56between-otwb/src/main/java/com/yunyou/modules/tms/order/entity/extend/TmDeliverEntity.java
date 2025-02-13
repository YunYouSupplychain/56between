package com.yunyou.modules.tms.order.entity.extend;

import java.util.Date;
import java.util.List;

import com.yunyou.core.persistence.DataEntity;

public class TmDeliverEntity extends DataEntity<TmDeliverEntity> {
    private static final long serialVersionUID = 3454587278059280864L;

    private String transportNo;     // 运输单号
    private String customerNo;      // 客户单号
    private String dispatchNo;      // 派车单号
    private String labelNo;         // 标签号
    private String outletCode;      // 货物所在网点编码
    private String outletName;      // 货物所在网点名称
    private String nowOutletCode;   // 实际发货网点编码
    private String nowOutletName;   // 实际发货网点名称
    private String nextOutletCode;  // 派送网点编码
    private String nextOutletName;  // 派送网点名称
    private String shipCode;        // 发货方编码
    private String shipName;        // 发货方名称
    private String shipCityId;      // 发货城市ID
    private String shipCity;        // 发货城市
    private String shipper;         // 发货人
    private String shipperTel;      // 发货人联系电话
    private String shipAddress;     // 发货地址
    private String consigneeCityId; // 目的地城市ID
    private String consigneeCity;   // 目的地城市
    private String consigneeCode;   // 收货方编码
    private String consigneeName;   // 收货方名称
    private String consignee;       // 收货人
    private String consigneeTel;    // 收货人联系电话
    private String consigneeAddress;// 收货地址
    private String baseOrgId;       // 基础数据机构ID
    private String orgId;           // 机构ID

    private Date orderTimeFm;       // 运输订单时间从
    private Date orderTimeTo;       // 运输订单时间到
    private String customerCode;    // 客户编码
    private String preAllocDispatchNo;// 预配载派车单号
    private List<String> shipOutletCodes;// 当前操作机构对应的发货网点

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
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

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getNowOutletCode() {
        return nowOutletCode;
    }

    public void setNowOutletCode(String nowOutletCode) {
        this.nowOutletCode = nowOutletCode;
    }

    public String getNowOutletName() {
        return nowOutletName;
    }

    public void setNowOutletName(String nowOutletName) {
        this.nowOutletName = nowOutletName;
    }

    public String getNextOutletCode() {
        return nextOutletCode;
    }

    public void setNextOutletCode(String nextOutletCode) {
        this.nextOutletCode = nextOutletCode;
    }

    public String getNextOutletName() {
        return nextOutletName;
    }

    public void setNextOutletName(String nextOutletName) {
        this.nextOutletName = nextOutletName;
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

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
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

    public String getConsigneeCityId() {
        return consigneeCityId;
    }

    public void setConsigneeCityId(String consigneeCityId) {
        this.consigneeCityId = consigneeCityId;
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
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

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Date getOrderTimeFm() {
        return orderTimeFm;
    }

    public void setOrderTimeFm(Date orderTimeFm) {
        this.orderTimeFm = orderTimeFm;
    }

    public Date getOrderTimeTo() {
        return orderTimeTo;
    }

    public void setOrderTimeTo(Date orderTimeTo) {
        this.orderTimeTo = orderTimeTo;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getPreAllocDispatchNo() {
        return preAllocDispatchNo;
    }

    public void setPreAllocDispatchNo(String preAllocDispatchNo) {
        this.preAllocDispatchNo = preAllocDispatchNo;
    }

    public List<String> getShipOutletCodes() {
        return shipOutletCodes;
    }

    public void setShipOutletCodes(List<String> shipOutletCodes) {
        this.shipOutletCodes = shipOutletCodes;
    }
}
