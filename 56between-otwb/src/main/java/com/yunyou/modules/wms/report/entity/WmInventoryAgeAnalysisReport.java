package com.yunyou.modules.wms.report.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;

import java.util.Date;

/**
 * 库龄分析报表
 */
public class WmInventoryAgeAnalysisReport {

    @ExcelField(title = "货主编码")
    private String ownerCode;
    @ExcelField(title = "货主名称")
    private String ownerName;
    @ExcelField(title = "商品编码")
    private String skuCode;
    @ExcelField(title = "商品名称")
    private String skuName;
    @ExcelField(title = "批次号")
    private String lotNum;
    @ExcelField(title = "包装单位")
    private String uom;
    @ExcelField(title = "包装单位名称")
    private String uomDesc;
    @ExcelField(title = "库存数")
    private String invQtyUom;
    @ExcelField(title = "库存数EA")
    private Double invQty;
    @ExcelField(title = "库龄")
    private Integer daysInStock;
    @ExcelField(title = "生产日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date lotAtt01;
    @ExcelField(title = "失效日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date lotAtt02;
    @ExcelField(title = "入库日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date lotAtt03;
    @ExcelField(title = "品质")
    private Date lotAtt04;
    @ExcelField(title = "批次属性05")
    private Date lotAtt05;
    @ExcelField(title = "批次属性06")
    private Date lotAtt06;
    @ExcelField(title = "批次属性07")
    private Date lotAtt07;
    @ExcelField(title = "批次属性08")
    private Date lotAtt08;
    @ExcelField(title = "批次属性09")
    private Date lotAtt09;
    @ExcelField(title = "批次属性10")
    private Date lotAtt10;
    @ExcelField(title = "批次属性11")
    private Date lotAtt11;
    @ExcelField(title = "批次属性12")
    private Date lotAtt12;
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

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
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

    public String getInvQtyUom() {
        return invQtyUom;
    }

    public void setInvQtyUom(String invQtyUom) {
        this.invQtyUom = invQtyUom;
    }

    public Double getInvQty() {
        return invQty;
    }

    public void setInvQty(Double invQty) {
        this.invQty = invQty;
    }

    public Integer getDaysInStock() {
        return daysInStock;
    }

    public void setDaysInStock(Integer daysInStock) {
        this.daysInStock = daysInStock;
    }

    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public Date getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(Date lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    public Date getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(Date lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    public Date getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(Date lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    public Date getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(Date lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    public Date getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(Date lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    public Date getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(Date lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    public Date getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(Date lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    public Date getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(Date lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    public Date getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(Date lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    public Date getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(Date lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    public Date getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(Date lotAtt12) {
        this.lotAtt12 = lotAtt12;
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
