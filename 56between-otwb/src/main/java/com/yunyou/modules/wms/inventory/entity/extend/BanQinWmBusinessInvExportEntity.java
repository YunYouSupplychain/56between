package com.yunyou.modules.wms.inventory.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inventory.entity.BanQinWmBusinessInvEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务库存导出Entity
 * @author WMJ
 * @version 2020-04-27
 */
public class BanQinWmBusinessInvExportEntity extends BanQinWmBusinessInvEntity {

    @Override
    @ExcelField(title = "状态", align = 2, sort = 1, dictType = "SYS_ACT_STATUS")
    public String getOrderType() {
        return super.getOrderType();
    }

    @Override
    @ExcelField(title = "仓库", align = 2, sort = 2)
    public String getZoneName() {
        return super.getZoneName();
    }

    @Override
    @ExcelField(title = "当前客户", align = 2, sort = 3)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "第一货主", align = 2, sort = 4)
    public String getFirstOwner() {
        return super.getFirstOwner();
    }

    @Override
    @ExcelField(title = "货转后货主", align = 2, sort = 5)
    public String getToOwner() {
        return super.getToOwner();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "货转日期", align = 2, sort = 6)
    public Date getTfDate() {
        return super.getTfDate();
    }

    @Override
    @ExcelField(title = "调整日期", align = 2, sort = 7)
    public Date getAdDate() {
        return super.getAdDate();
    }

    @Override
    @ExcelField(title = "订单号", align = 2, sort = 8)
    public String getOrderNo() {
        return super.getOrderNo();
    }

    @Override
    @ExcelField(title = "包库", align = 2, sort = 9)
    public String getLotAtt07() {
        return super.getLotAtt07();
    }

    @Override
    @ExcelField(title = "柜号", align = 2, sort = 10)
    public String getLotAtt06() {
        return super.getLotAtt06();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "入库日期", align = 2, sort = 11)
    public Date getLotAtt03() {
        return super.getLotAtt03();
    }

    @Override
    @ExcelField(title = "品名", align = 2, sort = 12)
    public String getSkuName() {
        return super.getSkuName();
    }

    @Override
    @ExcelField(title = "批次号", align = 2, sort = 13)
    public String getLotNum() {
        return super.getLotNum();
    }

    @Override
    @ExcelField(title = "重量", align = 2, sort = 14)
    public String getLotAtt05() {
        return super.getLotAtt05();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "期初日期", align = 2, sort = 15)
    public Date getBgDate() {
        return super.getBgDate();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "出库日期", align = 2, sort = 16)
    public Date getOutboundDate() {
        return super.getOutboundDate();
    }

    @Override
    @ExcelField(title = "数量(入)", align = 2, sort = 17)
    public BigDecimal getQtyIn() {
        return super.getQtyIn();
    }

    @Override
    @ExcelField(title = "重量(入)", align = 2, sort = 18)
    public BigDecimal getWeightIn() {
        return super.getWeightIn();
    }

    @Override
    @ExcelField(title = "数量(出)", align = 2, sort = 19)
    public BigDecimal getQtyOut() {
        return super.getQtyOut();
    }

    @Override
    @ExcelField(title = "重量(出)", align = 2, sort = 20)
    public BigDecimal getWeightOut() {
        return super.getWeightOut();
    }

    @Override
    @ExcelField(title = "期初库存", align = 2, sort = 21)
    public Double getQtyEaBefore() {
        return super.getQtyEaBefore();
    }

    @Override
    @ExcelField(title = "期末库存", align = 2, sort = 22)
    public Double getQtyEaAfter() {
        return super.getQtyEaAfter();
    }

    @Override
    @ExcelField(title = "库存重量", align = 2, sort = 23)
    public BigDecimal getWeightInv() {
        return super.getWeightInv();
    }

    @Override
    @ExcelField(title = "分色件数", align = 2, sort = 24)
    public String getLotAtt09() {
        return super.getLotAtt09();
    }

    @Override
    @ExcelField(title = "抄码件数", align = 2, sort = 25)
    public String getLotAtt08() {
        return super.getLotAtt08();
    }

    @Override
    @ExcelField(title = "批次属性10", align = 2, sort = 26)
    public String getLotAtt10() {
        return super.getLotAtt10();
    }

    @Override
    @ExcelField(title = "批次属性11", align = 2, sort = 27)
    public String getLotAtt11() {
        return super.getLotAtt11();
    }

    @Override
    @ExcelField(title = "批次属性12", align = 2, sort = 28)
    public String getLotAtt12() {
        return super.getLotAtt12();
    }

    @Override
    @ExcelField(title = "结算月份", align = 2, sort = 29)
    public String getLot() {
        return super.getLot();
    }

    @Override
    @ExcelField(title = "仓储天数", align = 2, sort = 30)
    public Integer getStoreDays() {
        return super.getStoreDays();
    }

    @Override
    @ExcelField(title = "自定义1", align = 2, sort = 31)
    public String getDef1() {
        return super.getDef1();
    }

    @Override
    @ExcelField(title = "自定义2", align = 2, sort = 32)
    public String getDef2() {
        return super.getDef2();
    }

    @Override
    @ExcelField(title = "自定义3", align = 2, sort = 33)
    public String getDef3() {
        return super.getDef3();
    }

    @Override
    @ExcelField(title = "自定义4", align = 2, sort = 34)
    public String getDef4() {
        return super.getDef4();
    }

    @Override
    @ExcelField(title = "自定义5", align = 2, sort = 35)
    public String getDef5() {
        return super.getDef5();
    }

    @Override
    @ExcelField(title = "自定义6", align = 2, sort = 36)
    public String getDef6() {
        return super.getDef6();
    }

    @Override
    @ExcelField(title = "自定义7", align = 2, sort = 37)
    public String getDef7() {
        return super.getDef7();
    }

    @Override
    @ExcelField(title = "自定义8", align = 2, sort = 38)
    public String getDef8() {
        return super.getDef8();
    }

    @Override
    @ExcelField(title = "自定义9", align = 2, sort = 39)
    public String getDef9() {
        return super.getDef9();
    }

    @Override
    @ExcelField(title = "自定义10", align = 2, sort = 40)
    public String getDef10() {
        return super.getDef10();
    }

}
