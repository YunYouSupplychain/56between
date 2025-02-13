package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WMSRF_RP_ReplenishmentDetail_Response implements Serializable {
    // 主键
    private String id;
    // 补货ID
    private String rpId;
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
    // 包装编码
    private String packCode;
    // 包装规格
    private String packDesc;
    // 包装单位
    private String uom;
    // 补货包装数量
    private Double qtyRpUom;
    // 补货EA数量
    private Double qtyRpEa;
    // 补货人员
    private String rpOp;
    // 补货时间
    private Date rpTime;
    // 分公司
    private String orgId;
    // 包装规格
    private List<WMSRF_CF_ProductionPackageConfig> packageConfigs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRpId() {
        return rpId;
    }

    public void setRpId(String rpId) {
        this.rpId = rpId;
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

    public Double getQtyRpUom() {
        return qtyRpUom;
    }

    public void setQtyRpUom(Double qtyRpUom) {
        this.qtyRpUom = qtyRpUom;
    }

    public Double getQtyRpEa() {
        return qtyRpEa;
    }

    public void setQtyRpEa(Double qtyRpEa) {
        this.qtyRpEa = qtyRpEa;
    }

    public String getRpOp() {
        return rpOp;
    }

    public void setRpOp(String rpOp) {
        this.rpOp = rpOp;
    }

    public Date getRpTime() {
        return rpTime;
    }

    public void setRpTime(Date rpTime) {
        this.rpTime = rpTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<WMSRF_CF_ProductionPackageConfig> getPackageConfigs() {
        return packageConfigs;
    }

    public void setPackageConfigs(List<WMSRF_CF_ProductionPackageConfig> packageConfigs) {
        this.packageConfigs = packageConfigs;
    }
}
