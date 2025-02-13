package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmLabelLog;

import java.util.Date;

/**
 * 标签履历Entity
 *
 * @author 刘剑华
 * @since 2023/11/16 13:58
 */
public class TmLabelLogEntity extends TmLabelLog {

    private Date opTimeFm;
    private Date opTimeTo;

    private String customerNo;// 客户单号
    private String shipCode;// 发货方编码
    private String shipName;// 发货方名称
    private String shipCityName;// 发货城市名称
    private String shipper;// 发货人
    private String shipperTel;// 发货人联系电话
    private String shipAddress;// 发货地址
    private String consigneeCode;// 收货方编码
    private String consigneeName;// 收货方名称
    private String consignee;// 收货人
    private String consigneeTel;// 收货人联系电话
    private String consigneeCityName;// 收货城市
    private String consigneeAddress;// 收货地址
    private String ownerName;// 货主名称
    private String skuName;// 商品名称
    private String carrierCode;// 承运商编码
    private String carrierName;// 承运商名称
    private String vehicleNo;// 车牌号
    private String preOutletName;// 上一个网点名称
    private String nowOutletName;// 当前网点名称
    private String nextOutletName;// 下一个网点名称
    private String orgName;// 机构名称

    public Date getOpTimeFm() {
        return opTimeFm;
    }

    public void setOpTimeFm(Date opTimeFm) {
        this.opTimeFm = opTimeFm;
    }

    public Date getOpTimeTo() {
        return opTimeTo;
    }

    public void setOpTimeTo(Date opTimeTo) {
        this.opTimeTo = opTimeTo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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

    public String getConsigneeCityName() {
        return consigneeCityName;
    }

    public void setConsigneeCityName(String consigneeCityName) {
        this.consigneeCityName = consigneeCityName;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
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

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
