package com.yunyou.modules.bms.basic.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 结算商品-供应商Entity
 *
 * @author Jianhua Liu
 * @version 2019-06-20
 */
public class SettlementSkuSupplier extends DataEntity<SettlementSkuSupplier> {

    private static final long serialVersionUID = 1L;
    // 结算商品ID
    private String skuId;
    // 供应商编码
    private String supplierCode;
    // 供应商名称
    private String supplierName;
    // 机构ID
    private String orgId;

    public SettlementSkuSupplier() {
        super();
    }

    public SettlementSkuSupplier(String id) {
        super(id);
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}