package com.yunyou.modules.oms.order.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.oms.order.entity.OmChainHeader;

import java.math.BigDecimal;
import java.util.Date;

public class OmChainDCImportEntity extends OmChainHeader {
    private String skuCode;
    private BigDecimal boxQty;
    private BigDecimal qty;
    private String poNo;
    private String orgCode;

    @ExcelField(title = "订单时间**必填 日期格式: yyyy-MM-dd hh:mm:ss", align = 2, sort = 1)
    @Override
    public Date getOrderDate() {
        return super.getOrderDate();
    }

    @ExcelField(title = "客户订单号**必填", align = 2, sort = 2)
    @Override
    public String getCustomerNo() {
        return super.getCustomerNo();
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

    @ExcelField(title = "供应商编码**必填", align = 2, sort = 6)
    @Override
    public String getSupplierCode() {
        return super.getSupplierCode();
    }

    @ExcelField(title = "订单号", align = 2, sort = 7)
    @Override
    public String getBusinessNo() {
        return super.getBusinessNo();
    }

    @ExcelField(title = "收货方编码**必填", align = 2, sort = 8)
    @Override
    public String getConsigneeCode() {
        return super.getConsigneeCode();
    }

    @ExcelField(title = "商品编码**必填", align = 2, sort = 9)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "箱数 必须是数字类型", align = 2, sort = 10)
    public BigDecimal getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(BigDecimal boxQty) {
        this.boxQty = boxQty;
    }

    @ExcelField(title = "数量 必须是数字类型", align = 2, sort = 11)
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @ExcelField(title = "入库订单类型", align = 2, sort = 12)
    @Override
    public String getDef7() {
        return super.getDef7();
    }

    @ExcelField(title = "门店调入单号", align = 2, sort = 13)
    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    @ExcelField(title = "订单备注", align = 2, sort = 14)
    @Override
    public String getRemarks() {
        return super.getRemarks();
    }

}
