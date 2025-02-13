package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmDispatchPlanDetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TmDispatchPlanDetailEntity extends TmDispatchPlanDetail {
    private static final long serialVersionUID = 1L;
    // 客户名称
    private String ownerName;
    // 商品名称
    private String skuName;
    // 商品类型
    private String skuType;
    private String skuTypeDesc;
    // 路线
    private String lineCode;
    private String lineName;
    // 分类
    private String classification;
    // 调度数量
    private BigDecimal dispatchQty;
    // 未调度量
    private BigDecimal unDispatchQty;
    // 客户编码集合
    private List<String> customerCodeList;
    // 开始送到时间
    private Date arrivalTimeFm;
    // 结束送达时间
    private Date arrivalTimeTo;
    // 是否客户查询
    private String isOwner;
    // 是否商品查询
    private String isSku;

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

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public String getSkuTypeDesc() {
        return skuTypeDesc;
    }

    public void setSkuTypeDesc(String skuTypeDesc) {
        this.skuTypeDesc = skuTypeDesc;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public BigDecimal getUnDispatchQty() {
        return unDispatchQty;
    }

    public void setUnDispatchQty(BigDecimal unDispatchQty) {
        this.unDispatchQty = unDispatchQty;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public BigDecimal getDispatchQty() {
        return dispatchQty;
    }

    public void setDispatchQty(BigDecimal dispatchQty) {
        this.dispatchQty = dispatchQty;
    }

    public List<String> getCustomerCodeList() {
        return customerCodeList;
    }

    public void setCustomerCodeList(List<String> customerCodeList) {
        this.customerCodeList = customerCodeList;
    }

    public Date getArrivalTimeFm() {
        return arrivalTimeFm;
    }

    public void setArrivalTimeFm(Date arrivalTimeFm) {
        this.arrivalTimeFm = arrivalTimeFm;
    }

    public Date getArrivalTimeTo() {
        return arrivalTimeTo;
    }

    public void setArrivalTimeTo(Date arrivalTimeTo) {
        this.arrivalTimeTo = arrivalTimeTo;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getIsSku() {
        return isSku;
    }

    public void setIsSku(String isSku) {
        this.isSku = isSku;
    }
}
