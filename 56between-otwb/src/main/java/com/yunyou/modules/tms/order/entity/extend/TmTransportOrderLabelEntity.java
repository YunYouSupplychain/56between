package com.yunyou.modules.tms.order.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;

import java.util.List;

public class TmTransportOrderLabelEntity extends TmTransportOrderLabel {
    private static final long serialVersionUID = 817138864098624125L;
    private String skuName;

    // 发货方编码
    private String shipCode;
    private String shipName;// 发货方
    // 发货城市ID
    private String shipCityId;
    private String shipCity;// 发货城市
    // 发货人
    private String shipper;
    // 发货人联系电话
    private String shipperTel;
    // 发货地址
    private String shipAddress;
    // 收货方编码
    private String consigneeCode;
    private String consigneeName;// 收货方
    // 目的地城市ID
    private String consigneeCityId;
    private String consigneeCity;// 目的地城市
    // 收货人
    private String consignee;
    // 收货人联系电话
    private String consigneeTel;
    // 收货地址
    private String consigneeAddress;
    // 揽收网点编码
    private String receiveOutletCode;
    private String receiveOutletName;// 揽收网点
    // 配送网点编码
    private String outletCode;
    private String outletName;// 配送网点

    private String dispatchNo;      // 派车单号
    private String hasDispatch;     // 是否配载

    private List<String> transportIdList = Lists.newArrayList();
    private List<String> transportNoList = Lists.newArrayList();

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
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

    public String getReceiveOutletCode() {
        return receiveOutletCode;
    }

    public void setReceiveOutletCode(String receiveOutletCode) {
        this.receiveOutletCode = receiveOutletCode;
    }

    public String getReceiveOutletName() {
        return receiveOutletName;
    }

    public void setReceiveOutletName(String receiveOutletName) {
        this.receiveOutletName = receiveOutletName;
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

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getHasDispatch() {
        return hasDispatch;
    }

    public void setHasDispatch(String hasDispatch) {
        this.hasDispatch = hasDispatch;
    }

    public List<String> getTransportIdList() {
        return transportIdList;
    }

    public void setTransportIdList(List<String> transportIdList) {
        this.transportIdList = transportIdList;
    }

    public List<String> getTransportNoList() {
        return transportNoList;
    }

    public void setTransportNoList(List<String> transportNoList) {
        this.transportNoList = transportNoList;
    }
}
