package com.yunyou.modules.wms.inventory.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActHold;

import java.util.Date;

/**
 * 库存冻结导出Entity
 * @author WMJ
 * @version 2020-04-07
 */
public class BanQinWmActHoldExportEntity extends BanQinWmActHold {

    @Override
    @ExcelField(title = "冻结Id", align = 2, sort = 1)
    public String getHoldId() {
        return super.getHoldId();
    }

    @Override
    @ExcelField(title = "行号", align = 2, sort = 2)
    public String getLineNo() {
        return super.getLineNo();
    }

    @Override
    @ExcelField(title = "冻结状态", align = 2, sort = 3, dictType = "SYS_WM_HOLD_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "冻结方式", align = 2, sort = 4, dictType = "SYS_WM_HOLD_TYPE")
    public String getHoldType() {
        return super.getHoldType();
    }

    @Override
    @ExcelField(title = "货主编码", align = 2, sort = 5)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title = "货主名称", align = 2, sort = 6)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "商品编码", align = 2, sort = 7)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @Override
    @ExcelField(title = "商品名称", align = 2, sort = 8)
    public String getSkuName() {
        return super.getSkuName();
    }

    @Override
    @ExcelField(title = "批次号", align = 2, sort = 9)
    public String getLotNum() {
        return super.getLotNum();
    }

    @Override
    @ExcelField(title = "库位编码", align = 2, sort = 10)
    public String getLocCode() {
        return super.getLocCode();
    }

    @Override
    @ExcelField(title = "跟踪号", align = 2, sort = 11)
    public String getTraceId() {
        return super.getTraceId();
    }

    @Override
    @ExcelField(title = "原因编码", align = 2, sort = 10, dictType = "SYS_WM_HOLD_REASON")
    public String getReasonCode() {
        return super.getReasonCode();
    }

    @Override
    @ExcelField(title = "原因描述", align = 2, sort = 11)
    public String getReason() {
        return super.getReason();
    }

    @Override
    @ExcelField(title = "是否可移动", align = 2, sort = 12, dictType = "SYS_YES_NO")
    public String getIsAllowMv() {
        return super.getIsAllowMv();
    }

    @Override
    @ExcelField(title = "是否可调整", align = 2, sort = 13, dictType = "SYS_YES_NO")
    public String getIsAllowAd() {
        return super.getIsAllowAd();
    }

    @Override
    @ExcelField(title = "是否可转移", align = 2, sort = 14, dictType = "SYS_YES_NO")
    public String getIsAllowTf() {
        return super.getIsAllowTf();
    }

    @Override
    @ExcelField(title = "操作人", align = 2, sort = 15)
    public String getOp() {
        return super.getOp();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "操作时间", align = 2, sort = 16)
    public Date getOpTime() {
        return super.getOpTime();
    }

}
