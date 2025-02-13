package com.yunyou.modules.wms.report.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.report.entity.WmRepOutLotEntity;

import java.util.Date;

/**
 * 拣货任务导出Entity
 * @author WMJ
 * @version 2020-04-07
 */
public class BanQinWmRepOutLotExportEntity extends WmRepOutLotEntity {

    private String lotAtt01String;
    private String lotAtt02String;
    private String lotAtt03String;

    @Override
    @ExcelField(title = "出库单号", align = 2, sort = 2)
    public String getSoNo() {
        return super.getSoNo();
    }

    @Override
    @ExcelField(title = "出库单行号", align = 2, sort = 3)
    public String getLineNo() {
        return super.getLineNo();
    }

    @Override
    @ExcelField(title = "状态", align = 2, sort = 4, dictType = "SYS_WM_SO_STATUS")
    public String getStatus() {
        return super.getStatus();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "订单时间", align = 2, sort = 5)
    public Date getOrderTime() {
        return super.getOrderTime();
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "发货时间", align = 2, sort = 5)
    public Date getShipTime() {
        return super.getShipTime();
    }

    @Override
    @ExcelField(title = "货主编码", align = 2, sort = 6)
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    @ExcelField(title = "货主名称", align = 2, sort = 7)
    public String getOwnerName() {
        return super.getOwnerName();
    }

    @Override
    @ExcelField(title = "商品编码", align = 2, sort = 8)
    public String getSkuCode() {
        return super.getSkuCode();
    }

    @Override
    @ExcelField(title = "商品名称", align = 2, sort = 9)
    public String getSkuName() {
        return super.getSkuName();
    }

    @Override
    @ExcelField(title = "库位", align = 2, sort = 10)
    public String getLocCode() {
        return super.getLocCode();
    }

    @Override
    @ExcelField(title = "批次号", align = 2, sort = 11)
    public String getLotNum() {
        return super.getLotNum();
    }

    @Override
    @ExcelField(title = "包装规格", align = 2, sort = 13)
    public String getPackDesc() {
        return super.getPackDesc();
    }

    @Override
    @ExcelField(title = "包装单位", align = 2, sort = 14)
    public String getUomDesc() {
        return super.getUomDesc();
    }

    @Override
    @ExcelField(title = "分配数", align = 2, sort = 15)
    public Double getQtyUom() {
        return super.getQtyUom();
    }

    @Override
    @ExcelField(title = "分配数EA", align = 2, sort = 16)
    public Double getQtyEa() {
        return super.getQtyEa();
    }

    @ExcelField(title = "生产日期", align = 2, sort = 17)
    public String getLotAtt01String() {
        return lotAtt01String;
    }

    public void setLotAtt01String(String lotAtt01String) {
        this.lotAtt01String = lotAtt01String;
    }

    @ExcelField(title = "失效日期", align = 2, sort = 18)
    public String getLotAtt02String() {
        return lotAtt02String;
    }

    public void setLotAtt02String(String lotAtt02String) {
        this.lotAtt02String = lotAtt02String;
    }

    @ExcelField(title = "入库日期", align = 2, sort = 19)
    public String getLotAtt03String() {
        return lotAtt03String;
    }

    public void setLotAtt03String(String lotAtt03String) {
        this.lotAtt03String = lotAtt03String;
    }

    @Override
    @ExcelField(title = "跟踪号", align = 2, sort = 20)
    public String getTraceId() {
        return super.getTraceId();
    }

    @Override
    @ExcelField(title = "客户订单号", align = 2, sort = 21)
    public String getCustomerNo() {
        return super.getCustomerNo();
    }

    @Override
    @ExcelField(title = "收货人名称", align = 2, sort = 22)
    public String getConsigneeName() {
        return super.getConsigneeName();
    }

    @Override
    @ExcelField(title = "收货人电话", align = 2, sort = 23)
    public String getConsigneeTel() {
        return super.getConsigneeTel();
    }

    @Override
    @ExcelField(title = "收货人地址", align = 2, sort = 24)
    public String getConsigneeAddr() {
        return super.getConsigneeAddr();
    }

    @Override
    @ExcelField(title = "订单备注", align = 2, sort = 25)
    public String getOrderRemarks() {
        return super.getOrderRemarks();
    }

    @Override
    @ExcelField(title = "商品长", align = 2, sort = 26)
    public Double getSkuLength() {
        return super.getSkuLength();
    }

    @Override
    @ExcelField(title = "商品宽", align = 2, sort = 27)
    public Double getSkuWidth() {
        return super.getSkuWidth();
    }

    @Override
    @ExcelField(title = "商品高", align = 2, sort = 28)
    public Double getSkuHeight() {
        return super.getSkuHeight();
    }

    @Override
    @ExcelField(title = "商品体积", align = 2, sort = 29)
    public Double getCubic() {
        return super.getCubic();
    }

    @Override
    @ExcelField(title = "商品毛重", align = 2, sort = 30)
    public Double getGrossWeight() {
        return super.getGrossWeight();
    }
}
