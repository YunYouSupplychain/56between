package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmDispatchOrderLabel;

public class TmDispatchOrderLabelEntity extends TmDispatchOrderLabel {
    private static final long serialVersionUID = -4691767869024299374L;

    private String principalCode;        // 委托方编码
    private String principalName;        // 委托方名称
    private String customerCode;        // 客户编码
    private String customerName;        // 客户名称
    private String isAppInput;        // 是否App录入
    private String nowOutletCode;        // 当前网点编码
    private String nextOutletCode;       // 下一网点编码
    private String nowOutletName;        // 当前网点名称
    private String nextOutletName;       // 下一网点名称
    private String shipCode;        // 发货方编码
    private String shipName;        // 发货方名称
    private String shipper;              // 发货人
    private String shipperTel;           // 发货人电话
    private String shipAddress;          // 发货地址
    private String consigneeCode;        // 收货方编码
    private String consigneeName;        // 收货方名称
    private String consignee;            // 收货人
    private String consigneeTel;         // 收货人电话
    private String consigneeAddress;      // 收货地址
    private String skuName;             // 商品名称
    private String carNo;

    public TmDispatchOrderLabelEntity() {
        super();
    }

    public TmDispatchOrderLabelEntity(String dispatchNo, String orgId) {
        super(dispatchNo, orgId);
    }

    public TmDispatchOrderLabelEntity(String dispatchNo, String labelNo, String orgId) {
        super(dispatchNo, labelNo, orgId);
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

    public String getIsAppInput() {
        return isAppInput;
    }

    public void setIsAppInput(String isAppInput) {
        this.isAppInput = isAppInput;
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

    public String getNowOutletCode() {
        return nowOutletCode;
    }

    public void setNowOutletCode(String nowOutletCode) {
        this.nowOutletCode = nowOutletCode;
    }

    public String getNextOutletCode() {
        return nextOutletCode;
    }

    public void setNextOutletCode(String nextOutletCode) {
        this.nextOutletCode = nextOutletCode;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
