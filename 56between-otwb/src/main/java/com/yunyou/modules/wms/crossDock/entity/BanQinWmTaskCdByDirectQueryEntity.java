package com.yunyou.modules.wms.crossDock.entity;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-02-14
 */
public class BanQinWmTaskCdByDirectQueryEntity {
    private String[] statuss;
    private String[] skuCodes;
    private String[] ownerCodes;
    private String asnNo;
    private String lineNo;
    private Date fmEtaFm;
    private Date fmEtaTo;
    private Date toEtaFm;
    private Date toEtaTo;
    private Date fmEtdFm;
    private Date fmEtdTo;
    private Date toEtdFm;
    private Date toEtdTo;
    private String soNo;
    private String cdType;
    private String orgId;

    private String ownerCode;
    private String ownerName;
    private String skuCode;
    private String skuName;
    private String status;
    private String asnLineNo;
    private String rcvLineNo;
    private Double qtyPlanEa;
    private Double qtyPlanUom;
    private Double qtyRcvEa;
    private String asnPackCode;
    private String asnUom;
    private String asnPackDesc;
    private String asnUomDesc;
    private String planId;
    private String toLoc;
    private String toId;
    private String lotNum;
    private String planPaLoc;
    private String qcStatus;
    private String isQc;
    private String qcPhase;
    private String qcRule;
    private String itemGroupCode;
    private String soLineNo;
    private String cdRcvId;

    public String[] getStatuss() {
        return statuss;
    }

    public void setStatuss(String[] statuss) {
        this.statuss = statuss;
    }

    public String[] getSkuCodes() {
        return skuCodes;
    }

    public void setSkuCodes(String[] skuCodes) {
        this.skuCodes = skuCodes;
    }

    public String[] getOwnerCodes() {
        return ownerCodes;
    }

    public void setOwnerCodes(String[] ownerCodes) {
        this.ownerCodes = ownerCodes;
    }

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
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

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getCdType() {
        return cdType;
    }

    public void setCdType(String cdType) {
        this.cdType = cdType;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAsnLineNo() {
        return asnLineNo;
    }

    public void setAsnLineNo(String asnLineNo) {
        this.asnLineNo = asnLineNo;
    }

    public String getRcvLineNo() {
        return rcvLineNo;
    }

    public void setRcvLineNo(String rcvLineNo) {
        this.rcvLineNo = rcvLineNo;
    }

    public Double getQtyPlanEa() {
        return qtyPlanEa;
    }

    public void setQtyPlanEa(Double qtyPlanEa) {
        this.qtyPlanEa = qtyPlanEa;
    }

    public Double getQtyPlanUom() {
        return qtyPlanUom;
    }

    public void setQtyPlanUom(Double qtyPlanUom) {
        this.qtyPlanUom = qtyPlanUom;
    }

    public Double getQtyRcvEa() {
        return qtyRcvEa;
    }

    public void setQtyRcvEa(Double qtyRcvEa) {
        this.qtyRcvEa = qtyRcvEa;
    }

    public String getAsnPackCode() {
        return asnPackCode;
    }

    public void setAsnPackCode(String asnPackCode) {
        this.asnPackCode = asnPackCode;
    }

    public String getAsnUom() {
        return asnUom;
    }

    public void setAsnUom(String asnUom) {
        this.asnUom = asnUom;
    }

    public String getAsnPackDesc() {
        return asnPackDesc;
    }

    public void setAsnPackDesc(String asnPackDesc) {
        this.asnPackDesc = asnPackDesc;
    }

    public String getAsnUomDesc() {
        return asnUomDesc;
    }

    public void setAsnUomDesc(String asnUomDesc) {
        this.asnUomDesc = asnUomDesc;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getPlanPaLoc() {
        return planPaLoc;
    }

    public void setPlanPaLoc(String planPaLoc) {
        this.planPaLoc = planPaLoc;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public String getIsQc() {
        return isQc;
    }

    public void setIsQc(String isQc) {
        this.isQc = isQc;
    }

    public String getQcPhase() {
        return qcPhase;
    }

    public void setQcPhase(String qcPhase) {
        this.qcPhase = qcPhase;
    }

    public String getQcRule() {
        return qcRule;
    }

    public void setQcRule(String qcRule) {
        this.qcRule = qcRule;
    }

    public String getItemGroupCode() {
        return itemGroupCode;
    }

    public void setItemGroupCode(String itemGroupCode) {
        this.itemGroupCode = itemGroupCode;
    }

    public String getSoLineNo() {
        return soLineNo;
    }

    public void setSoLineNo(String soLineNo) {
        this.soLineNo = soLineNo;
    }

    public String getCdRcvId() {
        return cdRcvId;
    }

    public void setCdRcvId(String cdRcvId) {
        this.cdRcvId = cdRcvId;
    }

}
