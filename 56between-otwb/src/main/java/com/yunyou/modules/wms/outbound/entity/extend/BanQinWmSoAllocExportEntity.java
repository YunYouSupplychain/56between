package com.yunyou.modules.wms.outbound.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;

/**
 * 拣货任务导出Entity
 * @author WMJ
 * @version 2020-04-07
 */
public class BanQinWmSoAllocExportEntity extends BanQinWmSoAllocEntity {

    @Override
    @ExcelField(title = "拣货任务Id", align = 2, sort = 1)
    public String getAllocId() {
        return super.getAllocId();
    }

    @Override
    @ExcelField(title = "出库单号", align = 2, sort = 2)
    public String getSoNo() {
        return super.getSoNo();
    }

    @Override
    @ExcelField(title = "出库单行号", align = 2, sort = 3)
    public String getLineNo() {
        return super.getLineNo();
    }

    @Override
    @ExcelField(title = "波次单号", align = 2, sort = 4)
    public String getWaveNo() {
        return super.getWaveNo();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 5, dictType = "SYS_WM_SO_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "货主编码", align = 2, sort = 6)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title = "货主名称", align = 2, sort = 7)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "商品编码", align = 2, sort = 8)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @Override
    @ExcelField(title = "商品名称", align = 2, sort = 9)
    public String getSkuName() {
        return super.getSkuName();
    }

    @Override
    @ExcelField(title = "批次号", align = 2, sort = 10)
    public String getLotNum() {
        return super.getLotNum();
    }

    @Override
    @ExcelField(title = "源库位", align = 2, sort = 11)
    public String getLocCode() {
        return super.getLocCode();
    }

    @Override
    @ExcelField(title = "源跟踪号", align = 2, sort = 12)
    public String getTraceId() {
        return super.getTraceId();
    }

    @Override
    @ExcelField(title = "包装规格", align = 2, sort = 13)
    public String getPackDesc() {
        return super.getPackDesc();
    }

    @Override
    @ExcelField(title = "包装单位", align = 2, sort = 14)
    public String getUomDesc() {
        return super.getUomDesc();
    }

    @Override
    @ExcelField(title = "拣货数", align = 2, sort = 15)
    public Double getQtyUom() {
        return super.getQtyUom();
    }

    @Override
    @ExcelField(title = "拣货数EA", align = 2, sort = 16)
    public Double getQtyEa() {
        return super.getQtyEa();
    }

    @Override
    @ExcelField(title = "目标库位", align = 2, sort = 17)
    public String getToLoc() {
        return super.getToLoc();
    }

    @Override
    @ExcelField(title = "目标跟踪号", align = 2, sort = 18)
    public String getToId() {
        return super.getToId();
    }
}
