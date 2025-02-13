package com.yunyou.modules.wms.report.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.report.entity.WmRepCombineEntity;

public class WmRepCombineExportEntity extends WmRepCombineEntity {

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
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "生产日期", align = 2, sort = 6)
    public String getLotAtt01() {
        return super.getLotAtt01();
    }

    @Override
    @ExcelField(title = "包装单位", align = 2, sort = 7)
    public String getUom() {
        return super.getUom();
    }

    @Override
    @ExcelField(title = "期初库存", align = 2, sort = 8)
    public Double getQckc() {
        return super.getQckc();
    }

    @Override
    @ExcelField(title = "期初库存箱数", align = 2, sort = 9)
    public Double getQckcUom() {
        return super.getQckcUom();
    }

    @Override
    @ExcelField(title = "本期入库", align = 2, sort = 10)
    public Double getBqrk() {
        return super.getBqrk();
    }

    @Override
    @ExcelField(title = "本期入库箱数", align = 2, sort = 11)
    public Double getBqrkUom() {
        return super.getBqrkUom();
    }

    @Override
    @ExcelField(title = "本期出库", align = 2, sort = 12)
    public Double getBqck() {
        return super.getBqck();
    }

    @Override
    @ExcelField(title = "本期出库箱数", align = 2, sort = 13)
    public Double getBqckUom() {
        return super.getBqckUom();
    }

    @Override
    @ExcelField(title = "本期调整增加数", align = 2, sort = 14)
    public Double getBqtzzjs() {
        return super.getBqtzzjs();
    }

    @Override
    @ExcelField(title = "本期调整增加箱数", align = 2, sort = 15)
    public Double getBqtzzjsUom() {
        return super.getBqtzzjsUom();
    }

    @Override
    @ExcelField(title = "本期调整减少数", align = 2, sort = 16)
    public Double getBqtzjss() {
        return super.getBqtzjss();
    }

    @Override
    @ExcelField(title = "本期调整减少箱数", align = 2, sort = 17)
    public Double getBqtzjssUom() {
        return super.getBqtzjssUom();
    }

    @Override
    @ExcelField(title = "本期转入数", align = 2, sort = 18)
    public Double getBqzr() {
        return super.getBqzr();
    }

    @Override
    @ExcelField(title = "本期转入箱数", align = 2, sort = 19)
    public Double getBqzrUom() {
        return super.getBqzrUom();
    }

    @Override
    @ExcelField(title = "本期转出数", align = 2, sort = 20)
    public Double getBqzc() {
        return super.getBqzc();
    }

    @Override
    @ExcelField(title = "本期转出箱数", align = 2, sort = 21)
    public Double getBqzcUom() {
        return super.getBqzcUom();
    }

    @Override
    @ExcelField(title = "期末库存", align = 2, sort = 22)
    public Double getQmkc() {
        return super.getQmkc();
    }

    @Override
    @ExcelField(title = "期末库存箱数", align = 2, sort = 23)
    public Double getQmkcUom() {
        return super.getQmkcUom();
    }
}
