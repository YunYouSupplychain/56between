package com.yunyou.modules.wms.inbound.entity;

import java.util.List;

/**
 * 描述：采购单扩展Entity
 *
 * @auther: Jianhua on 2019/1/29
 */
public class BanQinWmPoEntity extends BanQinWmPoHeader {
    // 货主名称
    private String ownerName;
    // 供应商名称
    private String supplierName;
    // 供应商电话
    private String supplierTel;
    // 供应商传真
    private String supplierFax;
    // 供应商地址
    private String supplierAddress;
    // 供应商行业类型
    private String supplierIndustryType;
    // 采购订单明细
    private List<BanQinWmPoDetailEntity> wmPoDetailEntitys;

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

    public String getSupplierTel() {
        return supplierTel;
    }

    public void setSupplierTel(String supplierTel) {
        this.supplierTel = supplierTel;
    }

    public String getSupplierFax() {
        return supplierFax;
    }

    public void setSupplierFax(String supplierFax) {
        this.supplierFax = supplierFax;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierIndustryType() {
        return supplierIndustryType;
    }

    public void setSupplierIndustryType(String supplierIndustryType) {
        this.supplierIndustryType = supplierIndustryType;
    }

    public List<BanQinWmPoDetailEntity> getWmPoDetailEntitys() {
        return wmPoDetailEntitys;
    }

    public void setWmPoDetailEntitys(List<BanQinWmPoDetailEntity> wmPoDetailEntitys) {
        this.wmPoDetailEntitys = wmPoDetailEntitys;
    }
}
