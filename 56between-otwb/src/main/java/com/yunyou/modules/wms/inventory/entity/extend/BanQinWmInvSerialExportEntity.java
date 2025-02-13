package com.yunyou.modules.wms.inventory.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerialEntity;

import java.util.Date;

/**
 * 序列号库存导出Entity
 * @author WMJ
 * @version 2020-04-07
 */
public class BanQinWmInvSerialExportEntity extends BanQinWmInvSerialEntity {

    @Override
    @ExcelField(title = "货主编码", align = 2, sort = 1)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title = "货主名称", align = 2, sort = 2)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "商品编码", align = 2, sort = 3)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @Override
    @ExcelField(title = "商品名称", align = 2, sort = 4)
    public String getSkuName() {
        return super.getSkuName();
    }

    @Override
    @ExcelField(title = "批次号", align = 2, sort = 5)
    public String getLotNum() {
        return super.getLotNum();
    }

    @Override
    @ExcelField(title = "序列号", align = 2, sort = 6)
    public String getSerialNo() {
        return super.getSerialNo();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "生产日期", align = 2, sort = 7)
    public Date getLotAtt01() {
        return super.getLotAtt01();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "失效日期", align = 2, sort = 8)
    public Date getLotAtt02() {
        return super.getLotAtt02();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "入库日期", align = 2, sort = 9)
    public Date getLotAtt03() {
        return super.getLotAtt03();
    }

    @Override
    @ExcelField(title = "批次属性4", align = 2, sort = 10)
    public String getLotAtt04() {
        return super.getLotAtt04();
    }

    @Override
    @ExcelField(title = "批次属性5", align = 2, sort = 11)
    public String getLotAtt05() {
        return super.getLotAtt05();
    }

    @Override
    @ExcelField(title = "批次属性6", align = 2, sort = 12)
    public String getLotAtt06() {
        return super.getLotAtt06();
    }

    @Override
    @ExcelField(title = "批次属性7", align = 2, sort = 13)
    public String getLotAtt07() {
        return super.getLotAtt07();
    }

    @Override
    @ExcelField(title = "批次属性8", align = 2, sort = 14)
    public String getLotAtt08() {
        return super.getLotAtt08();
    }

    @Override
    @ExcelField(title = "批次属性9", align = 2, sort = 15)
    public String getLotAtt09() {
        return super.getLotAtt09();
    }

    @Override
    @ExcelField(title = "批次属性10", align = 2, sort = 16)
    public String getLotAtt10() {
        return super.getLotAtt10();
    }

    @Override
    @ExcelField(title = "批次属性11", align = 2, sort = 17)
    public String getLotAtt11() {
        return super.getLotAtt11();
    }

    @Override
    @ExcelField(title = "批次属性12", align = 2, sort = 18)
    public String getLotAtt12() {
        return super.getLotAtt12();
    }

}
