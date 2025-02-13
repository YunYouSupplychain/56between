package com.yunyou.modules.wms.report.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 库存周转率报表
 */
public class WmInventoryTurnoverReport {

    @ExcelField(title = "货主编码")
    private String ownerCode;
    @ExcelField(title = "货主名称")
    private String ownerName;
    @ExcelField(title = "商品编码")
    private String skuCode;
    @ExcelField(title = "商品名称")
    private String skuName;
    @ExcelField(title = "包装单位")
    private String uom;
    @ExcelField(title = "包装单位名称")
    private String uomDesc;
    @ExcelField(title = "库存数")
    private String invQtyUom;
    @ExcelField(title = "库存数EA")
    private Double invQty;
    @ExcelField(title = "周转率")
    private Double turnoverRate;
    private String orgId;
    @ExcelField(title = "仓库")
    private String orgName;

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public Double getInvQty() {
        return invQty;
    }

    public void setInvQty(Double invQty) {
        this.invQty = invQty;
    }

    public String getInvQtyUom() {
        return invQtyUom;
    }

    public void setInvQtyUom(String invQtyUom) {
        this.invQtyUom = invQtyUom;
    }

    public Double getTurnoverRate() {
        return turnoverRate;
    }

    public void setTurnoverRate(Double turnoverRate) {
        this.turnoverRate = turnoverRate;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
