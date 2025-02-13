package com.yunyou.modules.wms.crossDock.entity;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-02-14
 */
public class BanQinGetCrossDockSoDetailCountQueryEntity {
    private String[] ownerCodes;
    private String[] skuCodes;
    private String orgId;
    private String soNo;
    private String[] soTypes;
    private Date fmEtdFm;
    private Date fmEtdTo;
    private Date toEtdFm;
    private Date toEtdTo;
    private Long soLineNum;
    private Double qtySoEa;

    private String ownerCode;
    private String ownerName;
    private String skuCode;
    private String skuName;
    private Double qty;
    private Long soNum;
    private Date fmEtd;
    private Date toEtd;

    public String[] getOwnerCodes() {
        return ownerCodes;
    }

    public void setOwnerCodes(String[] ownerCodes) {
        this.ownerCodes = ownerCodes;
    }

    public String[] getSkuCodes() {
        return skuCodes;
    }

    public void setSkuCodes(String[] skuCodes) {
        this.skuCodes = skuCodes;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String[] getSoTypes() {
        return soTypes;
    }

    public void setSoTypes(String[] soTypes) {
        this.soTypes = soTypes;
    }

    public Date getFmEtdFm() {
        return fmEtdFm;
    }

    public void setFmEtdFm(Date fmEtdFm) {
        this.fmEtdFm = fmEtdFm;
    }

    public Date getFmEtdTo() {
        return fmEtdTo;
    }

    public void setFmEtdTo(Date fmEtdTo) {
        this.fmEtdTo = fmEtdTo;
    }

    public Date getToEtdFm() {
        return toEtdFm;
    }

    public void setToEtdFm(Date toEtdFm) {
        this.toEtdFm = toEtdFm;
    }

    public Date getToEtdTo() {
        return toEtdTo;
    }

    public void setToEtdTo(Date toEtdTo) {
        this.toEtdTo = toEtdTo;
    }

    public Long getSoLineNum() {
        return soLineNum;
    }

    public void setSoLineNum(Long soLineNum) {
        this.soLineNum = soLineNum;
    }

    public Double getQtySoEa() {
        return qtySoEa;
    }

    public void setQtySoEa(Double qtySoEa) {
        this.qtySoEa = qtySoEa;
    }

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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Long getSoNum() {
        return soNum;
    }

    public void setSoNum(Long soNum) {
        this.soNum = soNum;
    }

    public Date getFmEtd() {
        return fmEtd;
    }

    public void setFmEtd(Date fmEtd) {
        this.fmEtd = fmEtd;
    }

    public Date getToEtd() {
        return toEtd;
    }

    public void setToEtd(Date toEtd) {
        this.toEtd = toEtd;
    }
}
