package com.yunyou.modules.wms.qc.entity;

/**
 * 描述：质检单明细扩展Entity
 *
 * @auther: Jianhua on 2019/1/28
 */
public class BanQinWmQcDetailEntity extends BanQinWmQcDetail {
    private String qcType;
    // 商品名称
    private String skuName;
    // 单位描述
    private String uomDesc;
    // 包装描述
    private String packDesc;
    // 质检规则
    private String qcRuleName;
    // 质检项组
    private String itemGroupName;
    // 包装数量
    private Double uomQty;
    // 可质检包装数
    private Double qtyAvailQcUom;
    // 快速录入码
    private String quickCode;
    // 体积
    private Double cubic;
    // 毛重
    private Double grossWeight;
    // 净重
    private Double netWeight;

    public String getQcType() {
        return qcType;
    }

    public void setQcType(String qcType) {
        this.qcType = qcType;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public String getQcRuleName() {
        return qcRuleName;
    }

    public void setQcRuleName(String qcRuleName) {
        this.qcRuleName = qcRuleName;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }

    public Double getUomQty() {
        return uomQty;
    }

    public void setUomQty(Double uomQty) {
        this.uomQty = uomQty;
    }

    public Double getQtyAvailQcUom() {
        return qtyAvailQcUom;
    }

    public void setQtyAvailQcUom(Double qtyAvailQcUom) {
        this.qtyAvailQcUom = qtyAvailQcUom;
    }

    public String getQuickCode() {
        return quickCode;
    }

    public void setQuickCode(String quickCode) {
        this.quickCode = quickCode;
    }

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
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
}
