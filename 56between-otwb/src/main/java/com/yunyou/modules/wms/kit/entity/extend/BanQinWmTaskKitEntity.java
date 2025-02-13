package com.yunyou.modules.wms.kit.entity.extend;

import com.yunyou.modules.wms.kit.entity.BanQinWmTaskKit;

/**
 * 描述：加工任务Entity
 * <p>
 * create by Jianhua on 2019/8/21
 */
public class BanQinWmTaskKitEntity extends BanQinWmTaskKit {
    private static final long serialVersionUID = 3427420439985331465L;
    // 子件名称
    private String subSkuName;
    // 包装规格
    private String packDesc;
    // 包装单位描述
    private String uomDesc;
    // 包装单位换算数量
    private Long uomQty = 1L;
    // 拣货人名称
    private String pickOpName;
    // 加工人名称
    private String kitOpName;
    // 拣货包装数量
    private Double qtyPkUom = 0D;
    // 拣货EA数量
    private Double qtyPkEa = 0D;
    // 商品单体积
    private Double cubic = 0D;
    // 商品单净重
    private Double netWeight = 0D;
    // 商品单毛重
    private Double grossWeight = 0D;
    // 商品上架规则
    private String paRule;
    // 是否生成上架任务
    private String isTaskPa;
    // 上架任务是否推荐分配库位
    private String isAllocLoc;

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

    public Long getUomQty() {
        return uomQty;
    }

    public void setUomQty(Long uomQty) {
        this.uomQty = uomQty;
    }

    public String getPickOpName() {
        return pickOpName;
    }

    public void setPickOpName(String pickOpName) {
        this.pickOpName = pickOpName;
    }

    public String getKitOpName() {
        return kitOpName;
    }

    public void setKitOpName(String kitOpName) {
        this.kitOpName = kitOpName;
    }

    public Double getQtyPkUom() {
        return qtyPkUom;
    }

    public void setQtyPkUom(Double qtyPkUom) {
        this.qtyPkUom = qtyPkUom;
    }

    public Double getQtyPkEa() {
        return qtyPkEa;
    }

    public void setQtyPkEa(Double qtyPkEa) {
        this.qtyPkEa = qtyPkEa;
    }

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getPaRule() {
        return paRule;
    }

    public void setPaRule(String paRule) {
        this.paRule = paRule;
    }

    public String getIsTaskPa() {
        return isTaskPa;
    }

    public void setIsTaskPa(String isTaskPa) {
        this.isTaskPa = isTaskPa;
    }

    public String getIsAllocLoc() {
        return isAllocLoc;
    }

    public void setIsAllocLoc(String isAllocLoc) {
        this.isAllocLoc = isAllocLoc;
    }
}
