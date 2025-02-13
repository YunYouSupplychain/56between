package com.yunyou.modules.bms.basic.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.bms.basic.entity.SettlementSku;
import com.yunyou.modules.bms.basic.entity.SettlementSkuSupplier;

import java.util.List;

/**
 * 描述：结算商品扩展实体
 *
 * @author Jianhua
 * @version 2019/6/20
 */
public class SettlementSkuEntity extends SettlementSku {
    private static final long serialVersionUID = 3260276635031554473L;
    private String ownerName;
    private String supplierCode;
    private String supplierName;
    private String skuClassName;
    private String orgName;
    List<SettlementSkuSupplier> skuSuppliers = Lists.newArrayList();

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public String getSkuClassName() {
        return skuClassName;
    }

    public void setSkuClassName(String skuClassName) {
        this.skuClassName = skuClassName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<SettlementSkuSupplier> getSkuSuppliers() {
        return skuSuppliers;
    }

    public void setSkuSuppliers(List<SettlementSkuSupplier> skuSuppliers) {
        this.skuSuppliers = skuSuppliers;
    }
}
