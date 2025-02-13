package com.yunyou.modules.tms.order.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;

import java.util.Date;

public class TmTransportOrderRouteEntity extends TmTransportOrderRoute {
    private static final long serialVersionUID = 1303785235291627943L;

    private Date orderTimeFm;
    private Date orderTimeTo;
    private String orgId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;// 运输订单受理时间
    private String customerNo;// 客户订单号
    private String preOutletName;// 上一站网点名称
    private String nowOutletName;// 当前网点名称
    private String nextOutletName;// 下一站网点名称
    private String baseOrgName;// 基础数据机构名称(归属组织中心)
    private String shipCode;// 发货方编码
    private String shipName;// 发货方名称
    private String shipCityId;// 发货城市ID
    private String shipCity;// 发货城市(全名称)
    private String shipper;// 发货人
    private String shipperTel;// 发货人电话
    private String shipAddress;// 发货详细地址
    private String consigneeCode;// 收货方编码
    private String consigneeName;// 收货方名称
    private String consigneeCityId;// 收货城市ID
    private String consigneeCity;// 收货城市(全名称)
    private String consignee;// 收货人
    private String consigneeTel;// 收货人电话
    private String consigneeAddress;// 收货详细地址

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

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getPreOutletName() {
        return preOutletName;
    }

    public void setPreOutletName(String preOutletName) {
        this.preOutletName = preOutletName;
    }

    public String getNowOutletName() {
        return nowOutletName;
    }

    public void setNowOutletName(String nowOutletName) {
        this.nowOutletName = nowOutletName;
    }

    public String getNextOutletName() {
        return nextOutletName;
    }

    public void setNextOutletName(String nextOutletName) {
        this.nextOutletName = nextOutletName;
    }

    public String getBaseOrgName() {
        return baseOrgName;
    }

    public void setBaseOrgName(String baseOrgName) {
        this.baseOrgName = baseOrgName;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
