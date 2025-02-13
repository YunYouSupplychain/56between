package com.yunyou.modules.tms.order.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderHeader;

import java.util.Date;
import java.util.List;

public class TmPreTransportOrderEntity extends TmPreTransportOrderHeader {
    private static final long serialVersionUID = 2519128372293958458L;

    /*查询条件*/
    private Date orderTimeFm;
    private Date orderTimeTo;
    private String isToReceive;// 是否待收货
    private String isToSign;// 是否待签收

    private TmPreTransportOrderDeliveryEntity orderDelivery = new TmPreTransportOrderDeliveryEntity(); // 订单配送信息
    private List<TmPreTransportOrderSkuEntity> tmTransportOrderSkuList = Lists.newArrayList(); // 订单明细信息
    private List<TmPreTransportOrderCostEntity> tmTransportOrderCostList = Lists.newArrayList(); // 费用明细信息

    /*额外字段*/
    private String orgName;// 机构名称
    private String receiveOutletName;// 揽收网点
    private String principalName;// 委托方
    private String customerName;// 客户
    private String outletName;// 配送网点
    private String shipName;// 发货方
    private String shipCity;// 发货城市
    private String consigneeName;// 收货方
    private String consigneeCity;// 目的地城市
    private long toAuditOrderQty = 0L;// 待审核订单数
    private long toReceiveOrderQty = 0L;// 待收货订单数
    private long toSignOrderQty = 0L;// 待签收订单数
    private long signedOrderQty = 0L;// 已签收订单数
    private String curOrgId;// 当前操作机构ID
    private String hasDispatch; // 是否已配载

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

    public String getIsToReceive() {
        return isToReceive;
    }

    public void setIsToReceive(String isToReceive) {
        this.isToReceive = isToReceive;
    }

    public String getIsToSign() {
        return isToSign;
    }

    public void setIsToSign(String isToSign) {
        this.isToSign = isToSign;
    }

    public TmPreTransportOrderDeliveryEntity getOrderDelivery() {
        return orderDelivery;
    }

    public void setOrderDelivery(TmPreTransportOrderDeliveryEntity orderDelivery) {
        this.orderDelivery = orderDelivery;
    }

    public List<TmPreTransportOrderSkuEntity> getTmTransportOrderSkuList() {
        return tmTransportOrderSkuList;
    }

    public void setTmTransportOrderSkuList(List<TmPreTransportOrderSkuEntity> tmTransportOrderSkuList) {
        this.tmTransportOrderSkuList = tmTransportOrderSkuList;
    }

    public List<TmPreTransportOrderCostEntity> getTmTransportOrderCostList() {
        return tmTransportOrderCostList;
    }

    public void setTmTransportOrderCostList(List<TmPreTransportOrderCostEntity> tmTransportOrderCostList) {
        this.tmTransportOrderCostList = tmTransportOrderCostList;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getReceiveOutletName() {
        return receiveOutletName;
    }

    public void setReceiveOutletName(String receiveOutletName) {
        this.receiveOutletName = receiveOutletName;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }

    public long getToAuditOrderQty() {
        return toAuditOrderQty;
    }

    public void setToAuditOrderQty(long toAuditOrderQty) {
        this.toAuditOrderQty = toAuditOrderQty;
    }

    public long getToReceiveOrderQty() {
        return toReceiveOrderQty;
    }

    public void setToReceiveOrderQty(long toReceiveOrderQty) {
        this.toReceiveOrderQty = toReceiveOrderQty;
    }

    public long getToSignOrderQty() {
        return toSignOrderQty;
    }

    public void setToSignOrderQty(long toSignOrderQty) {
        this.toSignOrderQty = toSignOrderQty;
    }

    public long getSignedOrderQty() {
        return signedOrderQty;
    }

    public void setSignedOrderQty(long signedOrderQty) {
        this.signedOrderQty = signedOrderQty;
    }

    public String getCurOrgId() {
        return curOrgId;
    }

    public void setCurOrgId(String curOrgId) {
        this.curOrgId = curOrgId;
    }

    public String getHasDispatch() {
        return hasDispatch;
    }

    public void setHasDispatch(String hasDispatch) {
        this.hasDispatch = hasDispatch;
    }
}
