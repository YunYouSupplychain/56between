package com.yunyou.modules.wms.kit.entity.extend;

import com.yunyou.modules.wms.kit.entity.BanQinWmKitParentDetail;

/**
 * 描述：加工父件Entity
 * <p>
 * create by Jianhua on 2019/8/21
 */
public class BanQinWmKitParentDetailEntity extends BanQinWmKitParentDetail {
    private static final long serialVersionUID = -2559580267557278624L;
    // 父件名称
    private String parentSkuName;
    // 快速录入码
    private String skuQuickCode;
    // 保质期
    private Double shelfLife;
    // 包装规格
    private String packDesc;
    // 单位描述
    private String uomDesc;
    // 包装单位换算数量
    private Long uomQty = 1L;
    // 上架规则
    private String paRuleName;
    // 计划加工单位数量
    private Double qtyPlanUom = 0D;
    // 实际加工单位数量
    private Double qtyKitUom = 0D;
    // 父件加工结果
    private BanQinWmKitResultDetailEntity resultEntity;

    public String getParentSkuName() {
        return parentSkuName;
    }

    public void setParentSkuName(String parentSkuName) {
        this.parentSkuName = parentSkuName;
    }

    public String getSkuQuickCode() {
        return skuQuickCode;
    }

    public void setSkuQuickCode(String skuQuickCode) {
        this.skuQuickCode = skuQuickCode;
    }

    public Double getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Double shelfLife) {
        this.shelfLife = shelfLife;
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

    public String getPaRuleName() {
        return paRuleName;
    }

    public void setPaRuleName(String paRuleName) {
        this.paRuleName = paRuleName;
    }

    public Double getQtyPlanUom() {
        return qtyPlanUom;
    }

    public void setQtyPlanUom(Double qtyPlanUom) {
        this.qtyPlanUom = qtyPlanUom;
    }

    public Double getQtyKitUom() {
        return qtyKitUom;
    }

    public void setQtyKitUom(Double qtyKitUom) {
        this.qtyKitUom = qtyKitUom;
    }

    public BanQinWmKitResultDetailEntity getResultEntity() {
        return resultEntity;
    }

    public void setResultEntity(BanQinWmKitResultDetailEntity resultEntity) {
        this.resultEntity = resultEntity;
    }

    public Long getUomQty() {
        return uomQty;
    }

    public void setUomQty(Long uomQty) {
        this.uomQty = uomQty;
    }
}
