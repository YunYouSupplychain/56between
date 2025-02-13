package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 商品条码信息Entity
 *
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmItemBarcode extends DataEntity<TmItemBarcode> {

    private static final long serialVersionUID = 1L;
    private String ownerCode;        // 货主编码
    private String skuCode;        // 商品编码
    private String barcode;        // 商品条码
    private String isDefault;        // 是否默认
    private String orgId;        // 机构ID

    public TmItemBarcode() {
        super();
    }

    public TmItemBarcode(String id) {
        super(id);
    }

    public TmItemBarcode(String ownerCode, String skuCode, String orgId) {
        this.ownerCode = ownerCode;
        this.skuCode = skuCode;
        this.orgId = orgId;
    }

    public TmItemBarcode(String ownerCode, String skuCode, String barcode, String orgId) {
        this.ownerCode = ownerCode;
        this.skuCode = skuCode;
        this.barcode = barcode;
        this.orgId = orgId;
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

    @ExcelField(title = "机构ID", align = 2, sort = 11)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}