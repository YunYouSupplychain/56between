package com.yunyou.modules.wms.task.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskRpEntity;

import java.util.Date;

/**
 * 补货任务导出Entity
 *
 * @author WMJ
 * @version 2020-04-03
 */
public class BanQinWmTaskRpExportEntity extends BanQinWmTaskRpEntity {

    @Override
    @ExcelField(title = "补货任务ID", align = 2, sort = 1)
    public String getRpId() {
        return super.getRpId();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 2, dictType = "SYS_WM_TASK_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "货主编码", align = 2, sort = 3)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title = "货主名称", align = 2, sort = 4)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "商品编码", align = 2, sort = 5)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @Override
    @ExcelField(title = "商品名称", align = 2, sort = 6)
    public String getSkuName() {
        return super.getSkuName();
    }

    @Override
    @ExcelField(title = "批次号", align = 2, sort = 7)
    public String getLotNum() {
        return super.getLotNum();
    }

    @Override
    @ExcelField(title = "源库位", align = 2, sort = 8)
    public String getFmLoc() {
        return super.getFmLoc();
    }

    @Override
    @ExcelField(title = "源跟踪号", align = 2, sort = 9)
    public String getFmId() {
        return super.getFmId();
    }

    @Override
    @ExcelField(title = "目标库位", align = 2, sort = 10)
    public String getToLoc() {
        return super.getToLoc();
    }

    @Override
    @ExcelField(title = "目标跟踪号", align = 2, sort = 11)
    public String getToId() {
        return super.getToId();
    }

    @Override
    @ExcelField(title = "包装规格", align = 2, sort = 12)
    public String getPackDesc() {
        return super.getPackDesc();
    }

    @Override
    @ExcelField(title = "包装单位", align = 2, sort = 13)
    public String getUom() {
        return super.getUom();
    }

    @Override
    @ExcelField(title = "补货数", align = 2, sort = 14)
    public Double getQtyRpUom() {
        return super.getQtyRpUom();
    }

    @Override
    @ExcelField(title = "补货数EA", align = 2, sort = 15)
    public Double getQtyRpEa() {
        return super.getQtyRpEa();
    }

    @Override
    @ExcelField(title = "操作人", align = 2, sort = 16)
    public String getRpOp() {
        return super.getRpOp();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "补货时间", align = 2, sort = 17)
    public Date getRpTime() {
        return super.getRpTime();
    }

    @Override
    @ExcelField(title = "库存数", align = 2, sort = 18)
    public Double getQty() {
        return super.getQty();
    }

    @Override
    @ExcelField(title = "库存可用数", align = 2, sort = 19)
    public Integer getQtyUse() {
        return super.getQtyUse();
    }

    @Override
    @ExcelField(title = "冻结数", align = 2, sort = 20)
    public Double getQtyHold() {
        return super.getQtyHold();
    }

    @Override
    @ExcelField(title = "分配数", align = 2, sort = 21)
    public Double getQtyAlloc() {
        return super.getQtyAlloc();
    }

    @Override
    @ExcelField(title = "上架待出数", align = 2, sort = 22)
    public Double getQtyPaOut() {
        return super.getQtyPaOut();
    }

    @Override
    @ExcelField(title = "补货待出数", align = 2, sort = 23)
    public Double getQtyRpOut() {
        return super.getQtyRpOut();
    }

    @Override
    @ExcelField(title = "移动待出数", align = 2, sort = 24)
    public Double getQtyMvOut() {
        return super.getQtyMvOut();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "生产日期", align = 2, sort = 25)
    public Date getLotAtt01() {
        return super.getLotAtt01();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "失效日期", align = 2, sort = 26)
    public Date getLotAtt02() {
        return super.getLotAtt02();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "入库日期", align = 2, sort = 27)
    public Date getLotAtt03() {
        return super.getLotAtt03();
    }

    @Override
    @ExcelField(title = "批次属性4", align = 2, sort = 28)
    public String getLotAtt04() {
        return super.getLotAtt04();
    }

    @Override
    @ExcelField(title = "批次属性5", align = 2, sort = 29)
    public String getLotAtt05() {
        return super.getLotAtt05();
    }

    @Override
    @ExcelField(title = "批次属性6", align = 2, sort = 30)
    public String getLotAtt06() {
        return super.getLotAtt06();
    }

    @Override
    @ExcelField(title = "批次属性7", align = 2, sort = 31)
    public String getLotAtt07() {
        return super.getLotAtt07();
    }

    @Override
    @ExcelField(title = "批次属性8", align = 2, sort = 32)
    public String getLotAtt08() {
        return super.getLotAtt08();
    }

    @Override
    @ExcelField(title = "批次属性9", align = 2, sort = 33)
    public String getLotAtt09() {
        return super.getLotAtt09();
    }

    @Override
    @ExcelField(title = "批次属性10", align = 2, sort = 34)
    public String getLotAtt10() {
        return super.getLotAtt10();
    }

    @Override
    @ExcelField(title = "批次属性11", align = 2, sort = 35)
    public String getLotAtt11() {
        return super.getLotAtt11();
    }

    @Override
    @ExcelField(title = "批次属性12", align = 2, sort = 36)
    public String getLotAtt12() {
        return super.getLotAtt12();
    }
}