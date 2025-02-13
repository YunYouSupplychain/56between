package com.yunyou.modules.wms.report.entity;

import java.util.Date;

/**
 * 验收单报表entity
 * @Author ZYF
 * @version 2020/05/29
 */
public class WmCheckReceiveOrderLabel {
    private String asnNo;
    private String orderNo;
    private String supplierCode;
    private String supplierName;
    private String lineNo;
    private String skuCode;
    private String skuName;
    private String ownerCode;
    private Double qtyPlanEa;
    private Double qtyRcvEa;
    private Double uomQty;
    private Double qtyRcv;
    private Date lotAtt01;

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public Double getQtyPlanEa() {
        return qtyPlanEa;
    }

    public void setQtyPlanEa(Double qtyPlanEa) {
        this.qtyPlanEa = qtyPlanEa;
    }

    public Double getQtyRcvEa() {
        return qtyRcvEa;
    }

    public void setQtyRcvEa(Double qtyRcvEa) {
        this.qtyRcvEa = qtyRcvEa;
    }

    public Double getUomQty() {
        return uomQty;
    }

    public void setUomQty(Double uomQty) {
        this.uomQty = uomQty;
    }

    public Double getQtyRcv() {
        return qtyRcv;
    }

    public void setQtyRcv(Double qtyRcv) {
        this.qtyRcv = qtyRcv;
    }

    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }
}
