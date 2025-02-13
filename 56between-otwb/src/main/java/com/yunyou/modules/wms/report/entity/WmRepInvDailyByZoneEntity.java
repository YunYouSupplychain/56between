package com.yunyou.modules.wms.report.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 库存日表表（按库区）
 */
public class WmRepInvDailyByZoneEntity extends DataEntity<WmRepInvDailyByZoneEntity> {
    private static final long serialVersionUID = -2272256002289580856L;
    @ExcelField(title = "货主编码", type = 1, sort = 1)
    private String ownerCode;
    @ExcelField(title = "货主名称", type = 1, sort = 2)
    private String ownerName;
    @ExcelField(title = "商品编码", type = 1, sort = 3)
    private String skuCode;
    @ExcelField(title = "商品名称", type = 1, sort = 4)
    private String skuName;
    @ExcelField(title = "库区编码", type = 1, sort = 5)
    private String zoneCode;
    @ExcelField(title = "库区名称", type = 1, sort = 6)
    private String zoneName;
    @ExcelField(title = "批次号", type = 1, sort = 7)
    private String lotNum;
    @ExcelField(title = "数量", type = 1, sort = 8)
    private Double invQty;
    @ExcelField(title = "重量", type = 1, sort = 9)
    private Double weight;
    @ExcelField(title = "生产日期", type = 1, sort = 10)
    private String lotAtt01;
    @ExcelField(title = "失效日期", type = 1, sort = 11)
    private String lotAtt02;
    @ExcelField(title = "入库日期", type = 1, sort = 12)
    private String lotAtt03;
    @ExcelField(title = "品质", dictType = "SYS_WM_QC_ATT", type = 1, sort = 13)
    private String lotAtt04;
    @ExcelField(title = "批次属性05", type = 1, sort = 14)
    private String lotAtt05;
    @ExcelField(title = "批次属性06", type = 1, sort = 15)
    private String lotAtt06;
    @ExcelField(title = "批次属性07", type = 1, sort = 16)
    private String lotAtt07;
    @ExcelField(title = "批次属性08", type = 1, sort = 17)
    private String lotAtt08;
    @ExcelField(title = "批次属性09", type = 1, sort = 18)
    private String lotAtt09;
    @ExcelField(title = "批次属性10", type = 1, sort = 19)
    private String lotAtt10;
    @ExcelField(title = "批次属性11", type = 1, sort = 20)
    private String lotAtt11;
    @ExcelField(title = "批次属性12", type = 1, sort = 21)
    private String lotAtt12;

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(String lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public String getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(String lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    public String getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(String lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    public String getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    public String getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    public String getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    public String getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    public String getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    public String getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    public String getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    public String getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    public String getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
    }

    public Double getInvQty() {
        return invQty;
    }

    public void setInvQty(Double invQty) {
        this.invQty = invQty;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
