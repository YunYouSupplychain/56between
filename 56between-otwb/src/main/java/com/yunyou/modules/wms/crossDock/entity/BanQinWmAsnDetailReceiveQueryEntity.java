package com.yunyou.modules.wms.crossDock.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-02-14
 */
public class BanQinWmAsnDetailReceiveQueryEntity {
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
    private String orgId;
    private String cdType;

    private String rcvLineNo;
    private String asnLineNo;
    private String logisticNo;
    private String logisticLineNo;
    private String poNo;
    private String poLineNo;
    private String status;
    private String ownerCode;
    private String skuCode;
    private String asnPackCode;
    private String asnUom;
    private String asnPackDesc;
    private String asnUomDesc;
    private String soPackCode;
    private String soUom;
    private String soCdpaFormat;
    private String soUnitDesc;
    private Double qtyPlanEa;
    private Double qtyPlanUom;
    private String planId;
    private Double qtyRcvEa;
    private String toLoc;
    private String toId;
    private String paRule;
    private String paId;
    private Double price;
    private Date lotAtt01;
    private Date lotAtt02;
    private Date lotAtt03;
    private String lotAtt04;
    private String lotAtt05;
    private String lotAtt06;
    private String lotAtt07;
    private String lotAtt08;
    private String lotAtt09;
    private String lotAtt10;
    private String lotAtt11;
    private String lotAtt12;
    private Date ediSendTime;
    private String isEdiSend;
    private String remark;
    private Integer recVer;
    private String lotNum;
    private String reserveCode;
    private String planPaLoc;
    private String voucherNo;
    private String qcStatus;
    private String isQc;
    private String qcPhase;
    private String qcRule;
    private String itemGroupCode;
    private String soLineNo;
    private String cdRcvId;
    private String skuName;
    private String ownerName;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFmEtaFm() {
        return fmEtaFm;
    }

    public void setFmEtaFm(Date fmEtaFm) {
        this.fmEtaFm = fmEtaFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFmEtaTo() {
        return fmEtaTo;
    }

    public void setFmEtaTo(Date fmEtaTo) {
        this.fmEtaTo = fmEtaTo;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getToEtaFm() {
        return toEtaFm;
    }

    public void setToEtaFm(Date toEtaFm) {
        this.toEtaFm = toEtaFm;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getToEtaTo() {
        return toEtaTo;
    }

    public void setToEtaTo(Date toEtaTo) {
        this.toEtaTo = toEtaTo;
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

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCdType() {
        return cdType;
    }

    public void setCdType(String cdType) {
        this.cdType = cdType;
    }

    public String getRcvLineNo() {
        return rcvLineNo;
    }

    public void setRcvLineNo(String rcvLineNo) {
        this.rcvLineNo = rcvLineNo;
    }

    public String getAsnLineNo() {
        return asnLineNo;
    }

    public void setAsnLineNo(String asnLineNo) {
        this.asnLineNo = asnLineNo;
    }

    public String getLogisticNo() {
        return logisticNo;
    }

    public void setLogisticNo(String logisticNo) {
        this.logisticNo = logisticNo;
    }

    public String getLogisticLineNo() {
        return logisticLineNo;
    }

    public void setLogisticLineNo(String logisticLineNo) {
        this.logisticLineNo = logisticLineNo;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getPoLineNo() {
        return poLineNo;
    }

    public void setPoLineNo(String poLineNo) {
        this.poLineNo = poLineNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSoPackCode() {
        return soPackCode;
    }

    public void setSoPackCode(String soPackCode) {
        this.soPackCode = soPackCode;
    }

    public String getSoUom() {
        return soUom;
    }

    public void setSoUom(String soUom) {
        this.soUom = soUom;
    }

    public String getSoCdpaFormat() {
        return soCdpaFormat;
    }

    public void setSoCdpaFormat(String soCdpaFormat) {
        this.soCdpaFormat = soCdpaFormat;
    }

    public String getSoUnitDesc() {
        return soUnitDesc;
    }

    public void setSoUnitDesc(String soUnitDesc) {
        this.soUnitDesc = soUnitDesc;
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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Double getQtyRcvEa() {
        return qtyRcvEa;
    }

    public void setQtyRcvEa(Double qtyRcvEa) {
        this.qtyRcvEa = qtyRcvEa;
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

    public String getPaRule() {
        return paRule;
    }

    public void setPaRule(String paRule) {
        this.paRule = paRule;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Date getEdiSendTime() {
        return ediSendTime;
    }

    public void setEdiSendTime(Date ediSendTime) {
        this.ediSendTime = ediSendTime;
    }

    public String getIsEdiSend() {
        return isEdiSend;
    }

    public void setIsEdiSend(String isEdiSend) {
        this.isEdiSend = isEdiSend;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRecVer() {
        return recVer;
    }

    public void setRecVer(Integer recVer) {
        this.recVer = recVer;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getReserveCode() {
        return reserveCode;
    }

    public void setReserveCode(String reserveCode) {
        this.reserveCode = reserveCode;
    }

    public String getPlanPaLoc() {
        return planPaLoc;
    }

    public void setPlanPaLoc(String planPaLoc) {
        this.planPaLoc = planPaLoc;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

}
