package com.yunyou.modules.wms.inbound.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnEntity;

import java.util.Date;

/**
 * 入库单导出Entity
 *
 * @author WMJ
 * @version 2020-04-03
 */
public class BanQinWmAsnExportEntity extends BanQinWmAsnEntity {

    @Override
    @ExcelField(title = "入库单号", align = 2, sort = 1)
    public String getAsnNo() {
        return super.getAsnNo();
    }

    @Override
    @ExcelField(title = "入库单类型", align = 2, sort = 2, dictType = "SYS_WM_ASN_TYPE")
    public String getAsnType() {
        return super.getAsnType();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 3, dictType = "SYS_WM_ASN_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "审核状态", align = 2, sort = 4, dictType = "SYS_AUDIT_STATUS")
    public String getAuditStatus() {
        return super.getAuditStatus();
    }

    @Override
    @ExcelField(title = "冻结状态", align = 2, sort = 5, dictType = "SYS_WM_ORDER_HOLD_STATUS")
    public String getHoldStatus() {
        return super.getHoldStatus();
    }

    @Override
    @ExcelField(title = "供应商", align = 2, sort = 6)
    public String getSupplierName() {
        return super.getSupplierName();
    }

    @Override
    @ExcelField(title = "结算方", align = 2, sort = 7)
    public String getSettleName() {
        return super.getSettleName();
    }

    @Override
    @ExcelField(title = "订单时间", align = 2, sort = 8)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderTime() {
        return super.getOrderTime();
    }

    @Override
    @ExcelField(title = "商流订单号", align = 2, sort = 9)
    public String getDef1() {
        return super.getDef1();
    }

    @Override
    @ExcelField(title = "供应链订单号", align = 2, sort = 10)
    public String getDef2() {
        return super.getDef2();
    }

    @Override
    @ExcelField(title = "作业任务号", align = 2, sort = 11)
    public String getDef3() {
        return super.getDef3();
    }

    @Override
    @ExcelField(title = "客户订单号", align = 2, sort = 12)
    public String getDef4() {
        return super.getDef4();
    }

    @Override
    @ExcelField(title = "外部单号", align = 2, sort = 13)
    public String getDef5() {
        return super.getDef5();
    }

    @Override
    @ExcelField(title = "货主", align = 2, sort = 14)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "仓库", align = 2, sort = 15)
    public String getOrgName() {
        return super.getOrgName();
    }

    @Override
    @ExcelField(title = "质检状态", align = 2, sort = 16, dictType = "SYS_WM_QC_STATUS")
    public String getQcStatus() {
        return super.getQcStatus();
    }

    @Override
    @ExcelField(title = "物流单号", align = 2, sort = 17)
    public String getLogisticNo() {
        return super.getLogisticNo();
    }

    @Override
    @ExcelField(title = "承运商", align = 2, sort = 18)
    public String getCarrierName() {
        return super.getCarrierName();
    }

    @Override
    @ExcelField(title = "审核人", align = 2, sort = 19)
    public String getAuditOp() {
        return super.getAuditOp();
    }

    @Override
    @ExcelField(title = "审核时间", align = 2, sort = 20)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getAuditTime() {
        return super.getAuditTime();
    }

    @Override
    @ExcelField(title = "冻结人", align = 2, sort = 21)
    public String getHoldOp() {
        return super.getHoldOp();
    }

    @Override
    @ExcelField(title = "冻结时间", align = 2, sort = 22)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getHoldTime() {
        return super.getHoldTime();
    }

    @Override
    @ExcelField(title = "预计到货时间从", align = 2, sort = 23)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFmEta() {
        return super.getFmEta();
    }

    @Override
    @ExcelField(title = "预计到货时间到", align = 2, sort = 24)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getToEta() {
        return super.getToEta();
    }

    @Override
    @ExcelField(title = "优先级别", align = 2, sort = 25, dictType = "SYS_WM_PRIORITY")
    public String getPriority() {
        return super.getPriority();
    }

}
