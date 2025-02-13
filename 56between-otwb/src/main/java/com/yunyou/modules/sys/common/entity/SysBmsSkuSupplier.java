package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 结算商品-供应商Entity
 */
public class SysBmsSkuSupplier extends DataEntity<SysBmsSkuSupplier> {

    private static final long serialVersionUID = 1L;
    private String skuId;// 结算商品ID
    private String supplierCode;// 供应商编码
    private String supplierName;// 供应商名称
    private String dataSet;// 数据套

    public SysBmsSkuSupplier() {
        super();
    }

    public SysBmsSkuSupplier(String id) {
        super(id);
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    @ExcelField(title = "供应商编码", align = 2, sort = 9)
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @ExcelField(title = "供应商名称", align = 2, sort = 10)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}