package com.yunyou.modules.oms.inv.entity;

/**
 * 描述：
 *
 * @auther: Jianhua on 2019/5/9
 */
public class OmSaleInventoryEntity extends OmSaleInventory {
    private static final long serialVersionUID = 5410757578570845750L;

    private String warehouseName;// 仓库名称
    private String ownerName;   // 货主名称
    private String skuName;     // 商品名称
    private Double qty;         // 数量
    private Double qtyAvailable;// 可用数量
    private String uom;         // 单位
    private String uomDesc;     // 单位描述
    private Double qtyUom;      // 单位数量
    private String status;      // 状态

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(Double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public Double getQtyUom() {
        return qtyUom;
    }

    public void setQtyUom(Double qtyUom) {
        this.qtyUom = qtyUom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
