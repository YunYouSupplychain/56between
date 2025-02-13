package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderSku;

public class TmTransportOrderSkuEntity extends TmTransportOrderSku {
    private static final long serialVersionUID = 3568819606021051086L;

    private String ownerName;
    private String skuName;
    private String skuWeight;// 商品重量
    private String skuCubic; // 商品体积
    private String consigneeCode;

    private TmTransportOrderHeader orderHeader;

    public TmTransportOrderSkuEntity() {
        super();
    }

    public TmTransportOrderSkuEntity(String id) {
        super(id);
    }

    public TmTransportOrderSkuEntity(String transportNo, String orgId) {
        super(transportNo, orgId);
    }

    public TmTransportOrderSkuEntity(String transportNo, String lineNo, String orgId) {
        super(transportNo, lineNo, orgId);
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

    public TmTransportOrderHeader getOrderHeader() {
        return orderHeader;
    }

    public void setOrderHeader(TmTransportOrderHeader orderHeader) {
        this.orderHeader = orderHeader;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }
}
