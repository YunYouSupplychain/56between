package com.yunyou.modules.wms.outbound.entity;

import java.util.Date;

/**
 * @author WMJ
 * @version 2020-02-17
 */
public class BanQinWmSoDetailByCdQuery {
    private String ownerCode;
    private String skuCode;
    private String status;
    private String waveNo;
    private String soNo;
    private String lineNo;
    private String[] cdTypes;
    private String orgId;
    // 是否越库标记
    private String isCd;
    // 组合条件，SoNo,soLineNo
    private String soAndLineNos;
    // sku查询条件
    private String[] soTypes;
    private Date fmEtdFm;
    private Date fmEtdTo;
    private Date toEtdFm;
    private Date toEtdTo;
    private Integer soLineNum;// 发运订单行数
    private Double qtySoEa;// 总订货数

    private String id;
    private String logisticNo;
    private Date ediSendTime;
    private String cdType;
    private String isEdiSend;
    private String ldStatus;
    private String def1;
    private String def2;
    private String def3;
    private String def4;
    private String def5;
    private String def6;
    private String def7;
    private String def8;
    private String def9;
    private String def10;
    private String remarks;
    private Integer recVer;
    private String logisticLineNo;
    private String packCode;
    private String uom;
    private Double qtyPreallocEa;
    private Double qtyAllocEa;
    private Double qtyPkEa;
    private Double qtyShipEa;
    private String rotationRule;
    private String preallocRule;
    private String allocRule;
    private Double price;
    private String areaCode;
    private String zoneCode;
    private String locCode;
    private String traceId;
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
    private String oldLineNo;
    private String ownerName;
    private String skuName;
    private String skuQuickCode;
    private String packDesc;
    private String uomDesc;
    private Double uomQty;
    private String preallocRuleName;
    private String allocRuleName;
    private String rotationRuleName;
    private String zoneName;
    private String areaName;
    private Double qtySoUom;
    private Double qtyPreallocUom;
    private Double qtyAllocUom;
    private Double qtyPkUom;
    private Double qtyShipUom;
    private String soType;
    private String saleNo;
    private String saleLineNo;
    private String headId;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWaveNo() {
        return waveNo;
    }

    public void setWaveNo(String waveNo) {
        this.waveNo = waveNo;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String[] getCdTypes() {
        return cdTypes;
    }

    public void setCdTypes(String[] cdTypes) {
        this.cdTypes = cdTypes;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIsCd() {
        return isCd;
    }

    public void setIsCd(String isCd) {
        this.isCd = isCd;
    }

    public String getSoAndLineNos() {
        return soAndLineNos;
    }

    public void setSoAndLineNos(String soAndLineNos) {
        this.soAndLineNos = soAndLineNos;
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

    public Integer getSoLineNum() {
        return soLineNum;
    }

    public void setSoLineNum(Integer soLineNum) {
        this.soLineNum = soLineNum;
    }

    public Double getQtySoEa() {
        return qtySoEa;
    }

    public void setQtySoEa(Double qtySoEa) {
        this.qtySoEa = qtySoEa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogisticNo() {
        return logisticNo;
    }

    public void setLogisticNo(String logisticNo) {
        this.logisticNo = logisticNo;
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

    public String getLdStatus() {
        return ldStatus;
    }

    public void setLdStatus(String ldStatus) {
        this.ldStatus = ldStatus;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef10() {
        return def10;
    }

    public void setDef10(String def10) {
        this.def10 = def10;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getRecVer() {
        return recVer;
    }

    public void setRecVer(Integer recVer) {
        this.recVer = recVer;
    }

    public String getLogisticLineNo() {
        return logisticLineNo;
    }

    public void setLogisticLineNo(String logisticLineNo) {
        this.logisticLineNo = logisticLineNo;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQtyPreallocEa() {
        return qtyPreallocEa;
    }

    public void setQtyPreallocEa(Double qtyPreallocEa) {
        this.qtyPreallocEa = qtyPreallocEa;
    }

    public Double getQtyAllocEa() {
        return qtyAllocEa;
    }

    public void setQtyAllocEa(Double qtyAllocEa) {
        this.qtyAllocEa = qtyAllocEa;
    }

    public Double getQtyPkEa() {
        return qtyPkEa;
    }

    public void setQtyPkEa(Double qtyPkEa) {
        this.qtyPkEa = qtyPkEa;
    }

    public Double getQtyShipEa() {
        return qtyShipEa;
    }

    public void setQtyShipEa(Double qtyShipEa) {
        this.qtyShipEa = qtyShipEa;
    }

    public String getRotationRule() {
        return rotationRule;
    }

    public void setRotationRule(String rotationRule) {
        this.rotationRule = rotationRule;
    }

    public String getPreallocRule() {
        return preallocRule;
    }

    public void setPreallocRule(String preallocRule) {
        this.preallocRule = preallocRule;
    }

    public String getAllocRule() {
        return allocRule;
    }

    public void setAllocRule(String allocRule) {
        this.allocRule = allocRule;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
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

    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public Date getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(Date lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

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

    public String getOldLineNo() {
        return oldLineNo;
    }

    public void setOldLineNo(String oldLineNo) {
        this.oldLineNo = oldLineNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuQuickCode() {
        return skuQuickCode;
    }

    public void setSkuQuickCode(String skuQuickCode) {
        this.skuQuickCode = skuQuickCode;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public Double getUomQty() {
        return uomQty;
    }

    public void setUomQty(Double uomQty) {
        this.uomQty = uomQty;
    }

    public String getPreallocRuleName() {
        return preallocRuleName;
    }

    public void setPreallocRuleName(String preallocRuleName) {
        this.preallocRuleName = preallocRuleName;
    }

    public String getAllocRuleName() {
        return allocRuleName;
    }

    public void setAllocRuleName(String allocRuleName) {
        this.allocRuleName = allocRuleName;
    }

    public String getRotationRuleName() {
        return rotationRuleName;
    }

    public void setRotationRuleName(String rotationRuleName) {
        this.rotationRuleName = rotationRuleName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Double getQtySoUom() {
        return qtySoUom;
    }

    public void setQtySoUom(Double qtySoUom) {
        this.qtySoUom = qtySoUom;
    }

    public Double getQtyPreallocUom() {
        return qtyPreallocUom;
    }

    public void setQtyPreallocUom(Double qtyPreallocUom) {
        this.qtyPreallocUom = qtyPreallocUom;
    }

    public Double getQtyAllocUom() {
        return qtyAllocUom;
    }

    public void setQtyAllocUom(Double qtyAllocUom) {
        this.qtyAllocUom = qtyAllocUom;
    }

    public Double getQtyPkUom() {
        return qtyPkUom;
    }

    public void setQtyPkUom(Double qtyPkUom) {
        this.qtyPkUom = qtyPkUom;
    }

    public Double getQtyShipUom() {
        return qtyShipUom;
    }

    public void setQtyShipUom(Double qtyShipUom) {
        this.qtyShipUom = qtyShipUom;
    }

    public String getSoType() {
        return soType;
    }

    public void setSoType(String soType) {
        this.soType = soType;
    }

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public String getSaleLineNo() {
        return saleLineNo;
    }

    public void setSaleLineNo(String saleLineNo) {
        this.saleLineNo = saleLineNo;
    }

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }

    public String getCdType() {
        return cdType;
    }

    public void setCdType(String cdType) {
        this.cdType = cdType;
    }
}
