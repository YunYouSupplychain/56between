package com.yunyou.modules.wms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 出库确认交接单报表entity
 * @Author ZYF
 * @version 2021/05/20
 */
public class ShipHandoverOrder {
    private String soNo;
    private String customerNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shipDate;
    private String ownerCode;
    private String ownerName;
    private String consigneeName;
    private String consigneeAddr;
    private String contactName;
    private String contactTel;
    private String remarks;
    private String lineNo;
    private String skuCode;
    private String skuName;
    private String skuSpec;
    private String uom;
    private Double qtyShipUom;
    private Double qtyShipEa;
    private String skuBarCodo;

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeAddr() {
        return consigneeAddr;
    }

    public void setConsigneeAddr(String consigneeAddr) {
        this.consigneeAddr = consigneeAddr;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQtyShipUom() {
        return qtyShipUom;
    }

    public void setQtyShipUom(Double qtyShipUom) {
        this.qtyShipUom = qtyShipUom;
    }

    public Double getQtyShipEa() {
        return qtyShipEa;
    }

    public void setQtyShipEa(Double qtyShipEa) {
        this.qtyShipEa = qtyShipEa;
    }

    public String getSkuBarCodo() {
        return skuBarCodo;
    }

    public void setSkuBarCodo(String skuBarCodo) {
        this.skuBarCodo = skuBarCodo;
    }
}
