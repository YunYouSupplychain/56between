package com.yunyou.modules.wms.inventory.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvQuery;

/**
 * 库存导出Entity
 * @author WMJ
 * @version 2020-02-15
 */
public class BanQinWmInvExportEntity extends BanQinWmInvQuery {

    @ExcelField(title="货主编码", align=2, sort=1)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @ExcelField(title="货主名称", align=2, sort=2)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @ExcelField(title="商品编码", align=2, sort=3)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @ExcelField(title="商品名称", align=2, sort=4)
    public String getSkuName() {
        return super.getSkuName();
    }

    @ExcelField(title="批次号", align=2, sort=5)
    public String getLotNum() {
        return super.getLotNum();
    }

    @ExcelField(title="库位编码", align=2, sort=6)
    public String getLocCode() {
        return super.getLocCode();
    }

    @ExcelField(title="跟踪号", align=2, sort=7)
    public String getTraceId() {
        return super.getTraceId();
    }

    @ExcelField(title="包装单位", align=2, sort=8)
    public String getPrintUom() {
        return super.getPrintUom();
    }

    @ExcelField(title="包装单位名称", align=2, sort=9)
    public String getCdprDesc() {
        return super.getCdprDesc();
    }

    @ExcelField(title="库存数EA", align=2, sort=10)
    public Double getQty() {
        return super.getQty();
    }

    @ExcelField(title="库存数", align=2, sort=11)
    public Double getQtyUom() {
        return super.getQtyUom();
    }

    @ExcelField(title="库存可用数EA", align=2, sort=12)
    public Double getQtyAvailable() {
        return super.getQtyAvailable();
    }

    @ExcelField(title="分配数EA", align=2, sort=13)
    public Double getQtyAlloc() {
        return super.getQtyAlloc();
    }

    @ExcelField(title="冻结EA", align=2, sort=14)
    public Double getQtyHold() {
        return super.getQtyHold();
    }

    @ExcelField(title="拣货数EA", align=2, sort=15)
    public Double getQtyPk() {
        return super.getQtyPk();
    }

    @ExcelField(title="上架待出数EA", align=2, sort=16)
    public Double getQtyPaOut() {
        return super.getQtyPaOut();
    }

    @ExcelField(title="补货待出数EA", align=2, sort=17)
    public Double getQtyRpOut() {
        return super.getQtyRpOut();
    }

    @ExcelField(title="移动待出数EA", align=2, sort=18)
    public Double getQtyMvOut() {
        return super.getQtyMvOut();
    }

    @ExcelField(title="上架待入数EA", align=2, sort=19)
    public Double getQtyPaIn() {
        return super.getQtyPaIn();
    }

    @ExcelField(title="补货待入数EA", align=2, sort=20)
    public Double getQtyRpIn() {
        return super.getQtyRpIn();
    }

    @ExcelField(title="移动待入数EA", align=2, sort=21)
    public Double getQtyMvIn() {
        return super.getQtyMvIn();
    }

    @ExcelField(title="生产日期", align=2, sort=22)
    public String getLotAtt01() {
        return super.getLotAtt01();
    }

    @ExcelField(title="失效日期", align=2, sort=23)
    public String getLotAtt02() {
        return super.getLotAtt02();
    }

    @ExcelField(title="入库日期", align=2, sort=24)
    public String getLotAtt03() {
        return super.getLotAtt03();
    }

    @ExcelField(title="品质", align=2, sort=25)
    public String getLotAtt04() {
        return super.getLotAtt04();
    }

    @ExcelField(title="批次属性5", align=2, sort=26)
    public String getLotAtt05() {
        return super.getLotAtt05();
    }

    @ExcelField(title="批次属性6", align=2, sort=27)
    public String getLotAtt06() {
        return super.getLotAtt06();
    }

    @ExcelField(title="批次属性7", align=2, sort=28)
    public String getLotAtt07() {
        return super.getLotAtt07();
    }

    @ExcelField(title="批次属性8", align=2, sort=29)
    public String getLotAtt08() {
        return super.getLotAtt08();
    }

    @ExcelField(title="批次属性9", align=2, sort=30)
    public String getLotAtt09() {
        return super.getLotAtt09();
    }

    @ExcelField(title="批次属性10", align=2, sort=31)
    public String getLotAtt10() {
        return super.getLotAtt10();
    }

    @ExcelField(title="批次属性11", align=2, sort=32)
    public String getLotAtt11() {
        return super.getLotAtt11();
    }

    @ExcelField(title="批次属性12", align=2, sort=33)
    public String getLotAtt12() {
        return super.getLotAtt12();
    }

    @ExcelField(title="仓库", align=2, sort=34)
    public String getOrgName() {
        return super.getOrgName();
    }

}
