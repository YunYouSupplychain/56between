package com.yunyou.modules.sys.common.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 商品条码表Entity
 */
public class SysOmsItemBarcode extends DataEntity<SysOmsItemBarcode> {

    private static final long serialVersionUID = 1L;
    private String lineNo;        // 行号
    private String barcode;        // 条码
    private String isDefault;        // 是否默认
    private String itemId;        // 商品表Id 父类
    private String dataSet;        // 数据套

    public SysOmsItemBarcode() {
        super();
    }

    public SysOmsItemBarcode(String id) {
        super(id);
    }

    public SysOmsItemBarcode(String id, String itemId) {
        super(id);
        this.itemId = itemId;
    }

    @ExcelField(title = "行号", align = 2, sort = 7)
    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    @ExcelField(title = "条码", align = 2, sort = 8)
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @ExcelField(title = "是否默认", align = 2, sort = 9)
    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}