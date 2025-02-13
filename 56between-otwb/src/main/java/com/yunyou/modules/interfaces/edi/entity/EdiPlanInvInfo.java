package com.yunyou.modules.interfaces.edi.entity;

import java.io.Serializable;

public class EdiPlanInvInfo implements Serializable {

    private static final long serialVersionUID = 5030552713221104902L;
    private String orgId;// 机构ID
    private String warehouse;// 仓库
    private String ownerCode;// 货主编码
    private String ownerName;// 货主名称
    private String skuCode;// 商品编码
    private String skuName;// 商品名称
    private String skuSpec;// 规格型号
    private String uom;// 单位
    private Double qtyEa;// 库存数量
    private Double qtyBox;// 库存箱数
    private String lockStatus;// 锁定情况
    private Double qtyAvailable;// 可用库存
    private Double qtyPlan;// 计划库存

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
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

    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQtyEa() {
        return qtyEa;
    }

    public void setQtyEa(Double qtyEa) {
        this.qtyEa = qtyEa;
    }

    public Double getQtyBox() {
        return qtyBox;
    }

    public void setQtyBox(Double qtyBox) {
        this.qtyBox = qtyBox;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(Double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    public Double getQtyPlan() {
        return qtyPlan;
    }

    public void setQtyPlan(Double qtyPlan) {
        this.qtyPlan = qtyPlan;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
