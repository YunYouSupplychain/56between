package com.yunyou.modules.tms.order.entity;

import com.yunyou.core.persistence.DataEntity;

public class TmRepairOrderDetail extends DataEntity<TmRepairOrderDetail> {
    private static final long serialVersionUID = -1794704960328970486L;
    private String repairNo;    // 维修单号
    private String repairCode;  // 维修单位
    private String ownerCode;   // 客户编码
    private String skuCode;     // 配件编码
    private String skuModel;    // 配件型号
    private Double qty;         // 数量
    private Double price;       // 单价
    private Double amount;      // 金额
    private Double workHour;    // 工时
    private Double workHourCost;// 工时费
    private Double totalAmount; // 小计
    private Long recVer = 0L;   // 版本号
    private String orgId;       // 机构ID
    private String baseOrgId;   // 基础数据机构ID

    public TmRepairOrderDetail() {
    }

    public TmRepairOrderDetail(String id) {
        super(id);
    }

    public TmRepairOrderDetail(String repairNo, String orgId) {
        this.repairNo = repairNo;
        this.orgId = orgId;
    }

    public String getRepairNo() {
        return repairNo;
    }

    public void setRepairNo(String repairNo) {
        this.repairNo = repairNo;
    }

    public String getRepairCode() {
        return repairCode;
    }

    public void setRepairCode(String repairCode) {
        this.repairCode = repairCode;
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

    public String getSkuModel() {
        return skuModel;
    }

    public void setSkuModel(String skuModel) {
        this.skuModel = skuModel;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
    }

    public Double getWorkHourCost() {
        return workHourCost;
    }

    public void setWorkHourCost(Double workHourCost) {
        this.workHourCost = workHourCost;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }
}
