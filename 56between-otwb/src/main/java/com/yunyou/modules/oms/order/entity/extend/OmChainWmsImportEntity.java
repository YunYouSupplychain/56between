package com.yunyou.modules.oms.order.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.oms.order.entity.OmChainHeader;

import java.math.BigDecimal;
import java.util.Date;

public class OmChainWmsImportEntity extends OmChainHeader {
    private String skuCode;
    private String uom;
    private BigDecimal qty;
    private String planRcvLoc;
    private String orgCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lotAtt01;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lotAtt02;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lotAtt03;
    private String lotAtt04;
    private String lotAtt05;
    private String lotAtt06;
    private String lotAtt07;
    private String lotAtt08;
    private String lotAtt09;
    private String lotAtt10;
    private String lotAtt11;
    private String lotAtt12;

    @ExcelField(title = "客户订单号**必填", align = 2, sort = 1)
    @Override
    public String getCustomerNo() {
        return super.getCustomerNo();
    }

    @ExcelField(title = "订单时间**必填 日期格式: yyyy-MM-dd hh:mm:ss", align = 2, sort = 2)
    @Override
    public Date getOrderDate() {
        return super.getOrderDate();
    }

    @ExcelField(title = "下发机构**必填", align = 2, sort = 3)
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @ExcelField(title = "客户机构", align = 2, sort = 4)
    @Override
    public String getDef10() {
        return super.getDef10();
    }

    @ExcelField(title = "货主编码**必填", align = 2, sort = 5)
    @Override
    public String getOwner() {
        return super.getOwner();
    }

    @ExcelField(title = "供应商编码", align = 2, sort = 6)
    @Override
    public String getSupplierCode() {
        return super.getSupplierCode();
    }

    @ExcelField(title="承运商编码", align=2, sort=7)
    @Override
    public String getCarrier() {
        return super.getCarrier();
    }

    @ExcelField(title="收货人编码", align=2, sort=8)
    @Override
    public String getConsigneeCode() {
        return super.getConsigneeCode();
    }

    @ExcelField(title="联系人名称", align=2, sort=9)
    @Override
    public String getConsignee() {
        return super.getConsignee();
    }

    @ExcelField(title="联系人电话", align=2, sort=10)
    @Override
    public String getConsigneeTel() {
        return super.getConsigneeTel();
    }

    @ExcelField(title="联系人地址", align=2, sort=11)
    @Override
    public String getConsigneeAddress() {
        return super.getConsigneeAddress();
    }

    @ExcelField(title = "商品编码**必填", align = 2, sort = 12)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "包装单位**必填 填写英文\nEA:件 IP:内包装\nCS:箱 PL:托盘\nOT:大包装", align = 2, sort = 13)
    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    @ExcelField(title = "数量**必填 必须是数字类型", align = 2, sort = 14)
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @ExcelField(title = "计划收货库位", align = 2, sort = 15)
    public String getPlanRcvLoc() {
        return planRcvLoc;
    }

    public void setPlanRcvLoc(String planRcvLoc) {
        this.planRcvLoc = planRcvLoc;
    }

    @ExcelField(title = "生产日期**日期格式: yyyy-MM-dd", align = 2, sort = 24)
    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    @ExcelField(title = "失效日期**日期格式: yyyy-MM-dd", align = 2, sort = 25)
    public Date getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(Date lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    @ExcelField(title = "入库日期**日期格式: yyyy-MM-dd", align = 2, sort = 26)
    public Date getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(Date lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    @ExcelField(title = "品质** 填写英文\nY:良品 N:不良品", align = 2, sort = 27)
    public String getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    @ExcelField(title = "批次属性5", align = 2, sort = 28)
    public String getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    @ExcelField(title = "批次属性6", align = 2, sort = 29)
    public String getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    @ExcelField(title = "批次属性7", align = 2, sort = 30)
    public String getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    @ExcelField(title = "批次属性8", align = 2, sort = 31)
    public String getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    @ExcelField(title = "批次属性9", align = 2, sort = 32)
    public String getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    @ExcelField(title = "批次属性10", align = 2, sort = 33)
    public String getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    @ExcelField(title = "批次属性11", align = 2, sort = 34)
    public String getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    @ExcelField(title = "批次属性12", align = 2, sort = 35)
    public String getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
    }
}
