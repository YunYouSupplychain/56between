package com.yunyou.modules.wms.kit.entity.extend;

import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;

/**
 * 描述：子件明细Entity
 * <p>
 * create by Jianhua on 2019/8/21
 */
public class BanQinWmKitSubDetailEntity extends BanQinWmKitSubDetail {
    private static final long serialVersionUID = 357772585205351423L;
    // 子件名称
    private String subSkuName;
    // 包装规格
    private String packDesc;
    // 包装单位描述
    private String uomDesc;
    // 包装单位换算数量
    private Long uomQty = 1L;
    // 分配规则名称
    private String allocRuleName;
    // 库存周转规则名称
    private String rotationRuleName;
    // 区域名称
    private String areaName;
    // 库区名称
    private String zoneName;
    // 待加工子件数UOM
    private Double qtyPlanUom = 0D;
    // 分配数UOM
    private Double qtyAllocUom = 0D;
    // 已拣货数UOM
    private Double qtyPkUom = 0D;
    // 已加工数UOM
    private Double qtyKitUom = 0D;
    // 计划加工台
    private String planKitLoc;

    public Long getUomQty() {
        return uomQty;
    }

    public void setUomQty(Long uomQty) {
        this.uomQty = uomQty;
    }

    public String getSubSkuName() {
        return subSkuName;
    }

    public void setSubSkuName(String subSkuName) {
        this.subSkuName = subSkuName;
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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Double getQtyPlanUom() {
        return qtyPlanUom;
    }

    public void setQtyPlanUom(Double qtyPlanUom) {
        this.qtyPlanUom = qtyPlanUom;
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

    public Double getQtyKitUom() {
        return qtyKitUom;
    }

    public void setQtyKitUom(Double qtyKitUom) {
        this.qtyKitUom = qtyKitUom;
    }

    public String getPlanKitLoc() {
        return planKitLoc;
    }

    public void setPlanKitLoc(String planKitLoc) {
        this.planKitLoc = planKitLoc;
    }
}
