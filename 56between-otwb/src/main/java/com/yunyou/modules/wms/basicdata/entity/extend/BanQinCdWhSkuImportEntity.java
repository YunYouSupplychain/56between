package com.yunyou.modules.wms.basicdata.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;

import java.math.BigDecimal;

/**
 * 商品导入Entity
 * @author WMJ
 * @version 2020-02-24
 */
public class BanQinCdWhSkuImportEntity extends BanQinCdWhSku {

    private String uom;
    private BigDecimal csQty;
    private BigDecimal plQty;
    private BigDecimal cdprTi;
    private BigDecimal cdprHi;

    @Override
    @ExcelField(title="货主编码**必填", align=2, sort=1)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title="商品编码**必填", align=2, sort=2)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @Override
    @ExcelField(title="商品名称**必填", align=2, sort=3)
    public String getSkuName() {
        return super.getSkuName();
    }

    @ExcelField(title="收发货包装单位**必填 填写英文\nEA:件 IP:内包装\nCS:箱 PL:托盘\nOT:大包装", align=2, sort=4)
    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    @ExcelField(title="箱含量**必须是数字类型", align=2, sort=4)
    public BigDecimal getCsQty() {
        return csQty;
    }

    public void setCsQty(BigDecimal csQty) {
        this.csQty = csQty;
    }

    @ExcelField(title="托盘含量**必须是数字类型", align=2, sort=5)
    public BigDecimal getPlQty() {
        return plQty;
    }

    public void setPlQty(BigDecimal plQty) {
        this.plQty = plQty;
    }

    @ExcelField(title = "TI**必须是数字类型", align = 2, sort = 6)
    public BigDecimal getCdprTi() {
        return cdprTi;
    }

    public void setCdprTi(BigDecimal cdprTi) {
        this.cdprTi = cdprTi;
    }

    @ExcelField(title = "HI**必须是数字类型", align = 2, sort = 7)
    public BigDecimal getCdprHi() {
        return cdprHi;
    }

    public void setCdprHi(BigDecimal cdprHi) {
        this.cdprHi = cdprHi;
    }


    @Override
    @ExcelField(title="保质期**只能填写数字", align=2, sort=8)
    public Double getShelfLife() {
        return super.getShelfLife();
    }

    @Override
    @ExcelField(title = "商品温层**填写英文\nNT:常温 TT:恒温\nRT:冷藏 FT:冷冻", align = 2, sort = 8)
    public String getTempLevel() {
        return super.getTempLevel();
    }

    @Override
    @ExcelField(title="上架库位指定规则**必填 填写编码\n MAN:人工指定库位\n PA:上架时计算库位\n RCV:收货时计算库位\n B_RCV_ONE:收货前计算库位-一步收货\n B_RCV_TWO:收货前计算库位-两步收货\n", align=2, sort=9)
    public String getReserveCode() {
        return super.getReserveCode();
    }


    @Override
    @ExcelField(title="上架规则", align=2, sort=10)
    public String getPaRule() {
        return super.getPaRule();
    }

    @Override
    @ExcelField(title="库存周转规则", align=2, sort=11)
    public String getRotationRule() {
        return super.getRotationRule();
    }

    @Override
    @ExcelField(title="分配规则", align=2, sort=12)
    public String getAllocRule() {
        return super.getAllocRule();
    }

    @Override
    @ExcelField(title="上架库区", align=2, sort=13)
    public String getPaZone() {
        return super.getPaZone();
    }

    @Override
    @ExcelField(title="上架库位", align=2, sort=13)
    public String getPaLoc() {
        return super.getPaLoc();
    }

    @Override
    @ExcelField(title="体积**只能填写数字", align=2, sort=14)
    public Double getCubic() {
        return super.getCubic();
    }

    @Override
    @ExcelField(title="毛重**只能填写数字", align=2, sort=15)
    public Double getGrossWeight() {
        return super.getGrossWeight();
    }

    @Override
    @ExcelField(title="净重**只能填写数字", align=2, sort=16)
    public Double getNetWeight() {
        return super.getNetWeight();
    }

    @Override
    @ExcelField(title="单价**只能填写数字", align=2, sort=17)
    public Double getPrice() {
        return super.getPrice();
    }

    @Override
    @ExcelField(title="长**只能填写数字", align=2, sort=18)
    public Double getLength() {
        return super.getLength();
    }

    @Override
    @ExcelField(title="宽**只能填写数字", align=2, sort=19)
    public Double getWidth() {
        return super.getWidth();
    }

    @Override
    @ExcelField(title="高**只能填写数字", align=2, sort=20)
    public Double getHeight() {
        return super.getHeight();
    }

    @Override
    @ExcelField(title="自定义1", align=2, sort=21)
    public String getDef1() {
        return super.getDef11();
    }

    @Override
    @ExcelField(title="自定义2", align=2, sort=22)
    public String getDef2() {
        return super.getDef2();
    }

    @Override
    @ExcelField(title="自定义3", align=2, sort=23)
    public String getDef3() {
        return super.getDef3();
    }

    @Override
    @ExcelField(title="自定义4", align=2, sort=24)
    public String getDef4() {
        return super.getDef4();
    }

    @Override
    @ExcelField(title="自定义5", align=2, sort=25)
    public String getDef5() {
        return super.getDef5();
    }

    @Override
    @ExcelField(title="自定义6", align=2, sort=26)
    public String getDef6() {
        return super.getDef6();
    }

    @Override
    @ExcelField(title="自定义7", align=2, sort=27)
    public String getDef7() {
        return super.getDef7();
    }

    @Override
    @ExcelField(title="自定义8", align=2, sort=28)
    public String getDef8() {
        return super.getDef8();
    }

    @Override
    @ExcelField(title="自定义9", align=2, sort=29)
    public String getDef9() {
        return super.getDef9();
    }

    @Override
    @ExcelField(title="自定义10", align=2, sort=30)
    public String getDef10() {
        return super.getDef10();
    }

    @Override
    @ExcelField(title="自定义11", align=2, sort=31)
    public String getDef11() {
        return super.getDef11();
    }

    @Override
    @ExcelField(title="自定义12", align=2, sort=32)
    public String getDef12() {
        return super.getDef12();
    }

    @Override
    @ExcelField(title="自定义13", align=2, sort=33)
    public String getDef13() {
        return super.getDef13();
    }

    @Override
    @ExcelField(title="自定义14", align=2, sort=34)
    public String getDef14() {
        return super.getDef14();
    }

    @Override
    @ExcelField(title="自定义15", align=2, sort=35)
    public String getDef15() {
        return super.getDef15();
    }
}
