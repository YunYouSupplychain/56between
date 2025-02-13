package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 商品条码信息Entity
 */
public class SysTmsItemBarcode extends DataEntity<SysTmsItemBarcode> {

    private static final long serialVersionUID = 1L;
    private String ownerCode;// 货主编码
    private String skuCode;// 商品编码
    private String barcode;// 商品条码
    private String isDefault;// 是否默认
    private String dataSet;// 数据套

    public SysTmsItemBarcode() {
        super();
    }

    public SysTmsItemBarcode(String id) {
        super(id);
    }

    public SysTmsItemBarcode(String ownerCode, String skuCode, String dataSet) {
        this.ownerCode = ownerCode;
        this.skuCode = skuCode;
        this.dataSet = dataSet;
    }

    public SysTmsItemBarcode(String ownerCode, String skuCode, String barcode, String dataSet) {
        this.ownerCode = ownerCode;
        this.skuCode = skuCode;
        this.barcode = barcode;
        this.dataSet = dataSet;
    }

    @ExcelField(title = "货主编码", align = 2, sort = 7)
    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    @ExcelField(title = "商品编码", align = 2, sort = 8)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "商品条码", align = 2, sort = 9)
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @ExcelField(title = "是否默认", dictType = "SYS_YES_NO", align = 2, sort = 10)
    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}