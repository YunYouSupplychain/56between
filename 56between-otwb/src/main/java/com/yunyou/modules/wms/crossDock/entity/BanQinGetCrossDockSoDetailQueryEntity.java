package com.yunyou.modules.wms.crossDock.entity;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-02-14
 */
public class BanQinGetCrossDockSoDetailQueryEntity {
    private String[] soNos;
    private String orgId;
    private String ownerCode;
    private String skuCode;

    private String soNo;
    private String soType;
    private String lineNo;
    private String status;
    private String skuName;
    private Double qtySoUom;
    private Double qtySoEa;
    private String uom;
    private String cdprDesc;
    private String packCode;
    private String cdpaFormat;
    private Date fmEtd;
    private Date toEtd;

    public String[] getSoNos() {
        return soNos;
    }

    public void setSoNos(String[] soNos) {
        this.soNos = soNos;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getSoType() {
        return soType;
    }

    public void setSoType(String soType) {
        this.soType = soType;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Double getQtySoUom() {
        return qtySoUom;
    }

    public void setQtySoUom(Double qtySoUom) {
        this.qtySoUom = qtySoUom;
    }

    public Double getQtySoEa() {
        return qtySoEa;
    }

    public void setQtySoEa(Double qtySoEa) {
        this.qtySoEa = qtySoEa;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getCdprDesc() {
        return cdprDesc;
    }

    public void setCdprDesc(String cdprDesc) {
        this.cdprDesc = cdprDesc;
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
