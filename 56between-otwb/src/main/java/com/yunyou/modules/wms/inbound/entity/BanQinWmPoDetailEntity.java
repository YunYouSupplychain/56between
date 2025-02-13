package com.yunyou.modules.wms.inbound.entity;

/**
 * 描述：采购单明细扩展Entity
 *
 * @auther: Jianhua on 2019/1/29
 */
public class BanQinWmPoDetailEntity extends BanQinWmPoDetail {
    // 货主名称
    private String ownerName;
    // 商品名称
    private String skuName;
    // 单位描述
    private String uomDesc;
    // 包装描述
    private String packDesc;
    // 当前生成ASN数EA
    private Double currentQtyAsnEa;
    // 当前生成ASN单位数
    private Double currentQtyAsnUom;
    // 采购数
    private Double qtyPoUom;
    // 预收数
    private Double qtyAsnUom;
    // 已收数
    private Double qtyRcvUom;
    // 单位数量
    private Double uomQty;
    // 快速录入码
    private String quickCode;

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

    public Double getCurrentQtyAsnEa() {
        return currentQtyAsnEa;
    }

    public void setCurrentQtyAsnEa(Double currentQtyAsnEa) {
        this.currentQtyAsnEa = currentQtyAsnEa;
    }

    public Double getCurrentQtyAsnUom() {
        return currentQtyAsnUom;
    }

    public void setCurrentQtyAsnUom(Double currentQtyAsnUom) {
        this.currentQtyAsnUom = currentQtyAsnUom;
    }

    public Double getQtyPoUom() {
        return qtyPoUom;
    }

    public void setQtyPoUom(Double qtyPoUom) {
        this.qtyPoUom = qtyPoUom;
    }

    public Double getQtyAsnUom() {
        return qtyAsnUom;
    }

    public void setQtyAsnUom(Double qtyAsnUom) {
        this.qtyAsnUom = qtyAsnUom;
    }

    public Double getQtyRcvUom() {
        return qtyRcvUom;
    }

    public void setQtyRcvUom(Double qtyRcvUom) {
        this.qtyRcvUom = qtyRcvUom;
    }

    public Double getUomQty() {
        return uomQty;
    }

    public void setUomQty(Double uomQty) {
        this.uomQty = uomQty;
    }

    public String getQuickCode() {
        return quickCode;
    }

    public void setQuickCode(String quickCode) {
        this.quickCode = quickCode;
    }
}
