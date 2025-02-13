package com.yunyou.modules.wms.outbound.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;

import java.util.Date;

/**
 * 出库单导出Entity
 * @author WMJ
 * @version 2020-04-03
 */
public class BanQinWmSoExportEntity extends BanQinWmSoEntity {
    @Override
    @ExcelField(title = "出库单号", align = 2, sort = 1)
    public String getSoNo() {
        return super.getSoNo();
    }

    @Override
    @ExcelField(title = "波次单号", align = 2, sort = 2)
    public String getWaveNo() {
        return super.getWaveNo();
    }

    @Override
    @ExcelField(title = "出库单类型", align = 2, sort = 3, dictType = "SYS_WM_SO_TYPE")
    public String getSoType() {
        return super.getSoType();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 4, dictType = "SYS_WM_SO_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "审核状态", align = 2, sort = 5, dictType = "SYS_AUDIT_STATUS")
    public String getAuditStatus() {
        return super.getAuditStatus();
    }

    @Override
    @ExcelField(title = "拦截状态", align = 2, sort = 6, dictType = "SYS_WM_INTERCEPT_STATUS")
    public String getInterceptStatus() {
        return super.getInterceptStatus();
    }

    @Override
    @ExcelField(title = "冻结状态", align = 2, sort = 7, dictType = "SYS_WM_ORDER_HOLD_STATUS")
    public String getHoldStatus() {
        return super.getHoldStatus();
    }

    @Override
    @ExcelField(title = "打包状态", align = 2, sort = 8, dictType = "SYS_WM_PACK_STATUS")
    public String getPackStatus() {
        return super.getPackStatus();
    }

    @Override
    @ExcelField(title = "订单时间", align = 2, sort = 9)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderTime() {
        return super.getOrderTime();
    }

    @Override
    @ExcelField(title = "商流订单号", align = 2, sort = 10)
    public String getDef1() {
        return super.getDef1();
    }

    @Override
    @ExcelField(title = "供应链订单号", align = 2, sort = 11)
    public String getDef2() {
        return super.getDef2();
    }

    @Override
    @ExcelField(title = "作业任务号", align = 2, sort = 12)
    public String getDef3() {
        return super.getDef3();
    }

    @Override
    @ExcelField(title = "客户订单号", align = 2, sort = 13)
    public String getDef5() {
        return super.getDef5();
    }

    @Override
    @ExcelField(title = "外部订单号", align = 2, sort = 14)
    public String getDef16() {
        return super.getDef16();
    }

    @Override
    @ExcelField(title = "货主", align = 2, sort = 15)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "仓库", align = 2, sort = 16)
    public String getOrgName() {
        return super.getOrgName();
    }

    @Override
    @ExcelField(title = "优先级别", align = 2, sort = 17, dictType = "SYS_WM_PRIORITY")
    public String getPriority() {
        return super.getPriority();
    }

    @Override
    @ExcelField(title = "物流单号", align = 2, sort = 18)
    public String getLogisticNo() {
        return super.getLogisticNo();
    }

    @Override
    @ExcelField(title = "承运商", align = 2, sort = 19)
    public String getCarrierName() {
        return super.getCarrierName();
    }

    @Override
    @ExcelField(title = "审核人", align = 2, sort = 20)
    public String getAuditOp() {
        return super.getAuditOp();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "审核时间", align = 2, sort = 21)
    public Date getAuditTime() {
        return super.getAuditTime();
    }

    @Override
    @ExcelField(title = "冻结人", align = 2, sort = 22)
    public String getHoldOp() {
        return super.getHoldOp();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "冻结时间", align = 2, sort = 23)
    public Date getHoldTime() {
        return super.getHoldTime();
    }

    @Override
    @ExcelField(title = "联系人", align = 2, sort = 24)
    public String getContactName() {
        return super.getContactName();
    }

    @Override
    @ExcelField(title = "联系人电话", align = 2, sort = 25)
    public String getContactTel() {
        return super.getContactTel();
    }

    @Override
    @ExcelField(title = "联系人三级地址", align = 2, sort = 26)
    public String getDef17() {
        return super.getDef17();
    }

    @Override
    @ExcelField(title = "联系人地址", align = 2, sort = 27)
    public String getContactAddr() {
        return super.getContactAddr();
    }

}
