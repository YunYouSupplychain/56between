package com.yunyou.modules.wms.inventory.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 批次库位库存表Entity
 *
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmInvLotLoc extends DataEntity<BanQinWmInvLotLoc> {

    private static final long serialVersionUID = 1L;
    // 批次号
    private String lotNum;
    // 库位编码
    private String locCode;
    // 跟踪号
    private String traceId;
    // 货主编码
    private String ownerCode;
    // 商品编码
    private String skuCode;
    // 库存数
    private Double qty;
    // 冻结数
    private Double qtyHold;
    // 分配数
    private Double qtyAlloc;
    // 已拣货数
    private Double qtyPk;
    // 上架待出数
    private Double qtyPaOut;
    // 上架待入数
    private Double qtyPaIn;
    // 补货待出数
    private Double qtyRpOut;
    // 补货待入数
    private Double qtyRpIn;
    // 移动待出数
    private Double qtyMvOut;
    // 移动待入数
    private Double qtyMvIn;
    // 分公司
    private String orgId;

    public BanQinWmInvLotLoc() {
        super();
        qty = 0d;
        qtyHold = 0d;
        qtyAlloc = 0d;
        qtyPk = 0d;
        qtyPaOut = 0d;
        qtyPaIn = 0d;
        qtyRpOut = 0d;
        qtyRpIn = 0d;
        qtyMvOut = 0d;
        qtyMvIn = 0d;
        recVer = 0;
    }

    public BanQinWmInvLotLoc(String id) {
        super(id);
    }

    @ExcelField(title = "批次号", align = 2, sort = 2)
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    @ExcelField(title = "库位编码", align = 2, sort = 3)
    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    @ExcelField(title = "跟踪号", align = 2, sort = 4)
    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @ExcelField(title = "货主编码", align = 2, sort = 5)
    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    @ExcelField(title = "商品编码", align = 2, sort = 6)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "库存数", align = 2, sort = 7)
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    @ExcelField(title = "冻结数", align = 2, sort = 8)
    public Double getQtyHold() {
        return qtyHold;
    }

    public void setQtyHold(Double qtyHold) {
        this.qtyHold = qtyHold;
    }

    @ExcelField(title = "分配数", align = 2, sort = 9)
    public Double getQtyAlloc() {
        return qtyAlloc;
    }

    public void setQtyAlloc(Double qtyAlloc) {
        this.qtyAlloc = qtyAlloc;
    }

    @ExcelField(title = "已拣货数", align = 2, sort = 10)
    public Double getQtyPk() {
        return qtyPk;
    }

    public void setQtyPk(Double qtyPk) {
        this.qtyPk = qtyPk;
    }

    @ExcelField(title = "上架待出数", align = 2, sort = 11)
    public Double getQtyPaOut() {
        return qtyPaOut;
    }

    public void setQtyPaOut(Double qtyPaOut) {
        this.qtyPaOut = qtyPaOut;
    }

    @ExcelField(title = "上架待入数", align = 2, sort = 12)
    public Double getQtyPaIn() {
        return qtyPaIn;
    }

    public void setQtyPaIn(Double qtyPaIn) {
        this.qtyPaIn = qtyPaIn;
    }

    @ExcelField(title = "补货待出数", align = 2, sort = 13)
    public Double getQtyRpOut() {
        return qtyRpOut;
    }

    public void setQtyRpOut(Double qtyRpOut) {
        this.qtyRpOut = qtyRpOut;
    }

    @ExcelField(title = "补货待入数", align = 2, sort = 14)
    public Double getQtyRpIn() {
        return qtyRpIn;
    }

    public void setQtyRpIn(Double qtyRpIn) {
        this.qtyRpIn = qtyRpIn;
    }

    @ExcelField(title = "移动待出数", align = 2, sort = 15)
    public Double getQtyMvOut() {
        return qtyMvOut;
    }

    public void setQtyMvOut(Double qtyMvOut) {
        this.qtyMvOut = qtyMvOut;
    }

    @ExcelField(title = "移动待入数", align = 2, sort = 16)
    public Double getQtyMvIn() {
        return qtyMvIn;
    }

    public void setQtyMvIn(Double qtyMvIn) {
        this.qtyMvIn = qtyMvIn;
    }

    @ExcelField(title = "分公司", align = 2, sort = 24)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}