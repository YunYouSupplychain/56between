package com.yunyou.modules.wms.inventory.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdHeaderEntity;

import java.util.Date;

/**
 * 库存调整导出Entity
 * @author WMJ
 * @version 2020-04-07
 */
public class BanQinWmAdHeaderExportEntity extends BanQinWmAdHeaderEntity {

    @Override
    @ExcelField(title = "调整单号", align = 2, sort = 1)
    public String getAdNo() {
        return super.getAdNo();
    }

    @Override
    @ExcelField(title = "货主编码", align = 2, sort = 2)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title = "调整状态", align = 2, sort = 3, dictType = "SYS_WM_AD_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "审核状态", align = 2, sort = 4, dictType = "SYS_AUDIT_STATUS")
    public String getAuditStatus() {
        return super.getAuditStatus();
    }

    @Override
    @ExcelField(title = "调整原因", align = 2, sort = 5, dictType = "SYS_WM_AD_REASON")
    public String getReasonCode() {
        return super.getReasonCode();
    }

    @Override
    @ExcelField(title = "原因描述", align = 2, sort = 6)
    public String getReason() {
        return super.getReason();
    }

    @Override
    @ExcelField(title = "审核人", align = 2, sort = 7)
    public String getAuditOp() {
        return super.getAuditOp();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "审核时间", align = 2, sort = 8)
    public Date getAuditTime() {
        return super.getAuditTime();
    }

    @Override
    @ExcelField(title = "审核意见", align = 2, sort = 9)
    public String getAuditComment() {
        return super.getAuditComment();
    }

    @Override
    @ExcelField(title = "调整操作人", align = 2, sort = 10)
    public String getAdOp() {
        return super.getAdOp();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "调整时间", align = 2, sort = 11)
    public Date getAdTime() {
        return super.getAdTime();
    }

    @Override
    @ExcelField(title = "盘点单号", align = 2, sort = 12)
    public String getCountNo() {
        return super.getCountNo();
    }

    @Override
    @ExcelField(title = "自定义1", align = 2, sort = 13)
    public String getDef1() {
        return super.getDef1();
    }

    @Override
    @ExcelField(title = "自定义2", align = 2, sort = 14)
    public String getDef2() {
        return super.getDef2();
    }

    @Override
    @ExcelField(title = "自定义3", align = 2, sort = 15)
    public String getDef3() {
        return super.getDef3();
    }

    @Override
    @ExcelField(title = "自定义4", align = 2, sort = 16)
    public String getDef4() {
        return super.getDef4();
    }

    @Override
    @ExcelField(title = "自定义5", align = 2, sort = 17)
    public String getDef5() {
        return super.getDef5();
    }

}
