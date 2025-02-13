package com.yunyou.modules.interfaces.edi.entity;

import java.util.List;

public class EdiOrderRequest {

    private String ediType;
    private List<EdiOrderInfo> orderList;

    public EdiOrderRequest() {
    }

    public EdiOrderRequest(String ediType, List<EdiOrderInfo> orderList) {
        this.ediType = ediType;
        this.orderList = orderList;
    }

    public String getEdiType() {
        return ediType;
    }

    public void setEdiType(String ediType) {
        this.ediType = ediType;
    }

    public List<EdiOrderInfo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<EdiOrderInfo> orderList) {
        this.orderList = orderList;
    }
}