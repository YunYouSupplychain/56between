package com.yunyou.modules.wms.inbound.entity;

import java.util.List;

/**
 * 入库单明细扩展Entity
 *
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinWmAsnDetailEntity extends BanQinWmAsnDetail {

    private static final long serialVersionUID = 1L;
    private String ownerName;
    // 商品名称
    private String skuName;
    // 单位描述
    private String uomDesc;
    // 包装描述
    private String packDesc;
    // 上架规则
    private String paRuleName;
    // 包装数量
    private Double uomQty;
    // 托盘包装数量
    private Double plQty;
    // 预收单位数量
    private Double qtyAsnUom;
    // 收货单位数量
    private Double qtyRcvUom;
    // 质检规则
    private String qcRuleName;
    // 质检项
    private String itemGroupName;
    // 保质期
    private Double shelfLife;
    // 快速录入码
    private String quickCode;
    // 当前生成质检单的可质检数（用于生成质检单）
    private Double currentQtyQc;
    // 收货明细
    List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntities;

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

    public String getPaRuleName() {
        return paRuleName;
    }

    public void setPaRuleName(String paRuleName) {
        this.paRuleName = paRuleName;
    }

    public Double getUomQty() {
        return uomQty;
    }

    public void setUomQty(Double uomQty) {
        this.uomQty = uomQty;
    }

    public Double getPlQty() {
        return plQty;
    }

    public void setPlQty(Double plQty) {
        this.plQty = plQty;
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

    public Double getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Double shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getQuickCode() {
        return quickCode;
    }

    public void setQuickCode(String quickCode) {
        this.quickCode = quickCode;
    }

    public Double getCurrentQtyQc() {
        return currentQtyQc;
    }

    public void setCurrentQtyQc(Double currentQtyQc) {
        this.currentQtyQc = currentQtyQc;
    }

    public List<BanQinWmAsnDetailReceiveEntity> getWmAsnDetailReceiveEntities() {
        return wmAsnDetailReceiveEntities;
    }

    public void setWmAsnDetailReceiveEntities(List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntities) {
        this.wmAsnDetailReceiveEntities = wmAsnDetailReceiveEntities;
    }
}