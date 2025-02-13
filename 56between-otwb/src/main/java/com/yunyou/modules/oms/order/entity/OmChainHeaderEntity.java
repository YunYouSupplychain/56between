package com.yunyou.modules.oms.order.entity;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @auther: Jianhua on 2019/5/6
 */
public class OmChainHeaderEntity extends OmChainHeader {
    private static final long serialVersionUID = 5527029010248579873L;

    private String warehouseName;   // 仓库名称
    private String projectName;     // 项目名称
    private String supplierName;    // 供应商名称
    private String customerName;    // 客户名称
    private String principalName;   // 委托方名称
    private String settlementName;  // 结算对象名称
    private String shipperName;     // 发货方名称
    private String consigneeName;   // 收货方名称
    private String ownerName;       // 货主名称
    private String carrierName;     // 承运商名称
    private String orgName;         // 机构名称
    private String consigneeAreaName;// 收货人区域名称
    private String shipperAreaName; // 发货人区域名称
    private Double ownerTaxRate;    // 货主-税率
    private Double supplierTaxRate; // 供应商-税率
    private String clerkName;   // 业务员名称
    private String isHaveSo;        // 是否出库
    /**
     * 查询条件
     */
    private Date orderDateFm;
    private Date orderDateTo;
    private String skuCode;
    private List<String> customerNoList;
    private List<String> extendNoList;
    private MultipartFile customerNoFile;
    private MultipartFile extendNoFile;
    private List<String> businessOrderTypeList;

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public Double getOwnerTaxRate() {
        return ownerTaxRate;
    }

    public void setOwnerTaxRate(Double ownerTaxRate) {
        this.ownerTaxRate = ownerTaxRate;
    }

    public Double getSupplierTaxRate() {
        return supplierTaxRate;
    }

    public void setSupplierTaxRate(Double supplierTaxRate) {
        this.supplierTaxRate = supplierTaxRate;
    }

    public String getIsHaveSo() {
        return isHaveSo;
    }

    public void setIsHaveSo(String isHaveSo) {
        this.isHaveSo = isHaveSo;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public List<String> getCustomerNoList() {
        return customerNoList;
    }

    public void setCustomerNoList(List<String> customerNoList) {
        this.customerNoList = customerNoList;
    }

    public List<String> getExtendNoList() {
        return extendNoList;
    }

    public void setExtendNoList(List<String> extendNoList) {
        this.extendNoList = extendNoList;
    }

    public MultipartFile getCustomerNoFile() {
        return customerNoFile;
    }

    public void setCustomerNoFile(MultipartFile customerNoFile) {
        this.customerNoFile = customerNoFile;
    }

    public MultipartFile getExtendNoFile() {
        return extendNoFile;
    }

    public void setExtendNoFile(MultipartFile extendNoFile) {
        this.extendNoFile = extendNoFile;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public List<String> getBusinessOrderTypeList() {
        return businessOrderTypeList;
    }

    public void setBusinessOrderTypeList(List<String> businessOrderTypeList) {
        this.businessOrderTypeList = businessOrderTypeList;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }
}
