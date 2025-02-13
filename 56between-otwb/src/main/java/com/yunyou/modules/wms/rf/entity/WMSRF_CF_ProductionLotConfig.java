package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_CF_ProductionLotConfig implements Serializable {
    private String lotCode;
    private String lotType;
    private String isNeed;
    private String lotTitle;
    private String lotRfTitle;
    private String lotFormate;
    private String lotStatus;
    private String lotKey;

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public String getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(String isNeed) {
        this.isNeed = isNeed;
    }

    public String getLotTitle() {
        return lotTitle;
    }

    public void setLotTitle(String lotTitle) {
        this.lotTitle = lotTitle;
    }

    public String getLotRfTitle() {
        return lotRfTitle;
    }

    public void setLotRfTitle(String lotRfTitle) {
        this.lotRfTitle = lotRfTitle;
    }

    public String getLotFormate() {
        return lotFormate;
    }

    public void setLotFormate(String lotFormate) {
        this.lotFormate = lotFormate;
    }

    public String getLotStatus() {
        return lotStatus;
    }

    public void setLotStatus(String lotStatus) {
        this.lotStatus = lotStatus;
    }

    public String getLotKey() {
        return lotKey;
    }

    public void setLotKey(String lotKey) {
        this.lotKey = lotKey;
    }
}
