package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 运输订单路由信息Entity
 *
 * @author liujianhua
 * @version 2020-03-04
 */
public class TmTransportOrderRoute extends DataEntity<TmTransportOrderRoute> {

    private static final long serialVersionUID = 1L;
    // 运输单号
    private String transportNo;
    // 标签号
    private String labelNo;
    // 派车单号
    private String dispatchNo;
    // 当前网点编码
    private String nowOutletCode;
    // 上一站网点编码
    private String preOutletCode;
    // 下一站网点编码
    private String nextOutletCode;
    // 基础数据机构ID
    private String baseOrgId;
    // 预配载派车单号，派车单配载订单时产生，网点收货后清除
    private String preAllocDispatchNo;
    // 货主编码
    private String ownerCode;
    // 商品编码
    private String skuCode;

    public TmTransportOrderRoute() {
        super();
    }

    public TmTransportOrderRoute(String id) {
        super(id);
    }

    public TmTransportOrderRoute(String transportNo, String baseOrgId) {
        this.transportNo = transportNo;
        this.baseOrgId = baseOrgId;
    }

    public TmTransportOrderRoute(String transportNo, String labelNo, String baseOrgId) {
        this.transportNo = transportNo;
        this.labelNo = labelNo;
        this.baseOrgId = baseOrgId;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getNowOutletCode() {
        return nowOutletCode;
    }

    public void setNowOutletCode(String nowOutletCode) {
        this.nowOutletCode = nowOutletCode;
    }

    public String getPreOutletCode() {
        return preOutletCode;
    }

    public void setPreOutletCode(String preOutletCode) {
        this.preOutletCode = preOutletCode;
    }

    public String getNextOutletCode() {
        return nextOutletCode;
    }

    public void setNextOutletCode(String nextOutletCode) {
        this.nextOutletCode = nextOutletCode;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getPreAllocDispatchNo() {
        return preAllocDispatchNo;
    }

    public void setPreAllocDispatchNo(String preAllocDispatchNo) {
        this.preAllocDispatchNo = preAllocDispatchNo;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
}