package com.yunyou.modules.wms.outbound.entity;

/**
 * 取消分配拣货记录Entity
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinWmDelAllocEntity extends BanQinWmDelAlloc {
	
	private static final long serialVersionUID = 1L;
    // 货主名称
    private String ownerName;
    // 商品名称
    private String skuName;
    // 收货人名称
    private String consigneeName;
    // 操作人名称
    private String opName;
    // 创建人名称
    private String creatorName;
    // 修改人名称
    private String modifierName;
    // 包装规格
    private String packDesc;
    // 包装单位描述
    private String uomDesc;
    // 商品单体积
    private Double cubic;
    // 商品单净重
    private Double netWeight;
    // 商品单毛重
    private Double grossWeight;
    // 商品上架规则
    private String paRule;

    // 用做页面传参赋值用
    private String soNo;
    private String isTaskPa;
    private String isAllocLoc;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
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

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
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