package com.yunyou.modules.wms.basicdata.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;

/**
 * 库位导出Entity
 *
 * @author WMJ
 * @version 2020-04-03
 */
public class BanQinCdWhLocExportEntity extends BanQinCdWhLoc {

    @Override
    @ExcelField(title = "库位编码", align = 2, sort = 1)
    public String getLocCode() {
        return super.getLocCode();
    }

    @Override
    @ExcelField(title = "库区编码", align = 2, sort = 2)
    public String getZoneCode() {
        return super.getZoneCode();
    }

    @Override
    @ExcelField(title = "库区名称", align = 2, sort = 3)
    public String getZoneName() {
        return super.getZoneName();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 4, dictType = "SYS_WM_LOC_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "是否启用", align = 2, sort = 5, dictType = "SYS_YES_NO")
    public String getIsEnable() {
        return super.getIsEnable();
    }

    @Override
    @ExcelField(title = "库位种类", align = 2, sort = 6, dictType = "SYS_WM_CATEGORY")
    public String getCategory() {
        return super.getCategory();
    }

    @Override
    @ExcelField(title = "库位使用类型", align = 2, sort = 7, dictType = "SYS_WM_LOC_USE_TYPE")
    public String getLocUseType() {
        return super.getLocUseType();
    }

    @Override
    @ExcelField(title = "上架顺序", align = 2, sort = 8)
    public String getPaSeq() {
        return super.getPaSeq();
    }

    @Override
    @ExcelField(title = "拣货顺序", align = 2, sort = 9)
    public String getPkSeq() {
        return super.getPkSeq();
    }

    @Override
    @ExcelField(title = "库位ABC", align = 2, sort = 10)
    public String getAbc() {
        return super.getAbc();
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
    @ExcelField(title = "通道", align = 2, sort = 14)
    public String getLane() {
        return super.getLane();
    }

    @Override
    @ExcelField(title = "序号", align = 2, sort = 15)
    public Long getSeq() {
        return super.getSeq();
    }

    @Override
    @ExcelField(title = "层", align = 2, sort = 16)
    public Long getFloor() {
        return super.getFloor();
    }

    @Override
    @ExcelField(title = "库位组", align = 2, sort = 17)
    public String getLocGroup() {
        return super.getLocGroup();
    }

    @Override
    @ExcelField(title = "是否允许混商品", align = 2, sort = 18, dictType = "SYS_YES_NO")
    public String getIsMixSku() {
        return super.getIsMixSku();
    }

    @Override
    @ExcelField(title = "最大混商品数量", align = 2, sort = 19)
    public Long getMaxMixSku() {
        return super.getMaxMixSku();
    }

    @Override
    @ExcelField(title = "是否允许混批次", align = 2, sort = 20, dictType = "SYS_YES_NO")
    public String getIsMixLot() {
        return super.getIsMixLot();
    }

    @Override
    @ExcelField(title = "最大混批次数量", align = 2, sort = 21)
    public Long getMaxMixLot() {
        return super.getMaxMixLot();
    }

    @Override
    @ExcelField(title = "是否忽略ID", align = 2, sort = 22, dictType = "SYS_YES_NO")
    public String getIsLoseId() {
        return super.getIsLoseId();
    }

    @Override
    @ExcelField(title = "最大重量", align = 2, sort = 23)
    public Double getMaxWeight() {
        return super.getMaxWeight();
    }

    @Override
    @ExcelField(title = "最大体积", align = 2, sort = 24)
    public Double getMaxCubic() {
        return super.getMaxCubic();
    }

    @Override
    @ExcelField(title = "最大托盘数", align = 2, sort = 25)
    public Double getMaxPl() {
        return super.getMaxPl();
    }

    @Override
    @ExcelField(title = "X坐标", align = 2, sort = 26)
    public Double getX() {
        return super.getX();
    }

    @Override
    @ExcelField(title = "Y坐标", align = 2, sort = 27)
    public Double getY() {
        return super.getY();
    }

    @Override
    @ExcelField(title = "Z坐标", align = 2, sort = 28)
    public Double getZ() {
        return super.getZ();
    }

    @Override
    @ExcelField(title = "自定义1", align = 2, sort = 29)
    public String getDef1() {
        return super.getDef1();
    }

    @Override
    @ExcelField(title = "自定义2", align = 2, sort = 30)
    public String getDef2() {
        return super.getDef2();
    }

    @Override
    @ExcelField(title = "自定义3", align = 2, sort = 31)
    public String getDef3() {
        return super.getDef3();
    }

    @Override
    @ExcelField(title = "自定义4", align = 2, sort = 32)
    public String getDef4() {
        return super.getDef4();
    }

    @Override
    @ExcelField(title = "自定义5", align = 2, sort = 33)
    public String getDef5() {
        return super.getDef5();
    }

    @Override
    @ExcelField(title = "自定义6", align = 2, sort = 34)
    public String getDef6() {
        return super.getDef6();
    }

    @Override
    @ExcelField(title = "自定义7", align = 2, sort = 35)
    public String getDef7() {
        return super.getDef7();
    }

    @Override
    @ExcelField(title = "自定义8", align = 2, sort = 36)
    public String getDef8() {
        return super.getDef8();
    }

    @Override
    @ExcelField(title = "自定义9", align = 2, sort = 37)
    public String getDef9() {
        return super.getDef9();
    }

    @Override
    @ExcelField(title = "自定义10", align = 2, sort = 38)
    public String getDef10() {
        return super.getDef10();
    }

}