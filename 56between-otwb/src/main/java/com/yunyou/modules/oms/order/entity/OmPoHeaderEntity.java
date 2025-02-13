package com.yunyou.modules.oms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 采购订单Entity
 * @author WMJ
 * @version 2019-04-22
 */
public class OmPoHeaderEntity extends OmPoHeader {
    private static final long serialVersionUID = 1750567982387468537L;

    private String ownerName;       // 货主名称
    private String supplierName;	// 供应商名称
	private String carrierName;		// 承运商名称
	private String settlementName;	// 结算对象名称
    private String projectName;     // 项目名称
	private String orgName;		    // 机构名称
	private String departName;		// 部门名称
    private String consigneeAreaName;// 收货人区域名称
    private String shipperAreaName; // 发货人区域名称
    private Double taxRate;         // 供应商-税率
    private String subOrgName;      // 下发机构名称
    private String principalName;   // 委托方名称
    /**
     * 查询条件
     */
    private Date orderDateFm;
    private Date orderDateTo;
    private Date arrivalTimeFm;
    private Date arrivalTimeTo;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getOrderDateFm() {
        return orderDateFm;
    }

    public void setOrderDateFm(Date orderDateFm) {
        this.orderDateFm = orderDateFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(Date orderDateTo) {
        this.orderDateTo = orderDateTo;
    }

    public String getConsigneeAreaName() {
        return consigneeAreaName;
    }

    public void setConsigneeAreaName(String consigneeAreaName) {
        this.consigneeAreaName = consigneeAreaName;
    }

    public String getShipperAreaName() {
        return shipperAreaName;
    }

    public void setShipperAreaName(String shipperAreaName) {
        this.shipperAreaName = shipperAreaName;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public String getSubOrgName() {
        return subOrgName;
    }

    public void setSubOrgName(String subOrgName) {
        this.subOrgName = subOrgName;
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

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }
}