package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 商品条码Entity
 */
public class SysWmsSkuBarcode extends DataEntity<SysWmsSkuBarcode> {

    private static final long serialVersionUID = 1L;
    private String ownerCode;        // 货主编码
    private String skuCode;        // 商品编码
    private String barcode;        // 条码
    private String dataSet;        // 数据套
    private String isDefault;        // 是否默认
    private String headerId;        // 商品Id

    public SysWmsSkuBarcode() {
        super();
    }

    public SysWmsSkuBarcode(String id) {
        super(id);
    }

    public SysWmsSkuBarcode(String id, String headerId, String dataSet) {
        super(id);
        this.headerId = headerId;
        this.dataSet = dataSet;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}