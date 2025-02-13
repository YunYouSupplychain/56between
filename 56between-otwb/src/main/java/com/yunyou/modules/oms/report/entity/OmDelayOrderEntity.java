package com.yunyou.modules.oms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.oms.order.entity.OmChainHeader;

import java.util.Date;

public class OmDelayOrderEntity extends OmChainHeader {
    private String ownerName;
    private String skuCode;
    private String skuName;
    private Double qty;

    private Date orderDateFm;
    private Date orderDateTo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "订单时间", align = 2, sort = 1)
    public Date getOrderDate() {
        return super.getOrderDate();
    }

    @ExcelField(title = "货主编码", align = 2, sort = 2)
    public String getOwner() {
        return super.getOwner();
    }

    @ExcelField(title = "货主名称", align = 2, sort = 3)
    public String getOwnerName() {
        return ownerName;
    }

    @ExcelField(title = "收货人", align = 2, sort = 4)
    public String getConsignee() {
        return super.getConsignee();
    }

    @ExcelField(title = "收货人电话", align = 2, sort = 5)
    public String getConsigneeTel() {
        return super.getConsigneeTel();
    }

    @ExcelField(title = "收货人地址", align = 2, sort = 6)
    public String getConsigneeAddress() {
        return super.getConsigneeAddress();
    }

    @ExcelField(title = "收货人地址区域", align = 2, sort = 7)
    public String getConsigneeAddressArea() {
        return super.getConsigneeAddressArea();
    }

    @ExcelField(title = "供应链订单号", align = 2, sort = 8)
    public String getChainNo() {
        return super.getChainNo();
    }

    @ExcelField(title = "客户订单号", align = 2, sort = 9)
    public String getCustomerNo() {
        return super.getCustomerNo();
    }

    @ExcelField(title = "外部单号", align = 2, sort = 10)
    public String getDef1() {
        return super.getDef1();
    }

    @ExcelField(title = "缺货商品编码", align = 2, sort = 11)
    public String getSkuCode() {
        return skuCode;
    }

    @ExcelField(title = "缺货商品名称", align = 2, sort = 12)
    public String getSkuName() {
        return skuName;
    }

    @ExcelField(title = "缺货数量", align = 2, sort = 13)
    public Double getQty() {
        return qty;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderDateFm() {
        return orderDateFm;
    }

    public void setOrderDateFm(Date orderDateFm) {
        this.orderDateFm = orderDateFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(Date orderDateTo) {
        this.orderDateTo = orderDateTo;
    }
}
