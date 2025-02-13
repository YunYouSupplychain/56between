package com.yunyou.modules.wms.inventory.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inventory.entity.BanQinWmCountHeaderEntity;

import java.util.Date;

/**
 * 库存盘点导出Entity
 * @author WMJ
 * @version 2020-04-07
 */
public class BanQinWmCountHeaderExportEntity extends BanQinWmCountHeaderEntity {

    @Override
    @ExcelField(title = "盘点单号", align = 2, sort = 1)
    public String getCountNo() {
        return super.getCountNo();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 2, dictType = "SYS_WM_COUNT_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "盘点单类型", align = 2, sort = 3, dictType = "SYS_WM_COUNT_TYPE")
    public String getCountType() {
        return super.getCountType();
    }

    @Override
    @ExcelField(title = "盘点范围", align = 2, sort = 4, dictType = "SYS_WM_COUNT_RANGE")
    public String getCountRange() {
        return super.getCountRange();
    }

    @Override
    @ExcelField(title = "盘点方式", align = 2, sort = 5, dictType = "SYS_WM_COUNT_MODE")
    public String getCountMode() {
        return super.getCountMode();
    }

    @Override
    @ExcelField(title = "盘点方法", align = 2, sort = 6, dictType = "SYS_WM_COUNT_METHOD")
    public String getCountMethod() {
        return super.getCountMethod();
    }

    @Override
    @ExcelField(title = "监盘人", align = 2, sort = 7)
    public String getMonitorOp() {
        return super.getMonitorOp();
    }

    @Override
    @ExcelField(title = "货主", align = 2, sort = 8)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "商品", align = 2, sort = 9)
    public String getSkuName() {
        return super.getSkuName();
    }

    @Override
    @ExcelField(title = "批次号", align = 2, sort = 10)
    public String getLotNum() {
        return super.getLotNum();
    }

    @Override
    @ExcelField(title = "区域", align = 2, sort = 11)
    public String getAreaName() {
        return super.getAreaName();
    }

    @Override
    @ExcelField(title = "库区", align = 2, sort = 12)
    public String getZoneName() {
        return super.getZoneName();
    }

    @Override
    @ExcelField(title = "库位从", align = 2, sort = 13)
    public String getFmLoc() {
        return super.getFmLoc();
    }

    @Override
    @ExcelField(title = "库位到", align = 2, sort = 14)
    public String getToLoc() {
        return super.getToLoc();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "动盘时间从", align = 2, sort = 15)
    public Date getTakeStartTime() {
        return super.getTakeStartTime();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "动盘时间到", align = 2, sort = 16)
    public Date getTakeEndTime() {
        return super.getTakeEndTime();
    }

    @Override
    @ExcelField(title = "抽盘数量", align = 2, sort = 17)
    public Double getRandomNum() {
        return super.getRandomNum();
    }

    @Override
    @ExcelField(title = "抽盘比例", align = 2, sort = 18)
    public Double getRandomRate() {
        return super.getRandomRate();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "盘点关闭时间", align = 2, sort = 19)
    public Date getCloseTime() {
        return super.getCloseTime();
    }

    @Override
    @ExcelField(title = "仓库", align = 2, sort = 20)
    public String getOrgName() {
        return super.getOrgName();
    }
}
