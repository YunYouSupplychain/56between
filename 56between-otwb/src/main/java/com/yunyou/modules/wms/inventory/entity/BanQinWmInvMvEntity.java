package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 库存移动Entity
 * @author WMH
 * @version 2019/06/20
 */
public class BanQinWmInvMvEntity extends BanQinWmInvLotLoc {
    // 货主名
    private String ownerName;
    // 商品名
    private String skuName;
    private String packCode;
    // 包装规格
    private String cdpaFormat;
    // 源库位
    private String fmLoc;
    // 源跟踪号
    private String fmTraceId;
    // 源数量
    private Double fmQty;
    // 目标库位
    private String toLoc;
    // 目标跟踪号
    private String toTraceId;
    // 移动数量EA
    private Double toQty;
    // 目标移动数量UOM
    private Double toQtyUom;
    // 目标移动单位
    private String toUom;
    // 目标移动单位描述
    private String toUomDesc;
    // 数量
    private Double cdprQuantity;
    // 批次属性01
    private Date lotAtt01;
    // 批次属性02
    private Date lotAtt02;
    // 批次属性03
    private Date lotAtt03;
    // 批次属性04
    private String lotAtt04;
    // 批次属性05
    private String lotAtt05;
    // 批次属性06
    private String lotAtt06;
    // 批次属性07
    private String lotAtt07;
    // 批次属性08
    private String lotAtt08;
    // 批次属性09
    private String lotAtt09;
    // 批次属性10
    private String lotAtt10;
    // 批次属性11
    private String lotAtt11;
    // 批次属性06
    private String lotAtt12;
    // 可用数
    private String availableQty;
    private String isRf;
    // 是否可移动
    private String isAllowMv;
    private String isAvailable;
    private String isHold;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getCdpaFormat() {
        return cdpaFormat;
    }

    public void setCdpaFormat(String cdpaFormat) {
        this.cdpaFormat = cdpaFormat;
    }

    public String getFmLoc() {
        return fmLoc;
    }

    public void setFmLoc(String fmLoc) {
        this.fmLoc = fmLoc;
    }

    public String getFmTraceId() {
        return fmTraceId;
    }

    public void setFmTraceId(String fmTraceId) {
        this.fmTraceId = fmTraceId;
    }

    public Double getFmQty() {
        return fmQty;
    }

    public void setFmQty(Double fmQty) {
        this.fmQty = fmQty;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getToTraceId() {
        return toTraceId;
    }

    public void setToTraceId(String toTraceId) {
        this.toTraceId = toTraceId;
    }

    public Double getToQty() {
        return toQty;
    }

    public void setToQty(Double toQty) {
        this.toQty = toQty;
    }

    public Double getToQtyUom() {
        return toQtyUom;
    }

    public void setToQtyUom(Double toQtyUom) {
        this.toQtyUom = toQtyUom;
    }

    public String getToUom() {
        return toUom;
    }

    public void setToUom(String toUom) {
        this.toUom = toUom;
    }

    public String getToUomDesc() {
        return toUomDesc;
    }

    public void setToUomDesc(String toUomDesc) {
        this.toUomDesc = toUomDesc;
    }

    public Double getCdprQuantity() {
        return cdprQuantity;
    }

    public void setCdprQuantity(Double cdprQuantity) {
        this.cdprQuantity = cdprQuantity;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(Date lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(Date lotAtt03) {
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

    public String getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(String availableQty) {
        this.availableQty = availableQty;
    }

    public String getIsRf() {
        return isRf;
    }

    public void setIsRf(String isRf) {
        this.isRf = isRf;
    }

    public String getIsAllowMv() {
        return isAllowMv;
    }

    public void setIsAllowMv(String isAllowMv) {
        this.isAllowMv = isAllowMv;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getIsHold() {
        return isHold;
    }

    public void setIsHold(String isHold) {
        this.isHold = isHold;
    }
}
