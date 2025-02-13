package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmPreTransportOrderSku;

public class TmPreTransportOrderSkuEntity extends TmPreTransportOrderSku {
    private static final long serialVersionUID = 3568819606021051086L;

    private String ownerName;
    private String skuName;
    private String skuWeight;// 商品重量
    private String skuCubic; // 商品体积

    public TmPreTransportOrderSkuEntity() {
        super();
    }

    public TmPreTransportOrderSkuEntity(String id) {
        super(id);
    }

    public TmPreTransportOrderSkuEntity(String transportNo, String orgId) {
        super(transportNo, orgId);
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

    public String getSkuWeight() {
        return skuWeight;
    }

    public void setSkuWeight(String skuWeight) {
        this.skuWeight = skuWeight;
    }

    public String getSkuCubic() {
        return skuCubic;
    }

    public void setSkuCubic(String skuCubic) {
        this.skuCubic = skuCubic;
    }
}
