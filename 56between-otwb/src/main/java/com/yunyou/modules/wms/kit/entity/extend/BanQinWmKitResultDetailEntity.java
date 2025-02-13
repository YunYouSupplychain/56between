package com.yunyou.modules.wms.kit.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitResultDetail;

import java.util.List;

/**
 * 描述：加工结果Entity
 * <p>
 * create by Jianhua on 2019/8/21
 */
public class BanQinWmKitResultDetailEntity extends BanQinWmKitResultDetail {
    private static final long serialVersionUID = -60781917309182184L;
    // 父件名称
    private String parentSkuName;
    // 父件毛重
    private Double grossWeight = 0D;
    // 父件净重
    private Double netWeight = 0D;
    // 父件体积
    private Double cubic = 0D;
    // 包装规格
    private String packDesc;
    // 包装单位描述
    private String uomDesc;
    // 包装单位换算数量
    private Long uomQty = 1L;
    // 上架规则名称
    private String paRuleName;
    // 计划加工数UOM
    private Double qtyPlanUom = 0D;
    // 实际加工数UOM
    private Double qtyCompleteUom = 0D;
    // 当前可加工数UOM
    private Double qtyCurrentKitUom = 0D;
    // 当前可加工数UOM
    private Double qtyCurrentKitEa = 0D;
    // 加工工序
    private List<BanQinWmKitStepEntity> stepEntitys = Lists.newArrayList();

    public String getParentSkuName() {
        return parentSkuName;
    }

    public void setParentSkuName(String parentSkuName) {
        this.parentSkuName = parentSkuName;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
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

    public Long getUomQty() {
        return uomQty;
    }

    public void setUomQty(Long uomQty) {
        this.uomQty = uomQty;
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

    public Double getQtyCompleteUom() {
        return qtyCompleteUom;
    }

    public void setQtyCompleteUom(Double qtyCompleteUom) {
        this.qtyCompleteUom = qtyCompleteUom;
    }

    public Double getQtyCurrentKitUom() {
        return qtyCurrentKitUom;
    }

    public void setQtyCurrentKitUom(Double qtyCurrentKitUom) {
        this.qtyCurrentKitUom = qtyCurrentKitUom;
    }

    public Double getQtyCurrentKitEa() {
        return qtyCurrentKitEa;
    }

    public void setQtyCurrentKitEa(Double qtyCurrentKitEa) {
        this.qtyCurrentKitEa = qtyCurrentKitEa;
    }

    public List<BanQinWmKitStepEntity> getStepEntitys() {
        return stepEntitys;
    }

    public void setStepEntitys(List<BanQinWmKitStepEntity> stepEntitys) {
        this.stepEntitys = stepEntitys;
    }
}
