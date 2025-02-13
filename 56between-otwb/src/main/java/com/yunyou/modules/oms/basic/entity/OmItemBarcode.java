package com.yunyou.modules.oms.basic.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 商品条码表Entity
 *
 * @author WMJ
 * @version 2019-04-15
 */
public class OmItemBarcode extends DataEntity<OmItemBarcode> {

    private static final long serialVersionUID = 1L;
    private String lineNo;        // 行号
    private String barcode;        // 条码
    private String isDefault;        // 是否默认
    private String itemId;        // 商品表Id 父类
    private String orgId;        // 平台编码

    public OmItemBarcode() {
        super();
    }

    public OmItemBarcode(String id) {
        super(id);
    }

    public OmItemBarcode(String id, String itemId) {
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

    @ExcelField(title = "平台编码", align = 2, sort = 11)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}