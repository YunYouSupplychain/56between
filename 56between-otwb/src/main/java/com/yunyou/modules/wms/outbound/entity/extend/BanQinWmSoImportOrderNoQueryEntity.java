package com.yunyou.modules.wms.outbound.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;

public class BanQinWmSoImportOrderNoQueryEntity {
    private String orderNo;

    @ExcelField(title = "订单号", align = 2, sort = 1)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

}
