package com.yunyou.modules.wms.inventory.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTranSerial;

import java.util.Date;

/**
 * 库存序列号交易导出Entity
 *
 * @author WMJ
 * @version 2020-04-07
 */
public class BanQinWmActTranSerialExportEntity extends BanQinWmActTranSerial {

    @Override
    @ExcelField(title = "序列号交易ID", align = 2, sort = 1)
    public String getSerialTranId() {
        return super.getTranId();
    }

    @Override
    @ExcelField(title = "交易类型", align = 2, sort = 2, dictType = "SYS_WM_SERIAL_TRAN_TYPE")
    public String getSerialTranType() {
        return super.getSerialTranType();
    }

    @Override
    @ExcelField(title = "货主", align = 2, sort = 3)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title = "商品", align = 2, sort = 4)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @Override
    @ExcelField(title = "序列号", align = 2, sort = 5)
    public String getSerialNo() {
        return super.getSerialNo();
    }

    @Override
    @ExcelField(title = "单据类型", align = 2, sort = 6)
    public String getOrderType() {
        return super.getOrderType();
    }

    @Override
    @ExcelField(title = "单据号", align = 2, sort = 7)
    public String getOrderNo() {
        return super.getOrderNo();
    }

    @Override
    @ExcelField(title = "行号", align = 2, sort = 8)
    public String getLineNo() {
        return super.getLineNo();
    }

    @Override
    @ExcelField(title = "批次号", align = 2, sort = 9)
    public String getLotNum() {
        return super.getLotNum();
    }

    @Override
    @ExcelField(title = "库存交易ID", align = 2, sort = 10)
    public String getTranId() {
        return super.getTranId();
    }

    @Override
    @ExcelField(title = "操作人", align = 2, sort = 11)
    public String getTranOp() {
        return super.getTranOp();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "交易时间", align = 2, sort = 12)
    public Date getTranTime() {
        return super.getTranTime();
    }

}
