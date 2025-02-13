package com.yunyou.modules.tms.order.entity.extend;

import java.util.List;

import com.yunyou.modules.tms.order.entity.TmReceiveLabel;

public class TmReceiveEntity extends TmReceiveLabel {
    private static final long serialVersionUID = 2519128372293958458L;

    /*查询条件*/
    private String shipCode;        // 发货人单位
    private String shipName;        // 发货单位名称
    private String shipper;         // 发货人
    private String shipperTel;      // 发货人电话
    private String shipCityId;      // 发货城市ID
    private String shipCity;        // 发货城市
    private String shipAddress;     // 发货人地址
    private String consigneeCode;   // 收货人单位
    private String consigneeName;   // 收货单位名称
    private String consignee;       // 收货人
    private String consigneeTel;    // 收货人电话
    private String consigneeCityId; // 目的城市ID
    private String consigneeCity;   // 目的城市
    private String consigneeAddress;// 收货人地址
    private String preOutletCode;   // 上一个网点
    private String preOutletName;   // 上一个网点
    private String nowOutletCode;   // 当前网点编码
    private String nowOutletName;   // 当前网点名称
    private String nextOutletCode;  // 下一个网点
    private String nextOutletName;  // 下一个网点

    private List<String> receiveOutletCodes;// 当前操作机构对应的收货网点

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

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getPreOutletCode() {
        return preOutletCode;
    }

    public void setPreOutletCode(String preOutletCode) {
        this.preOutletCode = preOutletCode;
    }

    public String getPreOutletName() {
        return preOutletName;
    }

    public void setPreOutletName(String preOutletName) {
        this.preOutletName = preOutletName;
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

    public List<String> getReceiveOutletCodes() {
        return receiveOutletCodes;
    }

    public void setReceiveOutletCodes(List<String> receiveOutletCodes) {
        this.receiveOutletCodes = receiveOutletCodes;
    }
}
