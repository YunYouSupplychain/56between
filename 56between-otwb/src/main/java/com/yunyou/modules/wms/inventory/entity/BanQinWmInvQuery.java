package com.yunyou.modules.wms.inventory.entity;

import com.yunyou.core.persistence.DataEntity;

/**
 * 库存查询实体类entity
 * @author WMJ
 * @version 2019/02/28
 */
public class BanQinWmInvQuery extends DataEntity<BanQinWmInvQuery> {
	private static final long serialVersionUID = 1L;
    private String ownerCode;
    private String ownerName;
    private String printUom;
    private String cdprDesc;
    private Double qty;
    private Double qtyUom;
    private Double qtyAvailable;
    private Double qtyAlloc;
    private Double qtyHold;
    private Double qtyPk;
    private Double qtyPaOut;
    private Double qtyRpOut;
    private Double qtyMvOut;
    private Double qtyPaIn;
    private Double qtyRpIn;
    private Double qtyMvIn;
    private String skuCode;
    private String skuName;
    private String lotNum;
    private Double qtyPrealloc;
    private String locCode;
    private String zoneCode;
    private String areaCode;
    private String traceId;
    private String lotAtt01;
    private String lotAtt02;
    private String lotAtt03;
    private String lotAtt04;
    private String lotAtt05;
    private String lotAtt06;
    private String lotAtt07;
    private String lotAtt08;
    private String lotAtt09;
    private String lotAtt10;
    private String lotAtt11;
    private String lotAtt12;
    private String orgId;
    private String orgName;

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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getQtyUom() {
        return qtyUom;
    }

    public void setQtyUom(Double qtyUom) {
        this.qtyUom = qtyUom;
    }

    public Double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(Double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
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

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public Double getQtyPrealloc() {
        return qtyPrealloc;
    }

    public void setQtyPrealloc(Double qtyPrealloc) {
        this.qtyPrealloc = qtyPrealloc;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}