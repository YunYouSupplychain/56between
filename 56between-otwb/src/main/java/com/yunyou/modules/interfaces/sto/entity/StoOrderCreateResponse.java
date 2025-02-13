package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;

/**
 * 订单创建接口Response
 */
public class StoOrderCreateResponse implements Serializable {
    private static final long serialVersionUID = 5006089805748732285L;
    // 订单号
    private String orderNo;
    // 运单号
    private String waybillNo;
    // 大字/三段码
    private String bigWord;
    // 集包地
    private String packagePlace;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getBigWord() {
        return bigWord;
    }

    public void setBigWord(String bigWord) {
        this.bigWord = bigWord;
    }

    public String getPackagePlace() {
        return packagePlace;
    }

    public void setPackagePlace(String packagePlace) {
        this.packagePlace = packagePlace;
    }
}
