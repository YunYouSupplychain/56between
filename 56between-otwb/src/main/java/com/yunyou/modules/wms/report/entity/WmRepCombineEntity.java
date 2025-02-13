package com.yunyou.modules.wms.report.entity;

import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

public class WmRepCombineEntity extends DataEntity<WmRepCombineEntity> {
    private String ownerCode;
    private String ownerName;
    private String skuCode;
    private String skuName;
    private String uom;
    private String lotNum;
    private String lotAtt01;		// 批次属性01(生产日期)
    private Double qckc;
    private Double qckcUom;
    private Double bqrk;
    private Double bqrkUom;
    private Double bqck;
    private Double bqckUom;
    private Double bqtzzjs;
    private Double bqtzzjsUom;
    private Double bqtzjss;
    private Double bqtzjssUom;
    private Double bqzr;
    private Double bqzrUom;
    private Double bqzc;
    private Double bqzcUom;
    private Double qmkc;
    private Double qmkcUom;

    private String orgId;
    private Date tranTimeFm;
    private Date tranTimeTo;

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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public Double getQckc() {
        return qckc;
    }

    public void setQckc(Double qckc) {
        this.qckc = qckc;
    }

    public Double getBqrk() {
        return bqrk;
    }

    public void setBqrk(Double bqrk) {
        this.bqrk = bqrk;
    }

    public Double getBqck() {
        return bqck;
    }

    public void setBqck(Double bqck) {
        this.bqck = bqck;
    }

    public Double getBqtzzjs() {
        return bqtzzjs;
    }

    public void setBqtzzjs(Double bqtzzjs) {
        this.bqtzzjs = bqtzzjs;
    }

    public Double getBqtzjss() {
        return bqtzjss;
    }

    public void setBqtzjss(Double bqtzjss) {
        this.bqtzjss = bqtzjss;
    }

    public Double getBqzr() {
        return bqzr;
    }

    public void setBqzr(Double bqzr) {
        this.bqzr = bqzr;
    }

    public Double getBqzc() {
        return bqzc;
    }

    public void setBqzc(Double bqzc) {
        this.bqzc = bqzc;
    }

    public Double getQmkc() {
        return qmkc;
    }

    public void setQmkc(Double qmkc) {
        this.qmkc = qmkc;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Date getTranTimeFm() {
        return tranTimeFm;
    }

    public void setTranTimeFm(Date tranTimeFm) {
        this.tranTimeFm = tranTimeFm;
    }

    public Date getTranTimeTo() {
        return tranTimeTo;
    }

    public void setTranTimeTo(Date tranTimeTo) {
        this.tranTimeTo = tranTimeTo;
    }

//    @JsonFormat(pattern = "yyyy-MM-dd")
    public String getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(String lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public Double getQckcUom() {
        return qckcUom;
    }

    public void setQckcUom(Double qckcUom) {
        this.qckcUom = qckcUom;
    }

    public Double getBqrkUom() {
        return bqrkUom;
    }

    public void setBqrkUom(Double bqrkUom) {
        this.bqrkUom = bqrkUom;
    }

    public Double getBqckUom() {
        return bqckUom;
    }

    public void setBqckUom(Double bqckUom) {
        this.bqckUom = bqckUom;
    }

    public Double getBqtzzjsUom() {
        return bqtzzjsUom;
    }

    public void setBqtzzjsUom(Double bqtzzjsUom) {
        this.bqtzzjsUom = bqtzzjsUom;
    }

    public Double getBqtzjssUom() {
        return bqtzjssUom;
    }

    public void setBqtzjssUom(Double bqtzjssUom) {
        this.bqtzjssUom = bqtzjssUom;
    }

    public Double getBqzrUom() {
        return bqzrUom;
    }

    public void setBqzrUom(Double bqzrUom) {
        this.bqzrUom = bqzrUom;
    }

    public Double getBqzcUom() {
        return bqzcUom;
    }

    public void setBqzcUom(Double bqzcUom) {
        this.bqzcUom = bqzcUom;
    }

    public Double getQmkcUom() {
        return qmkcUom;
    }

    public void setQmkcUom(Double qmkcUom) {
        this.qmkcUom = qmkcUom;
    }
}
