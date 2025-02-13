package com.yunyou.modules.wms.report.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inbound.entity.BanQinWmRepInDailyQueryEntity;

import java.util.Date;

/**
 * 入库日报表导出entity
 * @author WMJ
 * @version 2020/04/07
 */
public class BanQinWmRepInDailyQueryExportEntity extends BanQinWmRepInDailyQueryEntity {

    @Override
    @ExcelField(title = "入库单号", align = 2, sort = 1)
    public String getAsnNo() {
        return super.getAsnNo();
    }

    @Override
    @ExcelField(title = "入库单行号", align = 2, sort = 2)
    public String getAsnLineNo() {
        return super.getAsnLineNo();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 3, dictType = "SYS_WM_ASN_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "入库单类型", align = 2, sort = 4, dictType = "SYS_WM_ASN_TYPE")
    public String getAsnType() {
        return super.getAsnType();
    }

    @Override
    @ExcelField(title = "物流单号", align = 2, sort = 5)
    public String getLogisticNo() {
        return super.getLogisticNo();
    }

    @Override
    @ExcelField(title = "物流单行号", align = 2, sort = 6)
    public String getLogisticLineNo() {
        return super.getLogisticLineNo();
    }

    @Override
    @ExcelField(title = "货主编码", align = 2, sort = 7)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title = "货主名称", align = 2, sort = 8)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "商品编码", align = 2, sort = 9)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @Override
    @ExcelField(title = "商品名称", align = 2, sort = 10)
    public String getSkuName() {
        return super.getSkuName();
    }

    @Override
    @ExcelField(title = "包装规格", align = 2, sort = 11)
    public String getPackCode() {
        return super.getPackCode();
    }

    @Override
    @ExcelField(title = "包装单位", align = 2, sort = 12)
    public String getUom() {
        return super.getUom();
    }

    @Override
    @ExcelField(title = "预收箱数", align = 2, sort = 13)
    public Double getQtyAsnUom() {
        return super.getQtyAsnEa();
    }

    @Override
    @ExcelField(title = "预收数EA", align = 2, sort = 14)
    public Double getQtyAsnEa() {
        return super.getQtyAsnEa();
    }

    @Override
    @ExcelField(title = "收货库位", align = 2, sort = 15)
    public String getToLoc() {
        return super.getToLoc();
    }

    @Override
    @ExcelField(title = "收货跟踪号", align = 2, sort = 16)
    public String getToId() {
        return super.getToId();
    }

    @Override
    @ExcelField(title = "已收箱数", align = 2, sort = 17)
    public Double getQtyRcvUom() {
        return super.getQtyRcvEa();
    }

    @Override
    @ExcelField(title = "已收数EA", align = 2, sort = 18)
    public Double getQtyRcvEa() {
        return super.getQtyRcvEa();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预计到货时间从", align = 2, sort = 19)
    public Date getFmEta() {
        return super.getFmEta();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预计到货时间到", align = 2, sort = 20)
    public Date getToEta() {
        return super.getToEta();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "收货时间", align = 2, sort = 21)
    public Date getRcvTime() {
        return super.getRcvTime();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "生产日期", align = 2, sort = 22)
    public Date getLotAtt01() {
        return super.getLotAtt01();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "状态", align = 2, sort = 23)
    public Date getLotAtt02() {
        return super.getLotAtt02();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "入库日期", align = 2, sort = 24)
    public Date getLotAtt03() {
        return super.getLotAtt03();
    }

    @Override
    @ExcelField(title = "品质", align = 2, sort = 25)
    public String getLotAtt04() {
        return super.getLotAtt04();
    }

    @Override
    @ExcelField(title = "批次属性5", align = 2, sort = 26)
    public String getLotAtt05() {
        return super.getLotAtt05();
    }

    @Override
    @ExcelField(title = "批次属性6", align = 2, sort = 27)
    public String getLotAtt06() {
        return super.getLotAtt06();
    }

    @Override
    @ExcelField(title = "批次属性7", align = 2, sort = 28)
    public String getLotAtt07() {
        return super.getLotAtt07();
    }

    @Override
    @ExcelField(title = "批次属性8", align = 2, sort = 29)
    public String getLotAtt08() {
        return super.getLotAtt08();
    }

    @Override
    @ExcelField(title = "批次属性9", align = 2, sort = 30)
    public String getLotAtt09() {
        return super.getLotAtt09();
    }

    @Override
    @ExcelField(title = "批次属性10", align = 2, sort = 31)
    public String getLotAtt10() {
        return super.getLotAtt10();
    }

    @Override
    @ExcelField(title = "批次属性11", align = 2, sort = 32)
    public String getLotAtt11() {
        return super.getLotAtt11();
    }

    @Override
    @ExcelField(title = "批次属性12", align = 2, sort = 33)
    public String getLotAtt12() {
        return super.getLotAtt12();
    }

    @Override
    @ExcelField(title = "自定义1", align = 2, sort = 34)
    public String getDef1() {
        return super.getDef1();
    }

    @Override
    @ExcelField(title = "自定义2", align = 2, sort = 35)
    public String getDef2() {
        return super.getDef2();
    }

    @Override
    @ExcelField(title = "自定义3", align = 2, sort = 36)
    public String getDef3() {
        return super.getDef3();
    }

    @Override
    @ExcelField(title = "自定义4", align = 2, sort = 37)
    public String getDef4() {
        return super.getDef4();
    }

    @Override
    @ExcelField(title = "自定义5", align = 2, sort = 38)
    public String getDef5() {
        return super.getDef5();
    }

}
