package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WMSRF_PA_QueryPutAwayTaskByTaskNo_Response implements Serializable {
    // 主键
    private String orgId;
    // 上架任务ID
    private String paId;
    // 上架任务行号
    private String lineNo;
    // 状态
    private String status;
    // 单据号
    private String orderNo;
    // 单据类型
    private String orderType;
    // 货主编码
    private String ownerCode;
    // 货主名称
    private String ownerName;
    // 商品编码
    private String skuCode;
    //
    private String skuName;
    // 批次号
    private String lotNum;
    // 源库位编码
    private String fmLoc;
    // 源跟踪号
    private String fmId;
    // 目标库位编码
    private String toLoc;
    // 目标跟踪号
    private String toId;
    // 推荐库位
    private String suggestLoc;
    // 包装编码
    private String packCode;
    // 包装规格
    private String packDesc;
    // 包装单位
    private String uom;
    private String uomDesc;
    // 上架包装数量
    private Double qtyPaUom;
    // 上架EA数
    private Double qtyPaEa;
    // 上架人员
    private String paOp;
    // 上架时间
    private Date paTime;
    // 批次属性配置
//    private WMSRF_CF_ProductionLotConfig[] lotConfigs;
    // 包装规格
    private List<WMSRF_CF_ProductionPackageConfig> packageConfigs;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
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

    public String getSuggestLoc() {
        return suggestLoc;
    }

    public void setSuggestLoc(String suggestLoc) {
        this.suggestLoc = suggestLoc;
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

    public String getPaOp() {
        return paOp;
    }

    public void setPaOp(String paOp) {
        this.paOp = paOp;
    }

    public Date getPaTime() {
        return paTime;
    }

    public void setPaTime(Date paTime) {
        this.paTime = paTime;
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
}
