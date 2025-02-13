package com.yunyou.modules.tms.applet.entity.extend;

import com.yunyou.common.utils.IdGen;

import java.io.Serializable;
import java.util.Date;

public class TmDemandPlanDetailEntity implements Serializable {

    private String id;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    private String remarks;
    private String delFlag;
    private String orgId;
    private String planOrderNo;
    private String ownerCode;
    private String skuCode;
    private Double qty;
    private Long recVer = 0l;
    private String baseOrgId;

    private String unit;
    private String skuName;

    public TmDemandPlanDetailEntity() {
        this.delFlag = "0";
        this.id = IdGen.uuid();
    }

    public TmDemandPlanDetailEntity(String planOrderNo, String orgId) {
        this.planOrderNo = planOrderNo;
        this.orgId = orgId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Long getRecVer() {
        return recVer;
    }

    public void setRecVer(Long recVer) {
        this.recVer = recVer;
    }

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}