package com.yunyou.modules.oms.order.entity;

import java.util.Date;

/**
 * 描述：
 *
 * @auther: Jianhua on 2019/5/6
 */
public class OmSaleHeaderEntity extends OmSaleHeader {
    private static final long serialVersionUID = -5797397492957322305L;

    private String outWarhouseName; // 仓库名称
    private String projectName;     // 项目名称
    private String ownerName;       // 货主名称
    private String customerName;    // 客户名称
    private String principalName;   // 委托方名称
    private String settlementName;	// 结算对象名称
    private String carrierName;     // 承运商名称
    private String consigneeAreaName;// 收货人区域名称
    private String shipperAreaName; // 发货人区域名称
    private String orgName;		    // 机构名称
    private String departName;		// 部门名称
    private Double taxRate;         // 货主-税率
    private String clerkName;       // 业务员名称
    private String auditOrgName;    // 审核机构名称
    private String auditDepartName; // 审核部门名称
    private String updateOrgName;   // 更新机构名称
    private String updateDepartName;// 更新部门名称

    /**
     * 查询条件
     */
    private Date orderDateFm;
    private Date orderDateTo;

    private String saleOrderNos; // 销售订单号(以逗号分隔)

    public String getOutWarhouseName() {
        return outWarhouseName;
    }

    public void setOutWarhouseName(String outWarhouseName) {
        this.outWarhouseName = outWarhouseName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
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

    public Date getOrderDateFm() {
        return orderDateFm;
    }

    public void setOrderDateFm(Date orderDateFm) {
        this.orderDateFm = orderDateFm;
    }

    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(Date orderDateTo) {
        this.orderDateTo = orderDateTo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public String getAuditOrgName() {
        return auditOrgName;
    }

    public void setAuditOrgName(String auditOrgName) {
        this.auditOrgName = auditOrgName;
    }

    public String getAuditDepartName() {
        return auditDepartName;
    }

    public void setAuditDepartName(String auditDepartName) {
        this.auditDepartName = auditDepartName;
    }

    public String getUpdateOrgName() {
        return updateOrgName;
    }

    public void setUpdateOrgName(String updateOrgName) {
        this.updateOrgName = updateOrgName;
    }

    public String getUpdateDepartName() {
        return updateDepartName;
    }

    public void setUpdateDepartName(String updateDepartName) {
        this.updateDepartName = updateDepartName;
    }

    public String getSaleOrderNos() {
        return saleOrderNos;
    }

    public void setSaleOrderNos(String saleOrderNos) {
        this.saleOrderNos = saleOrderNos;
    }
}
