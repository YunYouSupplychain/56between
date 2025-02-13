package com.yunyou.modules.wms.inbound.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 序列号收货Entity
 * @author WMJ
 * @version 2020-05-12
 */
public class BanQinWmAsnSerialReceiveImportEntity {
    private String orderNo;
    private String skuCode;
    private String serialNo;
    private String toLoc;
    private String lotAtt01;
    private String lotAtt02;
    private String lotAtt03;
    private String lotAtt04;
    private String lotAtt05;
    private String lotAtt06;
    private String lotAtt07;
    private String lotAtt08;
    private String lotAtt09;
    private String lotAtt10;
    private String lotAtt11;
    private String lotAtt12;
    private String orgId;

    @ExcelField(title = "订单号**必填", align = 2, sort = 1)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @ExcelField(title = "商品编码**必填", align = 2, sort = 2)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "序列号**必填", align = 2, sort = 3)
    public String getSerialNo() {
        return serialNo;
    }

    @ExcelField(title = "收货库位**必填", align = 2, sort = 4)
    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @ExcelField(title = "生产日期**日期格式: yyyy-MM-dd", align = 2, sort = 5)
    public String getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(String lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    @ExcelField(title = "失效日期**日期格式: yyyy-MM-dd", align = 2, sort = 6)
    public String getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(String lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    @ExcelField(title = "入库日期**日期格式: yyyy-MM-dd", align = 2, sort = 7)
    public String getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(String lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    @ExcelField(title = "品质** 填写英文\nY:良品 N:不良品", align = 2, sort = 8)
    public String getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    @ExcelField(title = "批次属性5", align = 2, sort = 9)
    public String getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    @ExcelField(title = "批次属性6", align = 2, sort = 10)
    public String getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    @ExcelField(title = "批次属性7", align = 2, sort = 11)
    public String getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    @ExcelField(title = "批次属性8", align = 2, sort = 12)
    public String getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    @ExcelField(title = "批次属性9", align = 2, sort = 13)
    public String getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    @ExcelField(title = "批次属性10", align = 2, sort = 14)
    public String getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    @ExcelField(title = "批次属性11", align = 2, sort = 15)
    public String getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    @ExcelField(title = "批次属性12", align = 2, sort = 16)
    public String getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
