package com.yunyou.modules.wms.basicdata.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuEntity;

import java.util.Date;

/**
 * 商品导出Entity
 *
 * @author WMJ
 * @version 2020-04-02
 */
public class BanQinCdWhSkuExportEntity extends BanQinCdWhSkuEntity {

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
    @ExcelField(title = "包装规格", align = 2, sort = 5)
    public String getCdpaFormat() {
        return super.getCdpaFormat();
    }

    @Override
    @ExcelField(title = "批次属性", align = 2, sort = 6)
    public String getLotName() {
        return super.getLotName();
    }

    @Override
    @ExcelField(title = "体积", align = 2, sort = 7)
    public Double getCubic() {
        return super.getCubic();
    }

    @Override
    @ExcelField(title = "毛重", align = 2, sort = 8)
    public Double getGrossWeight() {
        return super.getGrossWeight();
    }

    @Override
    @ExcelField(title = "净重", align = 2, sort = 9)
    public Double getNetWeight() {
        return super.getNetWeight();
    }

    @Override
    @ExcelField(title = "单价", align = 2, sort = 10)
    public Double getPrice() {
        return super.getPrice();
    }

    @Override
    @ExcelField(title = "长", align = 2, sort = 11)
    public Double getLength() {
        return super.getLength();
    }

    @Override
    @ExcelField(title = "宽", align = 2, sort = 12)
    public Double getWidth() {
        return super.getWidth();
    }

    @Override
    @ExcelField(title = "高", align = 2, sort = 13)
    public Double getHeight() {
        return super.getHeight();
    }

    @Override
    @ExcelField(title = "缺省收货单位", align = 2, sort = 14)
    public String getRcvUomName() {
        return super.getRcvUomName();
    }

    @Override
    @ExcelField(title = "缺省发货单位", align = 2, sort = 15)
    public String getShipUomName() {
        return super.getShipUomName();
    }

    @Override
    @ExcelField(title = "缺省打印单位", align = 2, sort = 16)
    public String getPrintUomName() {
        return super.getPrintUomName();
    }

    @Override
    @ExcelField(title = "库存上限", align = 2, sort = 17)
    public Double getMaxLimit() {
        return super.getMaxLimit();
    }

    @Override
    @ExcelField(title = "库存下限", align = 2, sort = 18)
    public Double getMinLimit() {
        return super.getMinLimit();
    }

    @Override
    @ExcelField(title = "是否做效期控制", align = 2, sort = 19, dictType = "SYS_YES_NO")
    public String getIsValidity() {
        return super.getIsValidity();
    }

    @Override
    @ExcelField(title = "保质期", align = 2, sort = 20)
    public Double getShelfLife() {
        return super.getShelfLife();
    }

    @Override
    @ExcelField(title = "周期类型", align = 2, sort = 21, dictType = "SYS_YES_NO")
    public String getLifeType() {
        return super.getLifeType();
    }

    @Override
    @ExcelField(title = "入库效期(天数)", align = 2, sort = 22)
    public Double getInLifeDays() {
        return super.getInLifeDays();
    }

    @Override
    @ExcelField(title = "出库效期(天数)", align = 2, sort = 23)
    public Double getOutLifeDays() {
        return super.getOutLifeDays();
    }

    @Override
    @ExcelField(title = "是否允许超收", align = 2, sort = 24, dictType = "SYS_YES_NO")
    public String getIsOverRcv() {
        return super.getIsOverRcv();
    }

    @Override
    @ExcelField(title = "超收百分比", align = 2, sort = 25)
    public Double getOverRcvPct() {
        return super.getOverRcvPct();
    }

    @Override
    @ExcelField(title = "上架库区", align = 2, sort = 26)
    public String getPaZoneName() {
        return super.getPaZoneName();
    }

    @Override
    @ExcelField(title = "上架库位", align = 2, sort = 27)
    public String getPaLoc() {
        return super.getPaLoc();
    }

    @Override
    @ExcelField(title = "上架库位指定规则", align = 2, sort = 28, dictType = "SYS_WM_RESERVE_CODE")
    public String getReserveCode() {
        return super.getReserveCode();
    }

    @Override
    @ExcelField(title = "上架规则", align = 2, sort = 29)
    public String getPaRuleName() {
        return super.getPaRuleName();
    }

    @Override
    @ExcelField(title = "库存周转规则", align = 2, sort = 30)
    public String getRotationRuleName() {
        return super.getRotationRuleName();
    }

    @Override
    @ExcelField(title = "分配规则", align = 2, sort = 31)
    public String getAllocRuleName() {
        return super.getAllocRuleName();
    }

    @Override
    @ExcelField(title = "循环级别", align = 2, sort = 32)
    public String getCycleName() {
        return super.getCycleName();
    }

    @Override
    @ExcelField(title = "越库级别", align = 2, sort = 33)
    public String getCdClass() {
        return super.getCdClass();
    }

    @Override
    @ExcelField(title = "上次循环盘点时间", align = 2, sort = 34)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLastCountTime() {
        return super.getLastCountTime();
    }

    @Override
    @ExcelField(title = "首次入库时间", align = 2, sort = 35)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFirstInTime() {
        return super.getFirstInTime();
    }

    @Override
    @ExcelField(title = "款号", align = 2, sort = 36)
    public String getStyle() {
        return super.getStyle();
    }

    @Override
    @ExcelField(title = "颜色", align = 2, sort = 37)
    public String getColor() {
        return super.getColor();
    }

    @Override
    @ExcelField(title = "尺码", align = 2, sort = 38)
    public String getSkuSize() {
        return super.getSkuSize();
    }

    @Override
    @ExcelField(title = "是否危险品", align = 2, sort = 39, dictType = "SYS_YES_NO")
    public String getIsDg() {
        return super.getIsDg();
    }

    @Override
    @ExcelField(title = "危险品等级", align = 2, sort = 40)
    public String getDgClass() {
        return super.getDgClass();
    }

    @Override
    @ExcelField(title = "危险品编号", align = 2, sort = 41)
    public String getUnno() {
        return super.getUnno();
    }

    @Override
    @ExcelField(title = "是否温控", align = 2, sort = 42, dictType = "SYS_YES_NO")
    public String getIsCold() {
        return super.getIsCold();
    }

    @Override
    @ExcelField(title = "最低温度", align = 2, sort = 43)
    public Double getMinTemp() {
        return super.getMinTemp();
    }

    @Override
    @ExcelField(title = "最高温度", align = 2, sort = 44)
    public Double getMaxTemp() {
        return super.getMaxTemp();
    }

    @Override
    @ExcelField(title = "海关商品编码", align = 2, sort = 45)
    public String getHsCode() {
        return super.getHsCode();
    }

    @Override
    @ExcelField(title = "是否序列号管理", align = 2, sort = 46, dictType = "SYS_YES_NO")
    public String getIsSerial() {
        return super.getIsSerial();
    }

    @Override
    @ExcelField(title = "是否为父件", align = 2, sort = 47, dictType = "SYS_YES_NO")
    public String getIsParent() {
        return super.getIsParent();
    }

    @Override
    @ExcelField(title = "是否质检管理", align = 2, sort = 48, dictType = "SYS_YES_NO")
    public String getIsQc() {
        return super.getIsQc();
    }

    @Override
    @ExcelField(title = "质检阶段", align = 2, sort = 49, dictType = "SYS_WM_QC_PHASE")
    public String getQcPhase() {
        return super.getQcPhase();
    }

    @Override
    @ExcelField(title = "质检规则", align = 2, sort = 50)
    public String getQcRuleName() {
        return super.getQcRuleName();
    }

    @Override
    @ExcelField(title = "质检项组", align = 2, sort = 51)
    public String getItemGroupName() {
        return super.getItemGroupName();
    }

    @Override
    @ExcelField(title = "自定义1", align = 2, sort = 52)
    public String getDef1() {
        return super.getDef1();
    }

    @Override
    @ExcelField(title = "自定义2", align = 2, sort = 53)
    public String getDef2() {
        return super.getDef2();
    }

    @Override
    @ExcelField(title = "自定义3", align = 2, sort = 54)
    public String getDef3() {
        return super.getDef3();
    }

    @Override
    @ExcelField(title = "自定义4", align = 2, sort = 55)
    public String getDef4() {
        return super.getDef4();
    }

    @Override
    @ExcelField(title = "自定义5", align = 2, sort = 56)
    public String getDef5() {
        return super.getDef5();
    }

    @Override
    @ExcelField(title = "自定义6", align = 2, sort = 57)
    public String getDef6() {
        return super.getDef6();
    }

    @Override
    @ExcelField(title = "自定义7", align = 2, sort = 58)
    public String getDef7() {
        return super.getDef7();
    }

    @Override
    @ExcelField(title = "自定义8", align = 2, sort = 59)
    public String getDef8() {
        return super.getDef8();
    }

    @Override
    @ExcelField(title = "自定义9", align = 2, sort = 60)
    public String getDef9() {
        return super.getDef9();
    }

    @Override
    @ExcelField(title = "自定义10", align = 2, sort = 61)
    public String getDef10() {
        return super.getDef10();
    }

    @Override
    @ExcelField(title = "自定义11", align = 2, sort = 62)
    public String getDef11() {
        return super.getDef11();
    }

    @Override
    @ExcelField(title = "自定义12", align = 2, sort = 63)
    public String getDef12() {
        return super.getDef12();
    }

    @Override
    @ExcelField(title = "自定义13", align = 2, sort = 64)
    public String getDef13() {
        return super.getDef13();
    }

    @Override
    @ExcelField(title = "自定义14", align = 2, sort = 65)
    public String getDef14() {
        return super.getDef14();
    }

    @Override
    @ExcelField(title = "自定义15", align = 2, sort = 66)
    public String getDef15() {
        return super.getDef15();
    }

    @Override
    @ExcelField(title = "商品效期", align = 2, sort = 67)
    public Double getPeriodOfValidity() {return super.getPeriodOfValidity();}

    @Override
    @ExcelField(title = "效期单位", align = 2, sort = 68)
    public String getValidityUnit() {
        return super.getValidityUnit();
    }

    @Override
    @ExcelField(title = "商品类型", align = 2, sort = 69, dictType = "SYS_MATERIAL_TYPE")
    public String getTypeCode() {
        return super.getTypeCode();
    }

    @Override
    @ExcelField(title = "采购币别", align = 2, sort = 70, dictType = "SYS_CURRENCY")
    public String getStockCurId() {
        return super.getStockCurId();
    }

    @Override
    @ExcelField(title = "商品形态", align = 2, sort = 71, dictType = "SYS_MATERIAL_MORPHOLOGY")
    public String getFormCode() {
        return super.getFormCode();
    }

    @Override
    @ExcelField(title = "应急电话", align = 2, sort = 72)
    public String getEmergencyTel() {
        return super.getEmergencyTel();
    }

    @Override
    @ExcelField(title = "生效日期", align = 2, sort = 73)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEffectiveDate() {
        return super.getEffectiveDate();
    }

    @Override
    @ExcelField(title = "失效日期", align = 2, sort = 74)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getExpirationDate() {
        return super.getExpirationDate();
    }

    @Override
    @ExcelField(title = "闪点", align = 2, sort = 75)
    public Double getFlashPoint() {
        return super.getFlashPoint();
    }

    @Override
    @ExcelField(title = "燃点", align = 2, sort = 76)
    public Double getBurningPoint() {
        return super.getBurningPoint();
    }

    @Override
    @ExcelField(title = "商品温层", align = 2, sort = 77, dictType = "SYS_TEMPR_CATEGORY")
    public String getTempLevel() {
        return super.getTempLevel();
    }

}
