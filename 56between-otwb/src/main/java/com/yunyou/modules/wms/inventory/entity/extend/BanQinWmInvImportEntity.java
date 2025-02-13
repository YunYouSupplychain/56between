package com.yunyou.modules.wms.inventory.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvQuery;

/**
 * 库存导入Entity
 * @author WMJ
 * @version 2020-02-15
 */
public class BanQinWmInvImportEntity extends BanQinWmInvQuery {

    @ExcelField(title="货主编码", align=2, sort=1)
    @Override
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @ExcelField(title="商品编码", align=2, sort=2)
    @Override
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @ExcelField(title="库位编码", align=2, sort=3)
    public String getLocCode() {
        return super.getLocCode();
    }

    @ExcelField(title="跟踪号", align=2, sort=4)
    public String getTraceId() {
        return super.getTraceId();
    }

    @ExcelField(title="库存数", align=2, sort=5)
    public Double getQty() {
        return super.getQty();
    }

    @ExcelField(title="生产日期", align=2, sort=6)
    public String getLotAtt01() {
        return super.getLotAtt01();
    }

    @ExcelField(title="失效日期", align=2, sort=7)
    public String getLotAtt02() {
        return super.getLotAtt02();
    }

    @ExcelField(title="入库日期", align=2, sort=8)
    public String getLotAtt03() {
        return super.getLotAtt03();
    }

    @ExcelField(title="品质", align=2, sort=9)
    public String getLotAtt04() {
        return super.getLotAtt04();
    }

    @ExcelField(title="批次属性5", align=2, sort=10)
    public String getLotAtt05() {
        return super.getLotAtt05();
    }

    @ExcelField(title="批次属性6", align=2, sort=11)
    public String getLotAtt06() {
        return super.getLotAtt06();
    }

    @ExcelField(title="批次属性7", align=2, sort=12)
    public String getLotAtt07() {
        return super.getLotAtt07();
    }

    @ExcelField(title="批次属性8", align=2, sort=13)
    public String getLotAtt08() {
        return super.getLotAtt08();
    }

    @ExcelField(title="批次属性9", align=2, sort=14)
    public String getLotAtt09() {
        return super.getLotAtt09();
    }

    @ExcelField(title="批次属性10", align=2, sort=15)
    public String getLotAtt10() {
        return super.getLotAtt10();
    }

    @ExcelField(title="批次属性11", align=2, sort=16)
    public String getLotAtt11() {
        return super.getLotAtt11();
    }

    @ExcelField(title="批次属性12", align=2, sort=17)
    public String getLotAtt12() {
        return super.getLotAtt12();
    }

}
