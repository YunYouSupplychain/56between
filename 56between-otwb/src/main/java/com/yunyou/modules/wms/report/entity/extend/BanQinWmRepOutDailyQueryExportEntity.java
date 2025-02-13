package com.yunyou.modules.wms.report.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.outbound.entity.BanQinWmRepOutDailyQueryEntity;

import java.util.Date;

/**
 * 出库日报表导出entity
 * @author WMJ
 * @version 2020/04/07
 */
public class BanQinWmRepOutDailyQueryExportEntity extends BanQinWmRepOutDailyQueryEntity {

    @Override
    @ExcelField(title = "出库单号", align = 2, sort = 1)
    public String getSoNo() {
        return super.getSoNo();
    }

    @Override
    @ExcelField(title = "出库单行号", align = 2, sort = 2)
    public String getLineNo() {
        return super.getLineNo();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 3, dictType = "SYS_WM_SO_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @ExcelField(title = "出库单类型", align = 2, sort = 4, dictType = "SYS_WM_SO_TYPE")
    public String getSoType() {
        return super.getSoType();
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
    @ExcelField(title = "订货箱数", align = 2, sort = 13)
    public Double getQtySoUom() {
        return super.getQtySoUom();
    }

    @Override
    @ExcelField(title = "订货数EA", align = 2, sort = 14)
    public Double getQtySoEa() {
        return super.getQtySoEa();
    }

    @Override
    @ExcelField(title = "发货箱数", align = 2, sort = 15)
    public Double getQtyShipUom() {
        return super.getQtyShipUom();
    }

    @Override
    @ExcelField(title = "发货数EA", align = 2, sort = 16)
    public Double getQtyShipEa() {
        return super.getQtyShipEa();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预计发货时间从", align = 2, sort = 17)
    public Date getFmEtd() {
        return super.getFmEtd();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "预计发货时间到", align = 2, sort = 18)
    public Date getToEtd() {
        return super.getToEtd();
    }

    @Override
    @ExcelField(title = "收货人编码", align = 2, sort = 19)
    public String getConsigneeCode() {
        return super.getConsigneeCode();
    }


    @Override
    @ExcelField(title = "收货人名称", align = 2, sort = 20)
    public String getConsigneeName() {
        return super.getConsigneeName();
    }

    @Override
    @ExcelField(title = "自定义1", align = 2, sort = 21)
    public String getDef1() {
        return super.getDef1();
    }

    @Override
    @ExcelField(title = "自定义2", align = 2, sort = 22)
    public String getDef2() {
        return super.getDef2();
    }

    @Override
    @ExcelField(title = "自定义3", align = 2, sort = 23)
    public String getDef3() {
        return super.getDef3();
    }

    @Override
    @ExcelField(title = "自定义4", align = 2, sort = 24)
    public String getDef4() {
        return super.getDef4();
    }

    @Override
    @ExcelField(title = "自定义5", align = 2, sort = 25)
    public String getDef5() {
        return super.getDef5();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "订单时间", align = 2, sort = 6)
    public Date getOrderTime() {
        return super.getOrderTime();
    }

}
