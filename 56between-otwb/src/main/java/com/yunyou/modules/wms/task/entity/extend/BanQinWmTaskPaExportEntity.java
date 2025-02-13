package com.yunyou.modules.wms.task.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;

import java.util.Date;

/**
 * 上架任务导出Entity
 *
 * @author WMJ
 * @version 2020-04-03
 */
public class BanQinWmTaskPaExportEntity extends BanQinWmTaskPaEntity {

    @Override
    @ExcelField(title = "上架任务ID", align = 2, sort = 1)
    public String getPaId() {
        return super.getPaId();
    }

    @Override
    @ExcelField(title = "上架任务行号", align = 2, sort = 2)
    public String getLineNo() {
        return super.getLineNo();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 4, dictType = "SYS_WM_TASK_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "单据号", align = 2, sort = 5)
    public String getOrderNo() {
        return super.getOrderNo();
    }

    @Override
    @ExcelField(title = "单据类型", align = 2, sort = 6, dictType = "SYS_WM_ORDER_TYPE")
    public String getOrderType() {
        return super.getOrderType();
    }

    @Override
    @ExcelField(title = "货主编码", align = 2, sort = 7)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title = "商品编码", align = 2, sort = 8)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @Override
    @ExcelField(title = "批次号", align = 2, sort = 9)
    public String getLotNum() {
        return super.getLotNum();
    }

    @Override
    @ExcelField(title = "源库位编码", align = 2, sort = 10)
    public String getFmLoc() {
        return super.getFmLoc();
    }

    @Override
    @ExcelField(title = "源跟踪号", align = 2, sort = 11)
    public String getFmId() {
        return super.getFmId();
    }

    @Override
    @ExcelField(title = "目标库位编码", align = 2, sort = 12)
    public String getToLoc() {
        return super.getToLoc();
    }

    @Override
    @ExcelField(title = "目标跟踪号", align = 2, sort = 13)
    public String getToId() {
        return super.getToId();
    }

    @Override
    @ExcelField(title = "上架库位指定规则", align = 2, sort = 14, dictType = "SYS_WM_RESERVE_CODE")
    public String getReserveCode() {
        return super.getReserveCode();
    }

    @Override
    @ExcelField(title = "上架规则", align = 2, sort = 15)
    public String getPaRule() {
        return super.getPaRule();
    }

    @Override
    @ExcelField(title = "推荐库位", align = 2, sort = 16)
    public String getSuggestLoc() {
        return super.getSuggestLoc();
    }

    @Override
    @ExcelField(title = "包装编码", align = 2, sort = 17)
    public String getPackCode() {
        return super.getPackCode();
    }

    @Override
    @ExcelField(title = "包装单位", align = 2, sort = 18)
    public String getUom() {
        return super.getUom();
    }

    @Override
    @ExcelField(title = "上架包装数量", align = 2, sort = 19)
    public Double getQtyPaUom() {
        return super.getQtyPaUom();
    }

    @Override
    @ExcelField(title = "上架数EA", align = 2, sort = 20)
    public Double getQtyPaEa() {
        return super.getQtyPaEa();
    }

    @Override
    @ExcelField(title = "上架操作人", align = 2, sort = 21)
    public String getPaOp() {
        return super.getPaOp();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "上架时间", align = 2, sort = 22)
    public Date getPaTime() {
        return super.getPaTime();
    }

    @Override
    @ExcelField(title = "托盘ID", align = 2, sort = 23)
    public String getTraceId() {
        return super.getTraceId();
    }

}