package com.yunyou.modules.wms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 上架任务单报表entity
 * @Author WMJ
 * @version 2019/07/03
 */
public class PutawayTaskLabel {
    private String paId;
    private String fmLoc;
    private String fmId;
    private String zoneCode;
    private String toLoc;
    private String skuCode;
    private String skuName;
    private Double qtyPaUom;
    private Double qtyPaEa;
    private String packCode;
    private Date lotAtt01;

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getFmLoc() {
        return fmLoc;
    }

    public void setFmLoc(String fmLoc) {
        this.fmLoc = fmLoc;
    }

    public String getFmId() {
        return fmId;
    }

    public void setFmId(String fmId) {
        this.fmId = fmId;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
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

    public Double getQtyPaUom() {
        return qtyPaUom;
    }

    public void setQtyPaUom(Double qtyPaUom) {
        this.qtyPaUom = qtyPaUom;
    }

    public Double getQtyPaEa() {
        return qtyPaEa;
    }

    public void setQtyPaEa(Double qtyPaEa) {
        this.qtyPaEa = qtyPaEa;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }
}
