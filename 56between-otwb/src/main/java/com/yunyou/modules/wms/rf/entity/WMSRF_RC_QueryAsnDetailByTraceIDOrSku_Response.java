package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WMSRF_RC_QueryAsnDetailByTraceIDOrSku_Response implements Serializable {
    // 用户Id
    private String id;
    // 入库单号
    private String asnNo;
    // 收货明细行号
    private String lineNo;
    // 商品明细行行号
    private String asnLineNo;
    // 物流单号
    private String logisticNo;
    // 物流单行号
    private String logisticLineNo;
    // 采购单号
    private String poNo;
    // 采购单行号
    private String poLineNo;
    // 状态
    private String status;
    // 货主编码
    private String ownerCode;
    // 货主名称
    private String ownerName;
    // 商品编码
    private String skuCode;
    // 商品名称
    private String skuName;
    // 是否序列号管理
    private String isSerial;
    // 收货时间
    private Date rcvTime;
    // 包装编码
    private String packCode;
    // 包装规格
    private String packDesc;
    // 包装单位
    private String uom;
    // 包装单位名称
    private String uomDesc;
    // 计划数EA
    private Double qtyPlanEa;
    // 码盘计划跟踪号
    private String planId;
    // 收货数量EA
    private Double qtyRcvEa;
    // 收货库位编码
    private String toLoc;
    // 收货跟踪号
    private String toId;
    // 上架规则
    private String paRule;
    // 上架任务ID
    private String paId;
    // 价格
    private Double price;
    // 批次属性1(生产日期)
    private String lotAtt01;
    // 批次属性2(失效日期)
    private String lotAtt02;
    // 批次属性3(入库日期)
    private String lotAtt03;
    // 批次属性4
    private String lotAtt04;
    // 批次属性5
    private String lotAtt05;
    // 批次属性6
    private String lotAtt06;
    // 批次属性7
    private String lotAtt07;
    // 批次属性8
    private String lotAtt08;
    // 批次属性9
    private String lotAtt09;
    // 批次属性10
    private String lotAtt10;
    // 批次属性11
    private String lotAtt11;
    // 批次属性12
    private String lotAtt12;
    // 分公司
    private String orgId;
    // 是否码盘
    private String isPalletize;
    // 批次属性配置
    private List<WMSRF_CF_ProductionLotConfig> lotConfigs;
    // 包装规格
    private List<WMSRF_CF_ProductionPackageConfig> packageConfigs;
    //
    private Long cdprTI;
    //
    private Long cdprHI;
    // 条码
    private String barcode;
    //
    private Integer recVer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(String isSerial) {
        this.isSerial = isSerial;
    }

    public Date getRcvTime() {
        return rcvTime;
    }

    public void setRcvTime(Date rcvTime) {
        this.rcvTime = rcvTime;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQtyPlanEa() {
        return qtyPlanEa;
    }

    public void setQtyPlanEa(Double qtyPlanEa) {
        this.qtyPlanEa = qtyPlanEa;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIsPalletize() {
        return isPalletize;
    }

    public void setIsPalletize(String isPalletize) {
        this.isPalletize = isPalletize;
    }

    public Long getCdprTI() {
        return cdprTI;
    }

    public void setCdprTI(Long cdprTI) {
        this.cdprTI = cdprTI;
    }

    public Long getCdprHI() {
        return cdprHI;
    }

    public void setCdprHI(Long cdprHI) {
        this.cdprHI = cdprHI;
    }

    public List<WMSRF_CF_ProductionLotConfig> getLotConfigs() {
        return lotConfigs;
    }

    public void setLotConfigs(List<WMSRF_CF_ProductionLotConfig> lotConfigs) {
        this.lotConfigs = lotConfigs;
    }

    public List<WMSRF_CF_ProductionPackageConfig> getPackageConfigs() {
        return packageConfigs;
    }

    public void setPackageConfigs(List<WMSRF_CF_ProductionPackageConfig> packageConfigs) {
        this.packageConfigs = packageConfigs;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getRecVer() {
        return recVer;
    }

    public void setRecVer(Integer recVer) {
        this.recVer = recVer;
    }
}
