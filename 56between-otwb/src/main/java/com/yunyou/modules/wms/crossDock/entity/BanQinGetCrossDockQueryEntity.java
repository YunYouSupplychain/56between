package com.yunyou.modules.wms.crossDock.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-02-14
 */
public class BanQinGetCrossDockQueryEntity {
    private String asnNo;
    private String[] asnTypes;
    private Date fmEtaFm;
    private Date fmEtaTo;
    private Date toEtaFm;
    private Date toEtaTo;
    private String[] ownerCodes;
    private String[] skuCodes;
    private String soNo;
    private String[] soTypes;
    private Date fmEtdFm;
    private Date fmEtdTo;
    private Date toEtdFm;
    private Date toEtdTo;
    private String orgId;
    private Double qtySoEa;// 总订货数
    private Long soLineNum;// 订单明细行数

    private String ownerCode;
    private String ownerName;
    private String skuCode;
    private String skuName;
    /* 商品库存总数(包含所有数量) */
    private Double qty;
    /* 符合条件的预收数统计数 */
    private Double qtyPlanEa;
    /* 符合条件的入库单数 */
    private Long asnNum;
    /* 符合条件的入库单最早预计到货时间从 */
    private Date fmEta;
    /* 符合条件的入库单最晚预计到货时间到 */
    private Date toEta;
    private Date asnToEta;
    /* 符合条件的发运订单数 */
    private Long soNum;
    /* 符合条件的发运订单最早预计发货时间从 */
    private Date fmEtd;
    private Date toEtd;

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    public String[] getAsnTypes() {
        return asnTypes;
    }

    public void setAsnTypes(String[] asnTypes) {
        this.asnTypes = asnTypes;
    }

    public Date getFmEtaFm() {
        return fmEtaFm;
    }

    public void setFmEtaFm(Date fmEtaFm) {
        this.fmEtaFm = fmEtaFm;
    }

    public Date getFmEtaTo() {
        return fmEtaTo;
    }

    public void setFmEtaTo(Date fmEtaTo) {
        this.fmEtaTo = fmEtaTo;
    }

    public Date getToEtaFm() {
        return toEtaFm;
    }

    public void setToEtaFm(Date toEtaFm) {
        this.toEtaFm = toEtaFm;
    }

    public Date getToEtaTo() {
        return toEtaTo;
    }

    public void setToEtaTo(Date toEtaTo) {
        this.toEtaTo = toEtaTo;
    }

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFmEtdFm() {
        return fmEtdFm;
    }

    public void setFmEtdFm(Date fmEtdFm) {
        this.fmEtdFm = fmEtdFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFmEtdTo() {
        return fmEtdTo;
    }

    public void setFmEtdTo(Date fmEtdTo) {
        this.fmEtdTo = fmEtdTo;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getToEtdFm() {
        return toEtdFm;
    }

    public void setToEtdFm(Date toEtdFm) {
        this.toEtdFm = toEtdFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getToEtdTo() {
        return toEtdTo;
    }

    public void setToEtdTo(Date toEtdTo) {
        this.toEtdTo = toEtdTo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Double getQtySoEa() {
        return qtySoEa;
    }

    public void setQtySoEa(Double qtySoEa) {
        this.qtySoEa = qtySoEa;
    }

    public Long getSoLineNum() {
        return soLineNum;
    }

    public void setSoLineNum(Long soLineNum) {
        this.soLineNum = soLineNum;
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

    public Double getQtyPlanEa() {
        return qtyPlanEa;
    }

    public void setQtyPlanEa(Double qtyPlanEa) {
        this.qtyPlanEa = qtyPlanEa;
    }

    public Long getAsnNum() {
        return asnNum;
    }

    public void setAsnNum(Long asnNum) {
        this.asnNum = asnNum;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFmEta() {
        return fmEta;
    }

    public void setFmEta(Date fmEta) {
        this.fmEta = fmEta;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getToEta() {
        return toEta;
    }

    public void setToEta(Date toEta) {
        this.toEta = toEta;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getAsnToEta() {
        return asnToEta;
    }

    public void setAsnToEta(Date asnToEta) {
        this.asnToEta = asnToEta;
    }

    public Long getSoNum() {
        return soNum;
    }

    public void setSoNum(Long soNum) {
        this.soNum = soNum;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFmEtd() {
        return fmEtd;
    }

    public void setFmEtd(Date fmEtd) {
        this.fmEtd = fmEtd;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getToEtd() {
        return toEtd;
    }

    public void setToEtd(Date toEtd) {
        this.toEtd = toEtd;
    }

}
