package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;
import java.util.List;

public class WMSRF_INV_SkuInvDetail_Response implements Serializable {
    // 货主编码
    private String ownerCode;
    // 货主名称
    private String ownerName;
    // 单位
    private String printUom;
    // 单位描述
    private String cdprDesc;
    // 商品代码
    private String skuCode;
    // 商品名称
    private String skuName;
    // 库位
    private String locCode;
    // 批次
    private String lotNum;
    // 托盘
    private String traceId;
    // 数量
    private Double qty;
    // 可用数
    private Double qtyAvailable;
    // 预配数
    private Double qtyPrealloc;
    // 分配数
    private Double qtyAlloc;
    // 冻结数
    private Double qtyHold;
    // 拣货数
    private Double qtyPk;
    // 上架待出
    private Double qtyPaOut;
    // 补货待出
    private Double qtyRpOut;
    // 移动待出
    private Double qtyMvOut;
    // 上架待入
    private Double qtyPaIn;
    // 补货待入
    private Double qtyRpIn;
    // 移动待入
    private Double qtyMvIn;
    // 批次属性01
    private String lotAtt01;
    // 批次属性02
    private String lotAtt02;
    // 批次属性03
    private String lotAtt03;
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
    // 批次属性配置
    private List<WMSRF_CF_ProductionLotConfig> lotConfigs;
    // 包装规格
    private List<WMSRF_CF_ProductionPackageConfig> packageConfigs;
    // 包装代码
    private String packCode;
    // 包装数量
    private Double qtyUom;
    // 是否可移动
    private String isAllowMv;

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

    public String getPrintUom() {
        return printUom;
    }

    public void setPrintUom(String printUom) {
        this.printUom = printUom;
    }

    public String getCdprDesc() {
        return cdprDesc;
    }

    public void setCdprDesc(String cdprDesc) {
        this.cdprDesc = cdprDesc;
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

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(Double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    public Double getQtyPrealloc() {
        return qtyPrealloc;
    }

    public void setQtyPrealloc(Double qtyPrealloc) {
        this.qtyPrealloc = qtyPrealloc;
    }

    public Double getQtyAlloc() {
        return qtyAlloc;
    }

    public void setQtyAlloc(Double qtyAlloc) {
        this.qtyAlloc = qtyAlloc;
    }

    public Double getQtyHold() {
        return qtyHold;
    }

    public void setQtyHold(Double qtyHold) {
        this.qtyHold = qtyHold;
    }

    public Double getQtyPk() {
        return qtyPk;
    }

    public void setQtyPk(Double qtyPk) {
        this.qtyPk = qtyPk;
    }

    public Double getQtyPaOut() {
        return qtyPaOut;
    }

    public void setQtyPaOut(Double qtyPaOut) {
        this.qtyPaOut = qtyPaOut;
    }

    public Double getQtyRpOut() {
        return qtyRpOut;
    }

    public void setQtyRpOut(Double qtyRpOut) {
        this.qtyRpOut = qtyRpOut;
    }

    public Double getQtyMvOut() {
        return qtyMvOut;
    }

    public void setQtyMvOut(Double qtyMvOut) {
        this.qtyMvOut = qtyMvOut;
    }

    public Double getQtyPaIn() {
        return qtyPaIn;
    }

    public void setQtyPaIn(Double qtyPaIn) {
        this.qtyPaIn = qtyPaIn;
    }

    public Double getQtyRpIn() {
        return qtyRpIn;
    }

    public void setQtyRpIn(Double qtyRpIn) {
        this.qtyRpIn = qtyRpIn;
    }

    public Double getQtyMvIn() {
        return qtyMvIn;
    }

    public void setQtyMvIn(Double qtyMvIn) {
        this.qtyMvIn = qtyMvIn;
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

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public Double getQtyUom() {
        return qtyUom;
    }

    public void setQtyUom(Double qtyUom) {
        this.qtyUom = qtyUom;
    }

    public String getIsAllowMv() {
        return isAllowMv;
    }

    public void setIsAllowMv(String isAllowMv) {
        this.isAllowMv = isAllowMv;
    }

    public List<WMSRF_CF_ProductionLotConfig> getLotConfigs() {
        return lotConfigs;
    }

    public void setLotConfigs(List<WMSRF_CF_ProductionLotConfig> lotConfigs) {
        this.lotConfigs = lotConfigs;
    }

    public List<WMSRF_CF_ProductionPackageConfig> getPackageConfigs() {
        return packageConfigs;
    }

    public void setPackageConfigs(List<WMSRF_CF_ProductionPackageConfig> packageConfigs) {
        this.packageConfigs = packageConfigs;
    }
}
